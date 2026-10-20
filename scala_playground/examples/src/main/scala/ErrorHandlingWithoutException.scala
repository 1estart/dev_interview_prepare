import cats.syntax.flatMap
object ErrorHandlingWithoutException:
  enum Option[+A]:
    case Some(get: A)
    case None

    def map[B](f: A => B): Option[B] = {
      this.flatMap(get => Some(f(get)))
    }
    def flatMap[B](f: A => Option[B]): Option[B] = {
      this match
        case Some(get) => f(get)
        case None      => None
    }
    def getOrElse[B >: A](default: => B): B = {
      this match
        case Some(get) => get
        case None      => default
    }
    def orElse[B >: A](ob: => Option[B]): Option[B] = {
      this match
        case Some(get) => Some(get)
        case None      => ob
    }
    def filter(f: A => Boolean): Option[A] = {
      this match
        case Some(get) if f(get) => Some(get)
        case _                   => None
    }

  import Option.{Some, None}
  def mean(xs: Seq[Double]): Option[Double] =
    if xs.isEmpty then None
    else Some(xs.sum / xs.length)

  def variance(xs: Seq[Double]): Option[Double] = {
    for {
      m <- mean(xs)
      squares = xs.map(x => math.pow(x - m, 2))
      v <- mean(squares)
    } yield v
  }
