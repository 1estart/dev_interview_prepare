import org.scalatest.funsuite.AnyFunSuite

class LeetcodeTrainingTest extends AnyFunSuite {
  test("adding works correct") {
    val res = LeetcodeTraining.add(1, 2)
    assertResult(expected = 3)(actual = res)
  }
  // test maximum product of three numbers
  // посмотреть хорошо краевые случаи
  test("only three numbers in list") {
    assertResult(expected = 3)(actual =
      LeetcodeTraining.maximumProduct(List(1, 1, 3))
    )
  }

  test("only three numbers in list with negative") {
    assertResult(expected = -300)(actual =
      LeetcodeTraining.maximumProduct(List(1, -100, 3))
    )
  }

  test("simple more than three elements test") {
    assertResult(
      expected = 4
    )(
      actual = LeetcodeTraining.maximumProduct(List(2, 2, 1, 1, 1, 1))
    )
  }

  test("max numbers include negative numbers") {
    assertResult(
      expected = 10
    )(
      actual = LeetcodeTraining.maximumProduct(List(-5, 2, 5, 1, 1, 1, 1, 1))
    )
  }

  test("two of three max is negative") {
    assertResult(
      expected = 39200
    )(
      actual = LeetcodeTraining.maximumProduct(List(-100, -98, -1, 2, 3, 4))
    )
  }

  // element more than quater times
  test("small array with one elem") {
    assertResult(
      expected = 1
    )(
      LeetcodeTraining.elementMoreThanQuater(Array(1))
    )
  }

  test("small array with two") {
    assertResult(
      expected = 1
    )(
      LeetcodeTraining.elementMoreThanQuater(Array(1, 1))
    )
  }

  test("small array with three") {
    assertResult(
      expected = 1
    )(
      LeetcodeTraining.elementMoreThanQuater(Array(1, 1, 1))
    )
  }

  test("array with five five") {
    assertResult(
      expected = 5
    )(
      LeetcodeTraining.elementMoreThanQuater(Array(1, 5, 5, 5, 5, 6))
    )
  }

  test("seven elements") {
    assertResult(
      expected = 5
    )(
      actual =
        LeetcodeTraining.elementMoreThanQuater(Array(1, 2, 3, 4, 5, 5, 5))
    )
  }

  // number of perfect squares
  test("simple minimal tests") {
    assertResult(
      3
    )(
      actual = LeetcodeTraining.numSquares(12)
    )
  }

  test("simple minimal tests with 13") {
    assertResult(
      2
    )(
      actual = LeetcodeTraining.numSquares(13)
    )
  }

}
