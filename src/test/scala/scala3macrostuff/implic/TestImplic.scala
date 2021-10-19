// https://blog.shangjiaming.com/scala3-implicit/

package scala3macrostuff.implic

trait Show[A] {
  extension (v: A) {
    def show: String
  }
}

given intShow: Show[Int] with {
  extension (v: Int) {
    def show: String = v.toString()
  }
}

given strShow: Show[String] with {
  extension (v: String) {
    def show: String = s""""$v""""
  }
}

given listShow[A: Show]: Show[List[A]] with {
  extension (v: List[A]) {
    def show: String = v.map(_.show).mkString("[", ";", "]")
  }
}

class TestImplic extends munit.FunSuite:
  test("Implic") {
    assertEquals(1.show, "1")
    assertEquals("x".show, """"x"""")
    assertEquals(List(1, 2, 3).show, "[1;2;3]")
    assertEquals(List("x").show, """["x"]""")
  }
