// https://docs.scala-lang.org/scala3/guides/macros/compiletime.html

package scala3macrostuff

import scala.compiletime.{error, codeOf}

inline def doSomething(inline mode: Boolean): Unit =
  if mode then ()
  else if !mode then ()
  else error("Mode must be a known value but got: " + codeOf(mode))
