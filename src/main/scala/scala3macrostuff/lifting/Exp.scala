package scala3macrostuff.lifting

// https://docs.scala-lang.org/scala3/reference/metaprogramming/macros.html#lifting-expressions
// Bu seems like very terse documentation ;(
// Also see, https://users.scala-lang.org/t/solved-dotty-macros-lifting-expressions-how-to-set-this-up/6424

import scala.quoted.*
import scala.compiletime.{error, codeOf}

enum Exp:
  case Num(n: Int)
  case Plus(e1: Exp, e2: Exp)
  case Var(x: String)
  case Let(x: String, e: Exp, in: Exp)

import Exp.*

inline def compileExp(inline e: Exp): Int = ${
  applyImpl('e)
}

def applyImpl(e: Expr[Exp])(using Quotes): Expr[Int] =
  def rec(e: Expr[Exp], env: Map[String, Int]): Expr[Int] =
    e match
      case '{ Num($n) } =>
        n

      case '{ Plus($e1, $e2) } =>
        '{ ${ rec(e1, env) } + ${ rec(e2, env) } }

      case '{ Var($x) } =>
        Expr(env(x.valueOrError))

      case '{ Let($x, $e, $body) } =>
        val y    = rec(e, env)
        val env2 = env + (x.valueOrError -> y.valueOrError)
        rec(body, env2)

  rec(e, Map())

inline def assertExp(inline expected: Int, inline compiled: Int): Unit =
  if expected != compiled then
    error(
      "oops: expected=" + codeOf(expected) + " != compiled=" + codeOf(compiled)
    )
