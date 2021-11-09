// https://docs.scala-lang.org/scala3/guides/macros/reflection.html

package scala3macrostuff.reflection

import scala.compiletime.{error, codeOf}
import scala.quoted.Expr
import scala.quoted.Quotes
import scala.quoted.quotes

inline def typeRefl[T](inline x: T): String =
  ${ typeReflImpl('{ x }) }

def typeReflImpl[T](x: Expr[T])(using Quotes): Expr[String] =
  import quotes.reflect.*

  val tree: Term = x.asTerm

  tree match
    case Inlined(_, _, Literal(IntConstant(n))) =>
      if n <= 0 then
        '{"Parameter must be natural number"}
      else
        tree.asExprOf[Int]
    case _ =>
      scribe.warn(s"tree = $tree")
      '{"Parameter must be a known constant"}

  Expr("pending")
