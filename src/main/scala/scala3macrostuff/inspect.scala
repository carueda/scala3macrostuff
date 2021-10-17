// https://docs.scala-lang.org/scala3/guides/macros/macros.html

package scala3macrostuff

import scala.compiletime.{error, codeOf}
import scala.quoted.Expr
import scala.quoted.Quotes

inline def inspect(inline x: Any): Any = ${
  inspectCode('x) 
}

def inspectCode(x: Expr[Any])(using Quotes): Expr[Any] =
  scribe.warn(x.show)
  x
