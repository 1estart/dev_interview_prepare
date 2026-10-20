import org.scalatest.funsuite.AnyFunSuite

class MathUtilsTest extends AnyFunSuite {

  test("Сложение двух чисел работает корректно") {
    val result = 2 + 2
    assert(result === 4)
  }

  test("Умножение на ноль дает ноль") {
    val result = 5 * 0
    assert(result === 0)
  }
}
