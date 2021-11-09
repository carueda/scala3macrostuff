package scala3macrostuff.record

// If using `Record` as name:
// "... shadows outer reference to object Record in package java.lang"

class SRecord(elems: (String, Any)*) extends Selectable:
  private val fields = elems.toMap
  def selectDynamic(name: String): Any = fields(name)

type Person = SRecord {
  val name: String
  val age: Int
}

@main def hello: Unit =
    val person = SRecord(
    "name" -> "Emma",
    "age" -> 42
    ).asInstanceOf[Person]

    val n: String = person.name
    val a: Int = person.age
    println(s"${person.name} is ${person.age} years old.")
