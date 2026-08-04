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

object Publisher {

  def process(
      input: List[Any],
      client: BlockingSegmentClient,
      publisher: Publisher
  ): Future[List[PublishResult]] = {

    val records: List[RawRecord] = input match {
      case values: List[String] => values.map(parseString)
      case values: List[Int]    => values.map(parseInt)
      case _                    => Nil
    }

    val work: Future[List[PublishResult]] =
      Future
        .traverse(records) { record =>
          Future(client.lookup(record.id))
            .map(segment => EnrichedRecord(record.id, record.value, segment))
            .flatMap(publisher.publish)
        }
        .map { published =>
          published.foldLeft(PublishReport(0L, Map.empty, Nil)) {
            (report, item) =>
              val maxOffset = math.max(
                report.maxOffsetByPartition
                  .getOrElse(item.partition, Long.MinValue),
                item.offset
              )

              report.copy(
                successful = report.successful + 1,
                maxOffsetByPartition = report.maxOffsetByPartition
                  .updated(item.partition, maxOffset),
                publishedIds = report.publishedIds :+ item.id
              )
          }

          work
        }

  }
}
