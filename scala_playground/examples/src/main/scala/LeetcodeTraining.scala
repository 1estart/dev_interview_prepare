import scala.math.max
import scala.annotation.tailrec
import scala.compiletime.ops.any
import cats.syntax.all
import scala.collection.MapOps
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

  def countDaysTogether(
      arriveAlice: String,
      leaveAlice: String,
      arriveBob: String,
      leaveBob: String
  ): Int = {
    case class MDDate(m: Int, d: Int) {
      def in(range: MDRange): Boolean = {
        val moreThanFrom = {
          if (range.from.m > this.m) then false
          else if (range.from.m < this.m) then true
          else { range.from.d <= this.d }
        }
        val lessThanTo = {
          if (this.m > range.to.m) then false
          else if (this.m < range.to.m) then true
          else { this.d <= range.to.d }
        }
        moreThanFrom && lessThanTo
      }
    }
    object MDDate {
      def fromString(s: String): MDDate = {
        val Array(m, d) = s.split("-")
        MDDate(m.toInt, d.toInt)
      }
    }
    case class MDRange(from: MDDate, to: MDDate)

    val rangeAlice =
      MDRange(MDDate.fromString(arriveAlice), MDDate.fromString(leaveAlice))
    val rangeBob =
      MDRange(MDDate.fromString(arriveBob), MDDate.fromString(leaveBob))

    List(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31).zipWithIndex
      .map((e, i) => (e, i + 1))
      .flatMap { (numberOfDays, month) =>
        (1 to numberOfDays).map(MDDate(month, _))
      }
      .count { d =>
        d.in(rangeAlice) && d.in(rangeBob)
      }
  }

  def closedIsland(grid: Array[Array[Int]]): Int = {
    import scala.collection.mutable.Map
    case class Island(number: Int, closed: Boolean)
    case class Point(row: Int, column: Int)
    var number: Int = 0
    val rows = grid.size
    val cols = grid(0).size
    var m: Map[Point, Island] = Map.empty
    def findAnotherPartsOfIsland(p: Point, numberOfIsland: Int): Unit = {
      if (!m.contains(p)) {
        val isIsland = grid(p.row)(p.column) == 0
        val isPartOfOpenIsland =
          p.row == 0 || p.row == rows - 1 || p.column == 0 || p.column == cols - 1

        m += p -> Island(numberOfIsland, !isPartOfOpenIsland)
        val left = p.copy(column = p.column - 1)
        val top = p.copy(row = p.row - 1)
        val right = p.copy(column = p.column + 1)
        val bottom = p.copy(row = p.row + 1)
        List(left, top, right, bottom)
          .withFilter(p =>
            p.row >= 0 && p.row < rows && p.column >= 0 && p.column < cols
          )
          .withFilter(p => grid(p.row)(p.column) == 0)
          .foreach(findAnotherPartsOfIsland(_, numberOfIsland = numberOfIsland))
      }
    }

    (0 until rows)
      .flatMap(row => (0 until cols).map((row, _)))
      .foreach { (row, column) =>
        val p = Point(row, column)
        if (grid(p.row)(p.column) == 0 && !m.contains(p)) {
          findAnotherPartsOfIsland(p, numberOfIsland = number)
          number += 1
        }
      }
    m.values
      .groupBy(_.number)
      .filter { (_, islands) =>
        islands.forall(_.closed)
      }
      .size
  }
