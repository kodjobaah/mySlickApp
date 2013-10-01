import sbt._
import Keys._
import PlayProject._
//import play.Project._
//import com.typesafe.sbt.SbtAtmos.atmosSettings
//import com.typesafe.sbt.SbtAtmos.traceAkka 
// imports standard command parsing functionality
import complete.DefaultParsers._
object ApplicationBuild extends Build {

  // The command changes the foreground or background terminal color
  //  according to the input.
  lazy val change = Space ~> (reset | setColor)
  lazy val reset = token("reset" ^^^ "\033[0m")
  lazy val color = token(Space ~> ("blue" ^^^ "4" | "green" ^^^ "2"))
  lazy val select = token("fg" ^^^ "3" | "bg" ^^^ "4")
  lazy val setColor = (select ~ color) map { case (g, c) => "\033[" + g + c + "m" }

  def changeColor = Command("color")(_ => change) { (state, ansicode) =>
    print(ansicode)
    state
  }

  val appName = "mySlickApp"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.ostermiller" % "utils" % "1.07.00",
    "com.typesafe.slick" % "slick_2.10" % "1.0.0-RC2",
    "org.scalatest" %% "scalatest" % "2.0.M5b" % "test",
    "com.typesafe.slick" %% "slick-extensions" % "1.0.0",
    "mysql" % "mysql-connector-java" % "5.1.18",
    "com.typesafe.akka" %% "akka-actor" % "2.2.0",
    "com.typesafe.akka" %% "akka-actor-tests" % "2.2.0" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test",
     "com.googlecode.gstreamer-java" % "gstreamer-java" % "1.5",
     "net.java.dev.jna" % "jna" % "3.5.2",
      "org.apache.commons" % "commons-lang3" % "3.0",
      "commons-cli" % "commons-cli" % "1.2",
      "ch.qos.logback" % "logback-core" % "1.0.13",
      "ch.qos.logback" % "logback-classic" % "1.0.13",
      "ch.qos.logback" % "logback-access" % "1.0.13",
       "com.typesafe.play" %% "play-slick" % "0.4.0", 
       "org.webjars" % "jquery" % "1.8.2",
       "org.webjars" % "bootstrap" % "2.1.1",
       "org.webjars" % "webjars-play" % "2.1.0-1",
       "org.apache.commons" % "commons-email" % "1.3.1",
      jdbc,
    anorm)
      //"xuggle" % "xuggle-xuggler" % "5.2",
    
    //import  sbt.Project._
  val sub = play.Project(appName, appVersion, appDependencies).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*);

  val main = play.Project(appName, appVersion, appDependencies).settings(
    testOptions in Test := Nil,
    autoScalaLibrary := false,

    resolvers += "Java CV" at "http://maven2.javacv.googlecode.com/git",
    
    libraryDependencies ++= Dependencies.traceAkka,

    scalacOptions += "-language:postfixOps",
    javaOptions in run ++= Seq(
      "-javaagent:c:\\software\\typesafe\\typesafe-console-developer-1.2.0\\lib\\weaver\\aspectjweaver.jar",
      "-Dorg.aspectj.tracing.factory=default",
      "-Djna.library.path=C:\\pract\\play\\mySlickApp\\lib",
      "-Djava.library.path=c:\\software\\typesafe\\typesafe-console-developer-1.2.0\\lib\\sigar"),
    Keys.fork in run := true // Add your own project settings here      
	    //connectInput in run := true
    ).aggregate(sub)

  object Dependencies {

    object V {
      val Akka = "2.1.4"
      val Atmos = "1.2.0"
      val Logback = "1.0.7"
    }

    //val atmosTrace = "com.typesafe.atmos" % "trace-akka-2.2.0_2.11.0-M3" % "1.2.0-M6"
    val atmosTrace = "com.typesafe.atmos" % "trace-akka-2.2.0_2.10" % "1.2.0"
    //val atmosTrace = "com.typesafe.atmos" % "trace-akka-2.2.0_2.10" % "1.2.0"	  
    //	 val atmosTrace ="com.typesafe.atmos" % "trace-akka-2.1.4" % "1.2.0"

    val logback = "ch.qos.logback" % "logback-classic" % V.Logback

    val traceAkka = Seq(atmosTrace, logback)
  }
}
