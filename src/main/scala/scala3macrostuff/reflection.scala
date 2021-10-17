// https://docs.scala-lang.org/scala3/guides/macros/reflection.html

package scala3macrostuff

import scala.compiletime.{error, codeOf}
import scala.quoted.Expr
import scala.quoted.Quotes
import scala.quoted.quotes

inline def refl(inline n: Int) = ${ reflImpl('n) }

private def reflImpl(x: Expr[Int])(using Quotes): Expr[Int] =
  import quotes.reflect.*
  val tree: Term = x.asTerm
  scribe.warn(s"tree = $tree")
  val expr: Expr[Int] = tree.asExprOf[Int]
  scribe.warn(s"expr = $expr")
  expr
