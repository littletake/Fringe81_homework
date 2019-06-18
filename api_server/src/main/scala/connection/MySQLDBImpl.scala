// このモジュールでDBとコードをつなげている
package connection

// このモジュールがdbとの接続で重要
import slick.driver.MySQLDriver

// ここで変数dbを作成。この変数は以降extendsされて用いられる。
trait MySQLDBImpl{
  val driver = MySQLDriver
  import driver.api._
  val db: Database = MySqlDB.connectionPool
}

private[connection] object MySqlDB{
  import slick.driver.MySQLDriver.api._
  val connectionPool = Database.forConfig("mysql") // ここでの"mysql"はDBの設定ファイル(application.conf)の名前
}
