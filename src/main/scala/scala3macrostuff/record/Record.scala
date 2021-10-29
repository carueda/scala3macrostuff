package scala3macrostuff.record

class Record(elems: (String, Any)*) extends Selectable:
  private val fields = elems.toMap
  def selectDynamic(name: String): Any = fields(name)

type Person = Record {
  val name: String
  val age: Int
}

@main def hello: Unit =
    val person = Record(
    "name" -> "Emma",
    "age" -> 42
    ).asInstanceOf[Person]

    val n: String = person.name
    val a: Int = person.age
    println(s"${person.name} is ${person.age} years old.")
