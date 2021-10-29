package scala3macrostuff.lifting

// https://docs.scala-lang.org/scala3/reference/metaprogramming/macros.html#lifting-expressions
// Bu seems like very terse documentation ;(
// Also see, https://users.scala-lang.org/t/solved-dotty-macros-lifting-expressions-how-to-set-this-up/6424

import scala.quoted.*

enum Exp:
  case Num(n: Int)
  case Plus(e1: Exp, e2: Exp)
  case Var(x: String)
  case Let(x: String, e: Exp, in: Exp)

import Exp.*

object compile:
  def apply(e: Exp, env: Map[String, Expr[Int]])(using Quotes): Expr[Int] =
    e match
      case Num(n) =>
        Expr(n)
      case Plus(e1, e2) =>
        '{ ${ compile(e1, env) } + ${ compile(e2, env) } }
      case Var(x) =>
        env(x)
      case Let(x, e, body) =>
        '{ val y = ${ compile(e, env) }; ${ compile(body, env + (x -> 'y)) } }

@main def main: Unit =
  val exp    = Plus(Plus(Num(2), Var("x")), Num(4))
  val letExp = Let("x", Num(3), exp)

// TODO how to actually use this?

//   compile(letExp, Map())
//   '{ val y = 3; (2 + y) + 4 }
