package scala3macrostuff.typeinfo

@main def main(): Unit =
  // case class NonEmpty[T](e: T, tail: Option[NonEmpty[T]])
  // println(s"TypeInfo[NonEmpty] = ${TypeInfo[NonEmpty]}")

  case class S(q: Int)
  case class Qux[T](e: T, bla: String, s: S)
  println(s"TypeInfo[Qux] = ${TypeInfo[Qux]}")
