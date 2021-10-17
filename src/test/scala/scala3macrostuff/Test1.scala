package scala3macrostuff

class Test1 extends munit.FunSuite:
  test("inspect") {
    assertEquals(inspect(sys.env.get("foo")), None)
  }

  test("power") {
    assertEquals(power(2.0, 0), 1.0)
    assertEquals(power(2.0, 10), 1024.0)
  }

  test("reflect-basic") {
    assertEquals(refl(2), 2)
  }
