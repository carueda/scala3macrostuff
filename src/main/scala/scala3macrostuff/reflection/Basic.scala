// https://docs.scala-lang.org/scala3/guides/macros/reflection.html

package scala3macrostuff.reflection

import scala.compiletime.{error, codeOf}
import scala.quoted.Expr
import scala.quoted.Quotes
import scala.quoted.quotes

inline def basicRefl(inline n: Int) = ${ basicReflImpl('n) }

private def basicReflImpl(x: Expr[Int])(using Quotes): Expr[Int] =
  import quotes.reflect.*
  given Printer[Tree] = Printer.TreeStructure

  val tree: Term = x.asTerm
  scribe.warn(s"tree = $tree")
  scribe.warn(s"tree = ${tree.show}")

  // just playing with matches ...
  tree match
    // Inlined(call: Option[Tree /* Term | TypeTree */], bindings: List[Definition], expansion: Term)
    case Inlined(None, Nil, Literal(IntConstant(c))) =>
      // this of course must be our case here.
      scribe.warn(s"IntConstant=$c")

    case Literal(_)   =>
    case Select(_, _) =>
    case _            => throw new MatchError(tree.show)

  val expr: Expr[Int] = tree.asExprOf[Int]
  scribe.warn(s"expr = $expr")
  scribe.warn(s"expr = ${expr.show}")
  expr
