@main def run(): Unit =
  println("Hello, scala 3 with cats!!!".toUpperCase)

  // MySemigroup
  println(9 |+| 20)
  println("Hello " |+| "World")
  println(Option(4) |+| Option(15))
  println(Option(4) |+| None)

  //  Printable
  42.print
  "Uraboras".print

  Cat("Garfield", 5, "Orange").print
