// テスト用
// DBのためのjsonをつくっている
// まずは投稿時刻が含まれるようにする。

//package model
//
//import connection.MySQLDBImpl
//import spray.json.DefaultJsonProtocol
//
//case class DogModel(id: String, owner_id: String, name: String, posted_at: String)
//// 全表示する際に用いる。
//case class DogList(dogs: List[DogModel])
//case class PostModel(owner_id: String, name: String)
//
//
//trait DogTable extends DefaultJsonProtocol{
//  this: MySQLDBImpl =>
//
//  import driver.api._
//// implicit: 勝手に（暗黙的に）動作する
//// lazy: 遅延評価（一度しか起動しない）
//  implicit lazy val dogFormat = jsonFormat4(DogModel)
//  implicit lazy val postFormat = jsonFormat2(PostModel)
//  implicit lazy val dogListFormat = jsonFormat1(DogList)
//
//  class DogTable(tag: Tag) extends Table[DogModel](tag, "dog"){
//    def id = column[String]("ID")
//    def owner_id = column[String]("OWNER_ID")
//    def name = column[String]("NAME")
//    def posted_at = column[String]("POSTED_AT")
//    def * = (id, owner_id, name, posted_at) <>(DogModel.tupled, DogModel.unapply)
//  }
//}
