package scala3macrostuff.lifting

// https://docs.scala-lang.org/scala3/reference/metaprogramming/macros.html#lifting-expressions
// Bu seems like very terse documentation ;(
// Also see, https://users.scala-lang.org/t/solved-dotty-macros-lifting-expressions-how-to-set-this-up/6424

import Exp.*

@main def main: Unit =
  assertExp(42, compileExp(Num(42)) )
  assertExp(42, compileExp(Num(42)) )
  assertExp(42, compileExp(Let("x", Num(42), Var("x"))) ) // oops: expected=41 != compiled=42:Int
  assertExp(42, compileExp(Plus(Num(21), Plus(Num(8), Num(13)))) )
  assertExp(42, compileExp(Let("x", Num(3), Plus(Plus(Num(18), Var("x")), Num(21)))) )
  // assertExp(41, compileExp(Let("x", Num(42), Var("x"))) )