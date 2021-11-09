package scala3macrostuff.reflection

class Basic extends munit.FunSuite:
  test("reflect-basic") {
    assertEquals(basicRefl(2), 2)
    assertEquals(basicRefl(3 + 30) * 3, 99)
  }
