sealed trait Expr
case class Num(value: Double) extends Expr
case class Var(name: String) extends Expr
case class Add(left: Expr, right: Expr) extends Expr
case class Mul(left: Expr, right: Expr) extends Expr
case class Div(left: Expr, right: Expr) extends Expr

object Expr:
    def eval(expr: Expr, env: Map[String, Double]): Either[String, Double] = {
        expr match {
            case Num(value) => Right(value)
            case Var(name) => env.get(name).toRight(s"Unknown variable: $name")
            case Add(left, right) => for {
                leftValue <- eval(left, env)
                rightValue <- eval(right, env)
            } yield leftValue + rightValue
            case Mul(left, right) => for {
                leftValue <- eval(left, env)
                rightValue <- eval(right, env)
            } yield leftValue * rightValue
            case Div(left, right) => for {
                leftValue <- eval(left, env)
                rightValue <- eval(right, env).filterOrElse(_ != 0, "Division by zero")
            } yield leftValue / rightValue
        }
    }