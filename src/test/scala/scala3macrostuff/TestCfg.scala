package scala3macrostuff

case class Foo(n: Int)

class TestCfg extends munit.FunSuite:
  test("basic") {
    // val _ = Cfg[Int]
    val res = Cfg[Foo]
    println(s"res = $res")
    assertEquals(res.toString, "scala3macrostuff.Foo")
  }
