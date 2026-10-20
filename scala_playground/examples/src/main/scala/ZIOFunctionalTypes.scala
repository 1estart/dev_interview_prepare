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
