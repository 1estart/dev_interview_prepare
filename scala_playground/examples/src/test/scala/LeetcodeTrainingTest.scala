import org.scalatest.funsuite.AnyFunSuite

class LeetcodeTrainingTest extends AnyFunSuite {
  test("adding works correct") {
    val res = LeetcodeTraining.add(1, 2)
    assertResult(expected = 3)(actual = res)
  }

  // test maximum product of three numbers
  // посмотреть хорошо краевые случаи
  test("maximum product of three numbers: only three numbers in list") {
    assertResult(expected = 3)(actual =
      LeetcodeTraining.maximumProduct(List(1, 1, 3))
    )
  }

  test(
    "maximum product of three numbers: only three numbers in list with negative"
  ) {
    assertResult(expected = -300)(actual =
      LeetcodeTraining.maximumProduct(List(1, -100, 3))
    )
  }

  test(
    "maximum product of three numbers: simple more than three elements test"
  ) {
    assertResult(
      expected = 4
    )(
      actual = LeetcodeTraining.maximumProduct(List(2, 2, 1, 1, 1, 1))
    )
  }

  test(
    "maximum product of three numbersL: max numbers include negative numbers"
  ) {
    assertResult(
      expected = 10
    )(
      actual = LeetcodeTraining.maximumProduct(List(-5, 2, 5, 1, 1, 1, 1, 1))
    )
  }

  test("maximum product of three numbers: two of three max is negative") {
    assertResult(
      expected = 39200
    )(
      actual = LeetcodeTraining.maximumProduct(List(-100, -98, -1, 2, 3, 4))
    )
  }

  // element more than quater times
  test("element more than quater times: small array with one elem") {
    assertResult(
      expected = 1
    )(
      LeetcodeTraining.elementMoreThanQuater(Array(1))
    )
  }

  test("element more than quater times: small array with two") {
    assertResult(
      expected = 1
    )(
      LeetcodeTraining.elementMoreThanQuater(Array(1, 1))
    )
  }

  test("element more than quater times: zssmall array with three") {
    assertResult(
      expected = 1
    )(
      LeetcodeTraining.elementMoreThanQuater(Array(1, 1, 1))
    )
  }

  test("element more than quater times: array with five five") {
    assertResult(
      expected = 5
    )(
      LeetcodeTraining.elementMoreThanQuater(Array(1, 5, 5, 5, 5, 6))
    )
  }

  test("element more than quater times: seven elements") {
    assertResult(
      expected = 5
    )(
      actual =
        LeetcodeTraining.elementMoreThanQuater(Array(1, 2, 3, 4, 5, 5, 5))
    )
  }

  // number of perfect squares
  test("number of perfect squares: simple minimal tests") {
    assertResult(
      3
    )(
      actual = LeetcodeTraining.numSquares(12)
    )
  }

  test("number of perfect squares: simple minimal tests with 13") {
    assertResult(
      2
    )(
      actual = LeetcodeTraining.numSquares(13)
    )
  }

  // target indices
  test("target indices: empty result case") {
    assertResult(
      expected = List.empty
    )(
      actual = LeetcodeTraining.targetIndices(Array(1, 2, 3, 4), 5)
    )
  }

  test("target indices: non-empty list result case") {
    assertResult(
      expected = List(1, 2)
    )(
      actual = LeetcodeTraining.targetIndices(Array(1, 2, 3, 2), 2)
    )
  }

  test("sortable integers: three elements array") {
    assertResult(
      expected = 3
    )(
      actual = LeetcodeTraining.sortableIntegersNonFunc(Array(3, 1, 2))
    )
  }

  test("sortable integers: three elements array with 0 sum") {
    assertResult(
      expected = 0
    )(
      actual = LeetcodeTraining.sortableIntegersNonFunc(Array(7, 6, 5))
    )
  }

}
