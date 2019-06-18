package dao

import java.util.Date
import java.util.UUID.randomUUID

import connection.MySQLDBImpl
import model.{OnePostModel, PostModel, PostTable}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait PostDao extends PostTable with MySQLDBImpl {

  import driver.api._

  protected val postTableQuery = TableQuery[PostTable]

  val userIDSet = Set("11111111-1111-1111-1111-111111111111", "22222222-2222-2222-2222-222222222222", "33333333-3333-3333-3333-333333333333")
  var postIDSetDao = Set.empty[String]

  def getAll: Future[List[PostModel]] = db.run {
    postTableQuery.to[List].result
  }

  def createPost(post: OnePostModel, id: String): Future[Int] = {
    var userIDFlag = 0
    var textFlag = 0 //正常かどうか判断するための指針

    if (userIDSet.contains(post.user_id)) {
      userIDFlag = 1
    }
    if(post.text.length > 0 & post.text.length < 101){
      textFlag = 1
    }

    if(userIDFlag == 1 & textFlag == 1){
      val date = "%tF %<tT" format new Date
      postIDSetDao += id
      db.run {
        postTableQuery.map(
          p =>
            (p.id, p.user_id, p.text, p.parent_post_id,
              p.comment_count, p.posted_at)) += (id, post.user_id, post.text, "", 1, date)
      }
    } else if(userIDFlag == 0 & textFlag == 1) {
      val f: Future[Int] = Future {
        println("ユーザーは存在しません\n")
        400
      }
      f
    } else if(userIDFlag == 1 & textFlag == 0){
      val f: Future[Int] = Future {
        println("適切な文字列を入力してください\n")
        401
      }
      f
    } else {
      val f: Future[Int] = Future {
        println("該当するユーザーは存在しません。また、適切な文字列を入力してください\n")
        402
      }
      f
    }
  }

  // つぎはコメントの関数作る。
  def getCommentById(parent_post_id: String): Future[Option[PostModel]] = {
    //    if(postIDSet.contains(parent_post_id)){
    db.run(postTableQuery.filter(_.id === parent_post_id).result.headOption)
    //    } else {
    //      val f: Future[Option[PostModel]] = {
    //      }
    //    }
  }

  def createComment(post: OnePostModel, parent_post_id: String): Future[Int] = {
    var userIDFlag: Int = 0
    var postIDFlag: Int = 0
    var textFlag: Int = 0
    if (userIDSet.contains(post.user_id)) {
      userIDFlag = 1
    }
    if (postIDSetDao.contains((parent_post_id))) {
      postIDFlag = 1
    }
    if(post.text.length > 0 & post.text.length < 101){
      textFlag = 1
    }

    if (userIDFlag == 1 & postIDFlag == 1 & textFlag == 1) {
      println(postIDSetDao)
      val date = "%tF %<tT" format new Date
      val id = randomUUID.toString // これは投稿のID
      db.run {
        postTableQuery.map(
          p =>
            (p.id, p.user_id, p.text, p.parent_post_id,
              p.comment_count, p.posted_at)) += (id, post.user_id, post.text, parent_post_id, 2, date)
      }
    } else if (userIDFlag == 1 & postIDFlag == 0 & textFlag == 1) {
      val f: Future[Int] = Future {
        println("該当する投稿がありません\n")
        401
      }
      f
    } else if (userIDFlag == 0 & postIDFlag == 1 & textFlag == 1) {
      val f: Future[Int] = Future {
        println("ユーザーは存在しません\n")
        402
      }
      f
    }else if(userIDFlag == 1 & postIDFlag == 1 & textFlag == 0){
      val f: Future[Int] = Future {
        println("適切な文字列を入力してください\n")
        403
      }
      f
    } else {
      val f: Future[Int] = Future {
        println("入力した投稿ID、ユーザー、コメントの文字数のいずれか2つに問題があります。\n")
        404
      }
      f
    }
  }

  // テスト用
   def getById(id: String): Future[Option[PostModel]] = db.run{
     postTableQuery.filter(_.user_id === id).result.headOption
   }

  def ddl = db.run{
    postTableQuery.schema.create
  }
}
