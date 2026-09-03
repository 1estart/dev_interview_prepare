import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Await
import scala.concurrent.duration._
import cats.effect.kernel.Par
import munit.Assertions.assertEquals
import scala.annotation.tailrec
import cats.effect.IOApp
import cats.effect.IO
import cats.implicits._

object IOExample extends IOApp.Simple {
  /*
  println("Hello, scala 3 with cats!!!".toUpperCase)

  // MySemigroup
  println(9 |+| 20)
  println("Hello " |+| "World")
  println(Option(4) |+| Option(15))
  println(Option(4) |+| None)

  //  Printable
  42.print
  "Uraborasu".print

  Cat("Garfield", 5, "Orange").print

  // Graphs
  val graph: Graph = Map(
    1 -> List(2, 3),
    2 -> List(1, 4),
    3 -> List(1, 5),
    4 -> List(2),
    5 -> List(3)
  )

  GraphAlgorithms.printGraph(graph)

  GraphAlgorithms.dfs(graph, 1)
  
  GraphAlgorithms.bfs(graph, List(1), Set.empty)
  
  import sttp.client4.quick.*

  println(quickRequest.get(uri"http://httpbin.org/ip").send())


  val env = Map("population" -> 1000000.0)
  val expr = Div(Mul(Add(Num(1), Num(2)), Add(Num(3), Num(4))), Var("population"));
  println(Expr.eval(expr, env))

  val tree = Branch(Leaf("a"), Branch(Leaf("b"), Leaf("c")))
  val stateComputation = State.numberLeaves(tree)

  val (resultTree, finalState) = stateComputation.run(0)
  println(resultTree)
  println(finalState)


  println("Future")

  val f = Future { 2 }

   f.transform {
    case Success(value) => Success(2)
    case Failure(exception) => Success(1)
   }

   val res = Await.result(f, 2.seconds)

   println(res)

  
  println("Hi")
  println(if true then "true" else "false")

   */

  /*
      Распарсить строки в структуру Transaction(userId: String, amount: Double, currency: String, timestamp: String)
    Отфильтровать транзакции с отрицательной или нулевой суммой
    Сгруппировать валидные транзакции по валюте
    Для каждой валюты посчитать общую сумму
    Вернуть результат как Map[String, Either[String, Double]], где:
        Ключ — валюта
        Значение — Right(totalAmount) если все транзакции валидны, или Left(errorMessage) если были ошибки парсинга для этой валюты
        

Ограничения:

    Только функциональный стиль (никаких var, while, мутабельных коллекций)
    Использовать Option, Either, map, flatMap, fold где уместно
    Обработать все edge cases (пустые строки, null, неправильный формат)

Пиши код, думай вслух (можешь комментировать свои решения), а потом обсудим:

    Что можно улучшить
    Какие edge cases ты учел/не учел
    Теорию вокруг использованных конструкций



  val rawData: List[String] = List(
  "user1;100.50;USD;2024-01-01",
  "user2;invalid;EUR;2024-01-02",  // невалидная сумма
  "user3;-50.00;USD;2024-01-03",   // отрицательная сумма
  "user1;200.00;USD;2024-01-04",
  "malformed_string",              // невалидный формат
  "user2;75.25;EUR;2024-01-05",
  "user3;300.00;EUR;2024-01-06"
)

  sealed trait ParseError
  case object InvalidFormat extends ParseError

  case class Transaction(userId: String, amount: Double, currency: String, timestamp: String)
  
  object Transaction {
    def fromString(s: String): Either[ParseError, Transaction] = {
      s.split(';') match
        case Array(userId, amount, currency, timestamp) =>
          validateAmount(amount).flatMap( validatedAmount =>
            Right(Transaction(userId, validatedAmount, currency, timestamp))
          )

        case _ =>  Left(InvalidFormat)
    }

    private def validateAmount(amount: String): Either[ParseError, Double] = ???
  }

  def totalAmountInTxByCurrency(tx: List[String]): Map[String, Either[String, Double]] = {
    tx
      .map(Transaction.fromString)
      .groupMapReduce()


    Map.empty
  }

  // tests
  assert(totalAmountInTxByCurrency(List.empty) == Map.empty)
  assert(totalAmountInTxByCurrency(List("malformed_string")) == Map.empty)
  // assert(totalAmountInTxByCurrency(List("user2;invalid;EUR;2024-01-02")) == Map("EUR" -> Left("error_parsing")))

   */

  // val a = List(Some(2)).flatten

  // val futures = List(
  //   Future { 2 },
  //   Future { 3 }
  //   // Future { throw new RuntimeException }
  // )

  // val resFuture = Future.sequence(futures)
  // val result = Await.result(resFuture, 2.seconds)

  // @tailrec
  // def rec(i: Int): Int = {
  //   if i == 1 then 2 else rec(i - 1)
  // }

  // assertEquals(
  //   rec(2),
  //   2
  // )

  // trait A { def foo = "A" }
  // trait B extends A { override def foo = "B" + super.foo }
  // class C extends B with A // порядок важен

  // assertEquals(
  //   (new C).foo,
  //   "BA"
  // )

  // /** 1) Cжать последовательность интов Seq(1, 2, 2, 3, 4, 3, 3, 3) => Seq((1,
  //   * 1), (2, 2), (3, 1), (4, 1), (3, 3)) Ответ выдать в виде Seq[(Int, Int)]
  //   * (число из последовательности и число последовательных повторений) 2)
  //   * восстановаить исходную последовательность из сжатой
  //   */

  // val in = Seq(1, 2, 2, 3, 4, 3, 3, 3)

  // @tailrec
  // def compress(
  //     acc: Seq[(Int, Int)] = Seq.empty,
  //     seq: Seq[Int]
  // ): Seq[(Int, Int)] = {
  //   seq match
  //     case a :: tail => {
  //       acc match {
  //         case head :: tailAcc if a == head._1 =>
  //           compress((a, 1 + head._2) +: tailAcc, tail)
  //         case _ =>
  //           compress((a, 1) +: acc, tail)
  //       }
  //     }
  //     case Nil => acc.reverse
  // }

  // def compress_1(seq: Seq[Int]): Seq[(Int, Int)] = compress(seq = seq)

  // def uncompress(compressed: Seq[(Int, Int)]): Seq[Int] = {
  //   compressed.flatMap { (element, count) =>
  //     Seq.fill(count)(element)
  //   }
  // }

  // assertEquals(
  //   Seq((1, 1), (2, 2), (3, 1), (4, 1), (3, 3)),
  //   compress_1(in)
  // )
  // assertEquals(
  //   in,
  //   uncompress(compress_1(in))
  // )

  /** На вход List[IO[String]] Получить IO[(List[String], List[Throwable]) -
    * результат агрегации выполненых IO и исключений
    */

  val run = {

    //   val talk = List(
    //     IO.sleep(1.second).as("red"),
    //     IO.raiseError(new RuntimeException("exception1")),
    //     IO.pure("blue"),
    //     IO.raiseError(new RuntimeException("exception2")),
    //     IO.pure("green"),
    //     IO.raiseError(new RuntimeException("exception3"))
    //   )

    //   talk
    //     .traverse { io =>
    //       io.map(Right[Throwable, String](_))
    //         .recover { case throwable =>
    //           Left(throwable)
    //         }
    //     }
    //     .map(_.partitionMap {
    //       identity
    //     })
    //     .flatTap {
    //       IO.println(_)
    //     }
    //     .as(())

    // }

//   Написать реализацию Middleware, которая не позволяет
// обрабатывать больше N одновременных вызовов apply.

// Если лимит не достигнут, должен исполняться handler
// Если достигнут, вызов должен падать с какой-нибудь ошибкой
// */

    type Req = Unit
    type Resp = Unit

    trait Middleware {
      def apply(req: Req, handler: Req => IO[Resp]): IO[Resp]
    }

    trait Service extends Middleware {
      private var limit = 4

      def apply(req: Req, handler: Req => IO[Resp]): IO[Resp] = {

        handler(req)
      }
    }

    IO.unit
  }

// получить IO[(List[Throwable], List[String])

  // println("*********************************************************")
  // println("All assertions passed")
  // println("***********************************************************")

}
