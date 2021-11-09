package scala3macrostuff.reflection

class ForType extends munit.FunSuite:
  test("reflect-for-type") {
    case class Foo(n: Int, s: String)

    val foo = Foo(42, "foo")
    assertEquals(typeRefl(foo), "pending")
  }
