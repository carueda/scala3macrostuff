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

case class CC(d: Int, b: Baz) derives ShowB

// would be nice to handle this pattern in a general
// way; but it doesn't appear to be possible in scala3.
case class DD(d: Int, s: { val y: Int }) derives ShowB

// always have to "predefine" the type
type Person = SRecord {
  val name: String
  val age: Int
}
case class MyCfg(
    x: Int,
    r: Person
) derives ShowB

// Ideally:
case class MyCfg2(
    x: Int,
    server: {
      val host: String
      val post: Int
    }
) //  derives ShowB
//  ^ making this derivation generic seems super tricky, if at all possible.

class TestShowB extends munit.FunSuite:
  test("show") {
    scribe.warn("1.show = " + 1.show)
    scribe.warn("'hola'.show = " + "hola".show)

    assertEquals(Baz(1, "dos").show, """Baz(n = 1, s = "dos")""")

    assertEquals(Foo.One.show, """One""")
    assertEquals(Foo.Three("K", 2).show, """Three(s = "K", i = 2)""")

    val cc = CC(11, Baz(2, "tres"))
    scribe.warn("cc.show = " + cc.show)

    val dd = DD(33, new Object { val y: Int = 44 })
    scribe.warn("dd = " + dd)
    scribe.warn("dd = " + dd.s.getClass.getName)
    scribe.warn("dd.show = " + dd.show)

    val cfg = MyCfg(
      x = 0,
      // far from ideal:
      r = SRecord(
        "name" -> "nam",
        "age"  -> 100
      ).asInstanceOf[Person]
    )
    scribe.warn("cfg.show = " + cfg.show)

  }
