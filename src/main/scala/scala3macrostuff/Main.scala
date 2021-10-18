// https://docs.scala-lang.org/scala3/guides/macros/compiletime.html

package scala3macrostuff

@main def hello: Unit =
  val b = true
  doSomething(true)
  doSomething(false)
  // doSomething(b) // -> Mode must be a known value

  println(inspect(sys.env.get("foo")))
  println(power(2.0, 10))

  val (x, y, z) = (21, 34, 55)
  debugSingle(x)        // Value of x is 21
  debugSingle(x + y)    // Value of x.+(y) is 55
  debug("A", x, x + y)  // A, x = 21, x.+(y) = 55
  debug("A", x, "B", y) // A, x = 21, B, y = 34
  debug(x, y, z)        // x = 21, y = 34, z = 55
