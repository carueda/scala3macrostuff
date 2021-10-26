package scala3macrostuff.typeinfo


class TestTypeInfo extends munit.FunSuite:
  test("NonEmpty") {
    case class NonEmpty[T](e: T, tail: Option[NonEmpty[T]])

    val ti: String = TypeInfo[NonEmpty]
    
    assertEquals(ti, """NonEmpty(e: T, tail: Option[NonEmpty[T]])""")
  }
