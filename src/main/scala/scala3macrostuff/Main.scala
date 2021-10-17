// https://docs.scala-lang.org/scala3/guides/macros/compiletime.html

package scala3macrostuff

@main def hello: Unit =
  val b = true
  doSomething(true)
  doSomething(false)
  // doSomething(b) // -> Mode must be a known value

  println(inspect(sys.env.get("foo")))
  println(power(2.0, 10))
