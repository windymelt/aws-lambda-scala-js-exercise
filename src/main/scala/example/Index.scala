package example

import com.github.tarao.record4s.%

import scala.concurrent.Await
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Promise
import scala.scalajs.js.annotation._
import scala.util.Failure
import scala.util.Success

// 入出力のデモ

@js.native
trait Event extends js.Object {
  val key1: String
  val key2: String
  val key3: String
}

@js.native
trait Context extends js.Object {
  val functionName: String
}

object Main {
  // indexモジュール内のhandlerとしてexportする必要があるのでここで指定する
  @JSExportTopLevel(name = "handler", moduleID = "index")
  def handler(event: Event, context: Context): Promise[String] = { // you need paren even if you don't need args
    import concurrent.ExecutionContext.Implicits.global
    import js.JSConverters._
    println("Hello world!")

    // event/contextを利用した入出力ができる
    println(s"I am ${context.functionName} !")
    println(s"event is ${event}")
    println(s"event.key1 is ${event.key1}")

    // Scala.jsに対応しているライブラリを使える
    val me = %(
      name = "Windymelt",
      like = %(
        food = "sushi",
        drink = "beer"
      )
    )
    println(s"My favorite beverage is ${me.like.drink} !")

    // もちろん、Scalaの標準ライブラリも利用できる
    val x = (0 to 50).toSeq.map(_ * 2).reduce(_ + _)
    println(s"sum of 0 to 100 is ${x}")

    // npmライブラリを呼べる
    import npm.awsCryptoSha256Js.mod.Sha256

    val s256 = new Sha256()
    s256.update("All your base are belong to us.")
    val digest =
      s256.digest().toFuture.map(_.toSeq.map(_.toString).mkString(" "))
    digest.andThen(d => println(s"SHA256 is $d"))

    // npmライブラリを呼べる 2
    import npm.axios.mod.default as axios
    import npm.axios.mod.AxiosResponse
    val got: Promise[AxiosResponse[String, ?]] =
      axios.get("https://example.com")
    val showResult = got.`then` { (r) =>
      println(r.data)
    }.toFuture

    val all = for {
      _ <- showResult
    } yield "Hello, response!"

    // レスポンスするにはPromiseを返せばよい
    return all.toJSPromise
  }
}
