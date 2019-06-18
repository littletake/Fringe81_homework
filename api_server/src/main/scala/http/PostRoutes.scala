package http

import java.util.UUID.randomUUID

import scala.util.{Failure, Success}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.MethodDirectives
import dao.PostDao
import model.{ErrorModel, OnePostModel, PostModel}
// concurrentはとりあえず非同期処理やスレッド回りに関連する連中
import scala.concurrent.ExecutionContextExecutor

trait PostRoutes extends SprayJsonSupport {
  this: PostDao =>

  implicit val dispatcher: ExecutionContextExecutor

  val routes = pathPrefix("posts") {
    // 投稿のルーティング
    pathEndOrSingleSlash {
      get {
        complete(getAll)
      }
    } ~
      pathPrefix("create") {
        post {
          entity(as[OnePostModel]) {
            post =>
              val id = randomUUID.toString // これは投稿のID
              complete {
                createPost(post, id).map{
                  result =>
                    println(result)
                    if(result == 400){ //あとはこれをjsonに変換できればいい。
                      HttpResponse(400, entity = "{'result': 'NG', 'message': '該当するユーザーIDが登録されていません'}\n")
                    } else if(result == 401) {
                      HttpResponse(400, entity = "{'result': 'NG', 'message': '適切な文字列を入力してください'}\n")
                    } else if(result == 402){
                      HttpResponse(400, entity = "{'result': 'NG', 'message': '入力したユーザーIDと文字列を適切なものに変更してください'}\n")
                    } else {
                      HttpResponse(200, entity = "{'result': 'OK'}\n")
                    }
                }
              }
          }
        }
      } ~
      //  投稿へのコメントのルーティング
      // 返り値が空かどうかで分岐させて表示する物を変えたかったがうまくいかなかった。
      // 2回関数を読んだり、getを２回行ったりしたが、いずれも２回目の処理が優先されて２回分処理されることがなかった。
      pathPrefix(".+".r) { id =>
        println(id)
        pathPrefix("comments") {
          pathEndOrSingleSlash {
            get {
              complete {
                getCommentById(id)
// 試行した部分。
//                getCommentById(id).map {
//                  result =>
//                    if (result.isEmpty) {
//                      println("empty")
//                      HttpResponse(400, entity = "{'NG', '該当IDなし')\n")
//                    } else {
//                      println("not empty")
//                      HttpResponse(200)
//                    }
//                }
              }
            }
          } ~
          pathPrefix("create") {
            post {
              entity(as[OnePostModel]) {
                post =>
                  complete {
                    val parent_post_id = id.toString
                    createComment(post, parent_post_id).map {
                      result =>
                        println(result)
                        if(result == 401){
                          HttpResponse(400, entity = "{'result': 'NG', 'message': '該当する投稿IDが登録されていません'}\n")
                        } else if(result == 402){
                          HttpResponse(400, entity = "{'result': 'NG', 'message': '該当するユーザーIDが登録されていません'}\n")
                        } else if(result == 403){
                          HttpResponse(400, entity = "{'result': 'NG', 'message': '適切な文字列を入力してください'}\n")
                        } else if(result == 404){
                          HttpResponse(400, entity = "{'result': 'NG', 'message': '入力した投稿ID、ユーザーID、コメントの文字数のいずれか2つに問題があります。'}\n")
                        }  else {
                          HttpResponse(StatusCodes.OK, entity = "{'result': 'OK'}\n")
                        }
                    }
                  }
              }
            }
          } ~
            pathPrefix("search") { //試験用
              get {
                println("search")
                complete {
                  getById(id)
                }
              }
            }
        }
      }
  }
}