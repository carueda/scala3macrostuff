package scala3macrostuff.typeinfo

// https://medium.com/virtuslab/tasty-way-of-re-writing-macros-in-scala-3-3ce704a2c37c

import scala.quoted.*

object TypeInfo {
  inline def apply[T[_]]: String = ${ typeInfoImpl[T] }

  private def typeInfoImpl[T[_]](using
      qctx: Quotes,
      tpe: Type[T]
  ): Expr[String] = {
    import qctx.reflect.*

    val tpe = TypeRepr.of[T]

    val name = tpe.typeSymbol.name

    def fullTypeName(tpe: TypeRepr): String = tpe match
      case t: NamedType =>
        t.name
      case o: OrType =>
        fullTypeName(o.left) + " | " + fullTypeName(o.right)
      case o: AndType =>
        fullTypeName(o.left) + " & " + fullTypeName(o.right)
      case AppliedType(base, args) =>
        fullTypeName(base) + args.map(fullTypeName).mkString("[", ",", "]")

    val caseFields = tpe.typeSymbol.caseFields.map { s =>
      val name = s.name
      val tpe = s.tree match {
        case v: ValDef =>
          fullTypeName(v.tpt.tpe)
      }
      s"$name: $tpe"
    }

    Expr(
      s"$name(${caseFields.mkString(", ")})"
    )
  }
}
