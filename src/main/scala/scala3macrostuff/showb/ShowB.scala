// Initially based on https://www.youtube.com/watch?v=NLCDjUMw61U
// (code: https://github.com/cb372/type-class-derivation-in-scala-3).
// But some of the syntax seems to have changed.
// Then did adjustments according to https://blog.shangjiaming.com/scala3-implicit/

package scala3macrostuff.showb

import scala.quoted._
import scala.compiletime._
import scala.deriving._

trait ShowB[A]:
  extension (a: A) def show: String

given intCfgGen: ShowB[Int] with
  extension (a: Int) def show: String = a.toString()

given stringCfgGen: ShowB[String] with
  extension (a: String) def show: String = s""""$a""""

// well, handling structural types in a direct way for configuration handling purposes
// (like in the scalameta-based implementation of carueda/cfg)
// doesn't seem possible: you always have to predefine the type.

given structCfgGen: ShowB[Object{val y: Int}] with  
  extension (a: Object{val y: Int}) def show: String = s"""any($a)"""
  
class SRecord(elems: (String, Any)*) extends Selectable:
  private val fields                   = elems.toMap
  def selectDynamic(name: String): Any = fields(name)

given selCfgGen: ShowB[SRecord{val name: String; val age: Int}] with
  extension (a: SRecord{val name: String; val age: Int}) def show: String = s"""record($a)"""

object ShowB:
  inline def derived[A](using m: Mirror.Of[A]): ShowB[A] = new ShowB[A] {
    extension (a: A)
      def show: String =
        inline m match
          case given Mirror.ProductOf[A] => showProduct(a)
          case given Mirror.SumOf[A]     => showSum(a)
  }

  private inline def showProduct[A](a: A)(using
      m: Mirror.ProductOf[A]
  ): String =
    val productName: String       = constValue[m.MirroredLabel]
    val names: List[String]       = elemLabels[m.MirroredElemLabels]
    val instances: List[ShowB[_]] = summonAll[m.MirroredElemTypes]

    val values: List[Any] = a.asInstanceOf[Product].productIterator.toList

    val fields = (names zip (instances zip values)).map {
      case (name, (instance, value)) =>
        val valueStr = instance.asInstanceOf[ShowB[Any]].show(value)
        s"$name = $valueStr"
    }
    if fields.isEmpty then productName
    else s"$productName(${fields.mkString(", ")})"

  private inline def elemLabels[T <: Tuple]: List[String] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts)  => constValue[t].asInstanceOf[String] :: elemLabels[ts]

  private inline def summonAll[T <: Tuple]: List[ShowB[_]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts)  => summonInline[ShowB[t]] :: summonAll[ts]

  private inline def showSum[A](a: A)(using m: Mirror.SumOf[A]): String =
    val ord = m.ordinal(a)
    showCase[A, m.MirroredElemTypes](0, ord, a)

  private inline def showCase[A, T](n: Int, ord: Int, a: A): String =
    inline erasedValue[T] match
      case _: EmptyTuple => ""
      case _: (t *: ts) =>
        if n == ord then
          summonFrom { case given Mirror.ProductOf[`t`] =>
            showProduct[t](a.asInstanceOf[t])
          }
        else showCase[A, ts](n + 1, ord, a)
