// https://docs.scala-lang.org/scala3/guides/macros/macros.html

package scala3macrostuff

import scala.compiletime.{error, codeOf}
import scala.quoted.Expr
import scala.quoted.Quotes

inline def power(inline x: Double, inline n: Int) =
  ${ powerCode('x, 'n) }

private def powerCode(x: Expr[Double], n: Expr[Int])(using
    Quotes
): Expr[Double] =
  val value: Double = pow(x.valueOrError, n.valueOrError)
  Expr(value)

private def pow(x: Double, n: Int): Double =
  if n == 0 then 1 else x * pow(x, n - 1)
