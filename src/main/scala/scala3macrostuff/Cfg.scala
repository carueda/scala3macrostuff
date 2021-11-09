package scala3macrostuff

import scala.quoted._
import scala.compiletime.error
import scala.deriving.Mirror

inline def Cfg[A]: String = ${ impl[A] }

private def impl[A: Type](using Quotes): Expr[String] =
  import quotes.reflect.*
  given Printer[Tree] = Printer.TreeStructure

  val typ = Type.of[A]
  scribe.warn(s"typ = $typ")

  val typRepr = TypeRepr.of[A]
  scribe.warn(s"typRepr = $typRepr")
  val sym = typRepr.typeSymbol
  scribe.warn(s"sym = $sym")

  val isCaseClass = sym.isClassDef && sym.flags.is(Flags.Case)
  if !isCaseClass then scribe.warn("not a case class")

  Expr(TypeRepr.of[A].show)
