// Initially based on https://www.youtube.com/watch?v=NLCDjUMw61U
// (code: https://github.com/cb372/type-class-derivation-in-scala-3).
// But some of the syntax seems to have changed.
// Then did adjustments according to https://blog.shangjiaming.com/scala3-implicit/

package scala3macrostuff.showb

import scala.quoted._
import scala.compiletime._
import scala.deriving._

trait ShowB[A]:
  extension (v: A) {
    def show: String
  }

given intCfgGen: ShowB[Int] with
  extension (a: Int) {
    def show: String = a.toString()
  }

given stringCfgGen: ShowB[String] with
  extension (a: String) {
    def show: String = s""""$a""""
  }

object ShowB:

  inline def derived[A](using m: Mirror.Of[A]): ShowB[A] = new ShowB[A] {
    extension (a: A) {
      def show: String =
        inline m match {
          case p: Mirror.ProductOf[A] => showProduct(p, a)
          case s: Mirror.SumOf[A]     => showSum(s, a)
        }
    }
  }

  private inline def showProduct[A](m: Mirror.ProductOf[A], a: A): String =
    val productName: String       = constValue[m.MirroredLabel]
    val names: List[String]       = elemLabels[m.MirroredElemLabels]
    val instances: List[ShowB[_]] = summonAll[m.MirroredElemTypes]

    val values: List[Any] = a.asInstanceOf[Product].productIterator.toList

    val fields = (names zip (instances zip values)).map {
      case (name, (instance, value)) =>
        val valueStr = instance.asInstanceOf[ShowB[Any]].show(value)
        s"$name = $valueStr"
    }
    s"$productName(${fields.mkString(", ")})"

  private inline def showSum[A](p: Mirror.SumOf[A], a: A): String =
    "SUM-TODO"

  private inline def elemLabels[T <: Tuple]: List[String] =
    inline erasedValue[T] match {
      case _: EmptyTuple => Nil
      case _: (t *: ts)  => constValue[t].asInstanceOf[String] :: elemLabels[ts]
    }

  private inline def summonAll[T <: Tuple]: List[ShowB[_]] =
    inline erasedValue[T] match {
      case _: EmptyTuple => Nil
      case _: (t *: ts)  => summonInline[ShowB[t]] :: summonAll[ts]
    }