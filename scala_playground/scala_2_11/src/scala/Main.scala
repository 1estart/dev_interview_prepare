import scala.io.Source
import scala.collection.generic.Sorted

object Main extends App {
  // задача 2
  /*
  val _ = scala.io.StdIn.readLine()

  Iterator
    .continually(scala.io.StdIn.readLine())
    .takeWhile(_ != "")
    .toStream
    .groupBy(_.size)
    .maxBy(_._1)
    ._2
    .toSet
    .toList
    .sorted
    .foreach(println)
   */

  // задача 1
  val lines = Iterator
    .continually(scala.io.StdIn.readLine())
    .takeWhile(s => s != null && s.nonEmpty)
    .toArray

  val n = lines(0).trim.toInt
  val a = lines(1).trim.split("\\s+").map(_.toInt)

  val sum = a.sum
  val target = sum / (n - 1)

  val isPossible = sum % (n - 1) == 0 && a.count(_ == target) >= n - 2

  println(if (isPossible) "YES" else "NO")

}
