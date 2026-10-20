import io.getquill._
import java.net.{URI, HttpURLConnection}
import scala.io.Source

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

  {
    val rpcUrl = "https://ethereum-rpc.publicnode.com"
    // val rpcUrl = "https://rpc.ankr.com/eth"
    // val rpcUrl = "https://cloudflare-eth.com"

    val requestBody =
      """{"jsonrpc":"2.0","method":"eth_blockNumber","params":[],"id":1}"""

    val url = URI.create(rpcUrl).toURL
    val conn = url.openConnection().asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("POST")
    conn.setRequestProperty("Content-Type", "application/json")
    conn.setRequestProperty("User-Agent", "Mozilla/5.0")
    conn.setDoOutput(true)
    conn.getOutputStream.write(requestBody.getBytes("UTF-8"))

    val response = Source.fromInputStream(conn.getInputStream).mkString
    println(s"Raw response: $response")

    val json = ujson.read(response)
    val blockNumberHex = json("result").str
    val blockNumber = java.lang.Long.parseLong(blockNumberHex.drop(2), 16)

    println(s"Current Ethereum block: $blockNumber")
  }
}
