object CatChallenges:
  def identity[A]: A => A = a => a

  def compose[A, B, C](f: A => B)(g: B => C): A => C = { a =>
    g(f(a))
  }

  // idA compose f == f compose idB
  def checkIdRespectsCompose = {
    val f: Int => Double = a => a.toDouble
    val idA: Int => Int = identity[Int]
    val idB: Double => Double = identity[Double]
    compose(idA)(f) == compose(f)(idB)
  }
