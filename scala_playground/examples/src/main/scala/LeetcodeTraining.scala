import scala.math.max
import scala.annotation.tailrec
object LeetcodeTraining:
  def add(a: Int, b: Int): Int = a + b

  def maximumProduct(nums: List[Int]): Int = {
    val sortedNumbers = nums.toVector.sorted
    max(
      sortedNumbers.takeRight(3).product,
      sortedNumbers.take(2).product * sortedNumbers.last
    )
  }

  def elementMoreThanQuater(arr: Array[Int]): Int = {
    arr
      .groupMapReduce(identity)(_ => 1)(_ + _)
      .toArray
      .sortBy(_._2)
      .reverse
      .head
      ._1
  }

  def numSquares(n: Int): Int = {
    import scala.collection.mutable.Map
    import scala.math.min
    var solutions: Map[Int, Int] = Map(1 -> 1)
    def findPerfectSquares(number: Int): Int = {
      solutions.get(number) match
        case Some(numSquares) => numSquares
        case None             => {
          var squareMut: Int = 1
          var minSquares = number
          while (squareMut * squareMut <= number) {
            val reminder = number - squareMut * squareMut
            if (reminder == 0) {
              minSquares = 1
            }
            minSquares = min(minSquares, 1 + findPerfectSquares(reminder))
            squareMut += 1
          }
          solutions.put(number, minSquares)
          minSquares
        }
    }
    findPerfectSquares(n)
  }

  def targetIndices(nums: Array[Int], target: Int): List[Int] = {
    val sorted = nums.sorted
    val firstIndex = sorted.indexOf(target)
    if (firstIndex != -1) then {
      (firstIndex to sorted.lastIndexOf(target)).toList
    } else List.empty
  }
