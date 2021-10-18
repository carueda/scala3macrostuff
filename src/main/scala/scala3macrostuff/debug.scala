// https://blog.softwaremill.com/starting-with-scala-3-macros-a-short-tutorial-88e9d2b2584c

package scala3macrostuff

import scala.compiletime.{error, codeOf}
import scala.quoted.Expr
import scala.quoted.Quotes
import scala.quoted.Varargs
import scala.quoted.quotes

inline def debugSingle(inline expr: Any): Unit = ${ debugSingleImpl('expr) }

private def debugSingleImpl(expr: Expr[Any])(using Quotes): Expr[Unit] =
  '{ println("Value of " + ${ Expr(expr.show) } + " is " + $expr) }

inline def debug(inline exprs: Any*): Unit = ${ debugImpl('exprs) }

private def debugImpl(exprs: Expr[Seq[Any]])(using q: Quotes): Expr[Unit] =
  import q.reflect._

  def showWithValue(e: Expr[_]): Expr[String] = '{
    ${ Expr(e.show) } + " = " + $e
  }

  val stringExps: Seq[Expr[String]] = exprs match
    case Varargs(es) =>
      es.map { e =>
        e.asTerm match {
          case Literal(c: Constant) => Expr(c.value.toString)
          case _                    => showWithValue(e)
        }
      }
    case e => List(showWithValue(e))

  val concatenatedStringsExp = stringExps
    .reduceOption((e1, e2) => '{ $e1 + ", " + $e2 })
    .getOrElse('{ "" })
  '{ println($concatenatedStringsExp) }
