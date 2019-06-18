// akka-http テスト用

// import akka.actor.ActorSystem
// import akka.http.scaladsl.Http
// import akka.http.scaladsl.model._
// import akka.http.scaladsl.server.Directives._
// import akka.stream.ActorMaterializer
// import scala.io.StdIn
//
// object Sample {
//   def main(args: Array[String]){
//
//     implicit val system = ActorSystem("my-system")
//     implicit val materializer = ActorMaterializer()
//
//     implicit val executionContext = system.dispatcher
//
//     val route =
//       path("hello"){
//         get{
//           complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
//         }
//       }
//
//     val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
//
//     println("Server online at http://localhost:8080/\nPress RETURN to stop...")
//     StdIn.readLine()
//     bindingFuture
//       .flatMap(_.unbind()) // trigger unbinding from the port
//       .onComplete(_ => system.terminate())
//   }
// }
