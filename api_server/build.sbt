scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.10"
    val slickVersion = "3.1.1"
  Seq(
    "com.typesafe.akka"  %% "akka-http-experimental"            % akkaV
  , "com.typesafe.akka"  %% "akka-http-spray-json-experimental" % akkaV
	, "com.typesafe.slick" %% "slick"                             % slickVersion
	, "com.typesafe.slick" % "slick-hikaricp_2.11" % slickVersion
	, "mysql" % "mysql-connector-java" % "8.0.11"
	, "org.slf4j" % "slf4j-nop" % "1.6.4"
  )
}
