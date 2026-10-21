import zio._
object ZIOFunctionalTypes:
  case class Stats(views: Long, clicks: Long)

  trait Monoid[A]:
    def empty: A
    def combine(x: A, y: A): A

  val statsMonoid: Monoid[Stats] = new Monoid[Stats]:
    def empty = Stats(0, 0)
    def combine(a: Stats, b: Stats) =
      Stats(a.views + b.views, a.clicks + b.clicks)

  val shards: List[ZIO[Any, Nothing, Stats]] = List(
    ZIO.succeed(Stats(10, 1)),
    ZIO.succeed(Stats(20, 2)),
    ZIO.succeed(Stats(30, 3))
  )

  val total: ZIO[Any, Nothing, Stats] =
    ZIO.collectAllPar(shards).map { results =>
      results.foldLeft(statsMonoid.empty)(statsMonoid.combine)
    }

  object ZIOConcurrencyPrimitives:
    val task: UIO[Int] = ZIO.succeed(42)

    for {
      fiber <- task.fork
      x <- fiber.join
    } yield x

    val getUser: UIO[Int] = ZIO.succeed(1)
    val getOrders: UIO[Int] = ZIO.succeed(2)
    val result: ZIO[Any, Nothing, (Int, Int)] = getUser.zipPar(getOrders)

    // fastSource.race(slowSource)

    // promise
    for {
      promise <- Promise.make[Nothing, Int]

      waiter <- promise.await.fork

      _ <- promise.succeed(42)

      value <- waiter.join
    } yield value

    // ref
    for {
      counter <- Ref.make(0)
      _ <- counter.update(_ + 1)
      value <- counter.get
    } yield value

    // query
    for {
      queue <- Queue.bounded[Int](10)

      producer <- queue.offer(1).fork
      consumer <- queue.take

    } yield consumer

    // semaphore
    for {
      semaphore <- Semaphore.make(3)

      _ <- semaphore.withPermit(task)
    } yield ()

    // pub/sub
    // software transactional memory

    case class NonEmptyList[A](head: A, tail: Option[NonEmptyList[A]]) {
      def reverse: NonEmptyList[A] = {
        def loop(l: NonEmptyList[A], acc: NonEmptyList[A]): NonEmptyList[A] = {
          l.tail match {
            case None    => acc
            case Some(t) => loop(t, NonEmptyList(t.head, Some(acc)))
          }

        }
        loop(this, NonEmptyList(this.head, None))
      }
    }
