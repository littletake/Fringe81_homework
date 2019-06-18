 // DBのためのjsonをつくっている
 package model

 import connection.MySQLDBImpl
 import spray.json.DefaultJsonProtocol

 case class PostModel(id: String,
                      user_id: String,
                      text: String,
                      parent_post_id: String,
                      comment_count: Int,
                      posted_at: String)

 // 全表示する際に用いる。
 case class PostList(post: List[PostModel])

 case class OnePostModel(user_id: String, text: String)


 trait PostTable extends DefaultJsonProtocol{
   this: MySQLDBImpl =>

   import driver.api._
   // implicit: 勝手に（暗黙的に）動作する
   // lazy: 遅延評価（一度しか起動しない）
   implicit lazy val postFormat = jsonFormat6(PostModel)
   implicit lazy val postListFormat = jsonFormat1(PostList)
   implicit lazy val onePostFormat = jsonFormat2(OnePostModel)

   class PostTable(tag: Tag) extends Table[PostModel](tag, "POST"){
     def id = column[String]("ID")
     def user_id = column[String]("USER_ID")
     def text = column[String]("TEXT")
     def parent_post_id = column[String]("PARENT_POST_ID")
     def comment_count = column[Int]("COMMENT_COUNT")
     def posted_at = column[String]("POSTED_AT")
     def * = (id, user_id, text, parent_post_id, comment_count, posted_at) <>(PostModel.tupled, PostModel.unapply)
   }
 }
