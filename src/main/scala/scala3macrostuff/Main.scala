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

  case class CC(n: Int)
  class RC(n: Int)
  println(s" isCaseClass[CC] = ${isCaseClass[CC]}")
  println(s" isCaseClass[RC] = ${isCaseClass[RC]}")
  println(s" isCaseClass[Int] = ${isCaseClass[Int]}")
  println("\nno struct: " + showTree(List(1).map(x => x + 1), struct = false))
  println("\nwith struct: " + showTree(List(1).map(x => x + 1), struct = true))
  println("addOne(7) = " + addOne(7))
  println("Source.line = " + Source.line)
  println("addOneList(7) = " + addOneList(7))
  println("addOneToString(7) = " + addOneToString(7))
  println("paramInfo[List[Int]] = " + paramInfo[List[Int]])
  println("paramInfo[Int] = " + paramInfo[Int])

  // format: off
  println(Source.lines(List(
    "foo", 
    Dummy.echo("bar"))))
  // format: on
