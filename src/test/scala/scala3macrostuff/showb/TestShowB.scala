package scala3macrostuff.showb

enum Foo derives ShowB:
  case One
  case Two
  case Three(s: String, i: Int)
  
case class Baz(n: Int, s: String) derives ShowB
// The `derives` effect would basically be as if doing the following:
// case class Baz(n: Int, s: String)
// object Baz:
//   given ShowB[Baz] = ShowB.derived


class TestShowB extends munit.FunSuite:
  test("show") {

    val b = Baz(1, "dos")
    assertEquals(b.show, """Baz(n = 1, s = "dos")""")

    assertEquals(Foo.One.show, """One()""")
    assertEquals(Foo.Three("K", 2).show, """Three(s = "K", i = 2)""")
  }
