package forex

import cats.effect.*
import cats.implicits.*
import fs2.kafka.*
import scala.concurrent.duration.*

case class Order(id: String, currencyPair: String, amount: Double, isBuy: Boolean)

object ForexMatching:
  def run[F[_]: Async](bootstrapServers: String): F[Unit] =
    val consumerSettings = ConsumerSettings[F, String, String]
      .withAutoOffsetReset(AutoOffsetReset.Earliest)
      .withGroupId("matching-engine")
      .withBootstrapServers(bootstrapServers)

    val producerSettings = ProducerSettings[F, String, String]
      .withBootstrapServers(bootstrapServers)

    fs2.Stream.resource(
      for
        consumer <- KafkaConsumer.resource(consumerSettings)
        producer <- KafkaProducer.resource(producerSettings)
      yield (consumer, producer)
    ).flatMap { case (consumer, producer) =>
      fs2.Stream.eval(consumer.subscribeTo("forex-orders")) >>
      consumer.stream
        .evalMap { committable =>
          // 1. Get value -  committable.record.value
          val order = parseOrder(committable.record.value)
          val matchResult = processOrder(order)

          // 2. Create ProducerRecord
          val producerRecord = ProducerRecord("forex-matches", order.id, matchResult)

          // 3. Wrap to ProducerRecords.one
          val records = ProducerRecords.one(producerRecord)

          // 4. Send and return offset for commit
          producer.produce(records).as(committable.offset)
        }
        // Commit offsets by 100 messages or by 5 sec
        .through(commitBatchWithin(100, 5.seconds))
    }.compile.drain

  def parseOrder(value: String): Order =
    val parts = value.split(",")
    Order(parts(0), parts(1), parts(2).toDouble, parts(3).toBoolean)

  def processOrder(order: Order): String =
    if order.isBuy then s"MATCHED BUY ${order.amount} ${order.currencyPair}"
    else s"MATCHED SELL ${order.amount} ${order.currencyPair}"
