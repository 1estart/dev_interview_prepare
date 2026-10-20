import scala.math.max
import scala.annotation.tailrec
import scala.compiletime.ops.any
import cats.syntax.all
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

  def sortableIntegers(nums: Array[Int]): Int = {
    val n = nums.length
    val nums_vector = nums.toVector
    val sorted = nums_vector.sorted

    def sortedRotation(block: Vector[Int], blockStart: Int): Boolean = {
      val blockSize = block.length
      val expected = sorted.slice(blockStart, blockStart + blockSize)
      (0 until blockSize).exists { shift =>
        block.drop(shift) ++ block.take(shift) == expected
      }
    }

    def isSortable(k: Int): Boolean = {
      nums_vector
        .grouped(k)
        .zipWithIndex
        .forall { case (block, idx) =>
          sortedRotation(block, idx * k)
        }
    }

    (1 to n)
      .withFilter(k => n % k == 0)
      .withFilter(isSortable)
      .map(identity)
      .sum
  }

  def sortableIntegersNonFunc(nums: Array[Int]): Int = {
    val n = nums.length
    val sorted = nums.sorted

    def rotationMatches(
        blockStart: Int,
        blockSize: Int,
        shift: Int
    ): Boolean = {
      var i = 0
      while (i < blockSize) {
        if (
          nums(blockStart + (shift + i) % blockSize) != sorted(blockStart + i)
        ) {
          return false
        }
        i += 1
      }
      true
    }

    def blockSortable(blockStart: Int, blockSize: Int): Boolean = {
      var shift = 0
      while (shift < blockSize) {
        if (rotationMatches(blockStart, blockSize, shift)) {
          return true
        }
        shift += 1
      }
      false
    }

    def isSortable(k: Int): Boolean = {
      var blockStart = 0
      while (blockStart < n) {
        if (!blockSortable(blockStart, k)) {
          return false
        }
        blockStart += k
      }
      true
    }

    var result = 0
    var k = 1
    while (k <= n) {
      if (n % k == 0 && isSortable(k)) {
        result += k
      }
      k += 1
    }
    result
  }
