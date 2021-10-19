package scala3macrostuff.showb


class TestShowB extends munit.FunSuite:
  test("show") {
    case class Baz(n: Int, s: String) derives ShowB

    val b = Baz(1, "dos")
    
    assertEquals(b.show, """Baz(n = 1, s = "dos")""")
  }
