import java.util.UUID.randomUUID

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import dao.PostDao
import model.{PostModel, OnePostModel}
import http.PostRoutes
import scala.io.StdIn

import scala.concurrent.ExecutionContextExecutor

object Server extends App with PostRoutes with PostDao{
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher

  ddl.onComplete{
    _ =>
      val user_1 = "11111111-1111-1111-1111-111111111111"
      val user_2 = "22222222-2222-2222-2222-222222222222"
      val user_3 = "33333333-3333-3333-3333-333333333333"

      val id_1 = randomUUID().toString
      val id_3 = randomUUID().toString
      val id_4 = randomUUID().toString
      
      createPost(OnePostModel(user_1, "hello"), id_1)
      createPost(OnePostModel(user_2, "hi"), id_3)
      createPost(OnePostModel(user_3, "luck"), id_4)

      // Note that bindAndHandle currently does not support HTTP/2, you must use bindAndHandleAsync.
      val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)
      println("Server online at http://localhost:8080/\nPress RETURN to stop...")
      StdIn.readLine()
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
  }
}
