import io.getquill._

case class Transaction(height: Long, hash: String)

object Indexer extends App {
  // quil mirror example
  {
    val ctx = new SqlMirrorContext(PostgresDialect, SnakeCase)
    import ctx._

    val targetHeight = 125L
    val transactionQuery = quote {
      query[Transaction].filter(_.height == lift(targetHeight))
    }

    val mirror = ctx.run(transactionQuery)

    println("generated SQL:")
    println(mirror.string)

    println(mirror.prepareRow.data)
  }
}
