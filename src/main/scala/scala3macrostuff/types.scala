// https://eed3si9n.com/intro-to-scala-3-macros

package scala3macrostuff

import scala.compiletime.{error, codeOf}
import scala.quoted.Expr
import scala.quoted.Quotes
import scala.quoted.Type
import scala.quoted.Varargs
import scala.quoted.quotes
import scala.annotation.compileTimeOnly

inline def isCaseClass[A]: Boolean = ${ isCaseClassImpl[A] }

private def isCaseClassImpl[A: Type](using q: Quotes): Expr[Boolean] =
  import q.reflect.*
  val sym = TypeRepr.of[A].typeSymbol
  Expr(sym.isClassDef && sym.flags.is(Flags.Case))

inline def showTree[A](inline a: A, struct: Boolean): String = ${
  showTreeImpl[A]('{ a }, 'struct)
}

private def showTreeImpl[A: Type](a: Expr[A], struct: Expr[Boolean])(using
    Quotes
): Expr[String] =
  import quotes.reflect.*
  Expr(
    if struct.value.get then Printer.TreeStructure.show(a.asTerm)
    else a.asTerm.show
  )

inline def addOne(inline x: Int): Int = ${ addOneImpl('{ x }) }

private def addOneImpl(x: Expr[Int])(using Quotes): Expr[Int] =
  Expr(x.valueOrError + 1)

object Dummy:
  @compileTimeOnly("echo can only be used in lines macro")
  def echo(line: String): String = ???
end Dummy

object Source:
  inline def line: Int = ${ lineImpl() }
  private def lineImpl()(using Quotes): Expr[Int] =
    import quotes.reflect.*
    val pos = Position.ofMacroExpansion
    Expr(pos.startLine + 1)

  // see object Dummy
  inline def lines(inline xs: List[String]): List[String] = ${
    linesv2Impl('{ xs })
  }

  private def linesv2Impl(xs: Expr[List[String]])(using Quotes): Expr[List[String]] =
    import quotes.reflect.*
    xs match
      case '{ List[String]($vargs*) } =>
        vargs match
          case Varargs(args) =>
            val args2 = args map { arg =>
              arg match
                case '{ Dummy.echo($str) } =>
                  val pos = arg.asTerm.pos
                  Expr(s"${pos.startLine + 1}: ${str.valueOrError}")
                case _ => arg
            }
            '{ List(${ Varargs[String](args2.toList) }: _*) }
end Source

inline def addOneList(inline x: Int): List[Int] = ${ addOneListImpl('{ x }) }

private def addOneListImpl(x: Expr[Int])(using Quotes): Expr[List[Int]] =
  val inner = Expr(x.valueOrError + 1)
  '{ List($inner) }

inline def addOneToString(inline x: Int): String = ${
  addOneToStringImpl('{ x })
}

private def addOneToStringImpl(x: Expr[Int])(using Quotes): Expr[String] =
  import quotes.reflect.*
  val inner = Literal(IntConstant(x.valueOrError + 1))
  Select.unique(inner, "toString").appliedToNone.asExprOf[String]

inline def paramInfo[A]: List[String] = ${ paramInfoImpl[A] }

private def paramInfoImpl[A: Type](using Quotes): Expr[List[String]] =
  import quotes.reflect.*
  val tpe = TypeRepr.of[A]
  val targs = tpe.widenTermRefByName.dealias match
    case AppliedType(_, args) => args
    case _                    => Nil
  Expr(targs.map(_.show))
