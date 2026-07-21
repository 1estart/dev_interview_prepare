package forex

import cats.effect.*
import com.dimafeng.testcontainers.KafkaContainer
import fs2.kafka.*
import munit.CatsEffectSuite
import org.testcontainers.utility.DockerImageName

import scala.concurrent.duration.*

class ForexMatchingTest extends CatsEffectSuite:

  override def munitTimeout: Duration = 120.seconds

  test("should match a forex buy order and produce to matches topic") {
    
    val kafkaContainer = KafkaContainer(DockerImageName.parse("apache/kafka:3.8.0"))

    kafkaContainer.start()

    val bootstrapServers = kafkaContainer.bootstrapServers

    val consumerSettings = ConsumerSettings[IO, String, String]
      .withAutoOffsetReset(AutoOffsetReset.Earliest)
      .withGroupId("test-matching-engine")
      .withBootstrapServers(bootstrapServers)

    val producerSettings = ProducerSettings[IO, String, String]
      .withBootstrapServers(bootstrapServers)

    val test = for
      // 1.Launch engine for matching
      engineFiber <- ForexMatching.run[IO](bootstrapServers).start

      // 2. Time for consumer init and subscribe to topic
      _ <- IO.sleep(3.seconds)

      // 3. Publish order
      _ <- KafkaProducer.resource[IO, String, String](producerSettings).use { producer =>
        val pr = ProducerRecord("forex-orders", "order-1", "order-1,EUR/USD,1000.0,true")
        producer.produce(ProducerRecords.one(pr)).void
      }

      // 4. Read match
      matchResult <- KafkaConsumer.resource[IO, String, String](consumerSettings).use { consumer =>
        consumer.subscribeTo("forex-matches") >>
        consumer.stream
          .take(1)
          .map(_.record.value)
          .compile
          .toList
          .timeout(5.seconds)
      }

      // 5. Stop engine
      _ <- engineFiber.cancel

      // 6. Check result
      _ <- IO(assert(matchResult.head.contains("MATCHED BUY 1000.0 EUR/USD")))
    yield ()

    test.guarantee(IO(kafkaContainer.stop()))
  }
