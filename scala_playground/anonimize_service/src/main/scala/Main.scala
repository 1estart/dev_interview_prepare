object Main extends App {
  println("Hello, world!")
  /*
  

Реализуй функцию, которая из списка целочисленных диапазонов [from, to]
 с включенными границами строит список объединенных диапазонов,
  и возвращает результат в формате [from, length].

Пример:
[[1, 4], [4, 5], [5, 11], [22, 33], [44, 55]]

[[1, 10], [22, 11], [44, 11]]
  */

  case class FromTo(from: Int, to: Int)
  case class FromLength(from: Int, length: Int)

  def process(seq: Seq[FromTo]): Seq[FromLength] = {
    if (seq.isEmpty) {
      return Seq()
    }
    val sortedSeq = seq.sortBy(_.from)
    sortedSeq.foldLeft(Seq(FromLength(sortedSeq.head.from, sortedSeq.head.to - sortedSeq.head.from))) {
      (acc, fromTo) => {
        val rightBound = acc.last.from + acc.last.length
        if (acc.last.from <= fromTo.to && fromTo.to <= rightBound) {
          // full include by previous element
          acc
        }
        else if (fromTo.from <= rightBound) {
          // include 
          acc.init :+ acc.last.copy(from = acc.last.from, length = fromTo.to - acc.last.from)
        } else  {
          acc :+ FromLength(fromTo.from, fromTo.to - fromTo.from) 
        }
      }
    }
  }

  /* 
  Никита Зайцев
Реализуй функцию, которая из списка целочисленных диапазонов [from, to] с включенными границами строит список объединенных диапазонов, и возвращает результат в формате [from, length].

Пример:
[[1, 4], [4, 5], [5, 11], [22, 33], [44, 55]]

[[1, 10], [22, 10], [44, 10]]
Daniil
[[5, 5]]
Daniil
[[1, 10], [3, 5]]
Daniil
[[1, 2], [3, 4]]
Daniil
[[-5, -3], [-2, 1]]
Daniil
[] */

  val initialSeqs = Seq(
    Seq(FromTo(22,33), FromTo(1,4), FromTo(5,11), FromTo(44,55), FromTo(4,5)),
    Seq(FromTo(3,5), FromTo(1,2), FromTo(4,5)),
    Seq(),
    Seq(FromTo(1,2), FromTo(3,4)),
    Seq(FromTo(-5, -3), FromTo(-2, 1)),
    Seq(FromTo(5, 5)),
    Seq(FromTo(1,10), FromTo(3,5)),
   Seq(FromTo(1,4), FromTo(4,5), FromTo(5,11), FromTo(22,33), FromTo(44,55)),
    Seq(FromTo(1, 10), FromTo(1, 5))

    )

  println(initialSeqs.mkString("\n"))
  println()
  println(initialSeqs.map(process).mkString("\n"))
  

}


import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

final case class RawRecord(id: Long, value: String)
final case class EnrichedRecord(id: Long, value: String, segment: String)
final case class PublishResult(id: Long, offset: Long)

trait BlockingSegmentClient {
def lookup(id: Long): String // JDBC
}

trait Publisher {
def publish(record: EnrichedRecord): Future[PublishResult]
}

object Process {
def process(
input: List[Any],
client: BlockingSegmentClient,
publisher: Publisher
): Future[List[PublishResult]] = {

val records: List[RawRecord] = input match {
case values: List[String] => values.map(parseString)
case values: List[Int] => values.map(parseInt)
case _ => Nil
}

val work: Future[List[PublishResult]] =
Future.traverse(records) { record =>
Future(client.lookup(record.id))
.map(segment => EnrichedRecord(record.id, record.value, segment))
.flatMap(publisher.publish)
}.map { published =>
published.foldLeft(PublishReport(0L, Map.empty, Nil)) { (report, item) =>
val maxOffset = math.max(
report.maxOffsetByPartition.getOrElse(item.partition, Long.MinValue),
item.offset
)

report.copy(
successful = report.successful + 1,
maxOffsetByPartition =
report.maxOffsetByPartition.updated(item.partition, maxOffset),
publishedIds = report.publishedIds :+ item.id
)
}

work


}
}
}