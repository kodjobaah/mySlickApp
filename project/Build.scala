import sbt._
import Keys._
import play.Project._
import com.typesafe.sbt.SbtAtmos.atmosSettings
import com.typesafe.sbt.SbtAtmos.traceAkka 

object ApplicationBuild extends Build {


  val appName         = "mySlickApp"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.scalatest" %% "scalatest" % "2.0.M6-SNAP26" % "test",
    "org.scala-lang" % "scala-actors" % "2.11.0-M4" % "test",
    //"com.typesafe.akka" %% "akka-actor" % "2.2.0",
    "com.typesafe.slick" % "slick_2.10" % "1.0.0-RC2",
    "mysql" % "mysql-connector-java" % "5.1.18",
    "org.fluentlenium" % "fluentlenium-core" % "0.6.0",
    "org.fluentlenium" % "fluentlenium-festassert" % "0.6.0",
    jdbc,
    anorm
  )


 //import  sbt.Project._
  val main = play.Project(appName, appVersion, appDependencies).settings(
	testOptions in Test := Nil,
      
	  resolvers += "JBoss repository" at "https://repository.jboss.org/nexus/content/repositories/",
      resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots",
      resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases",
      resolvers += "Maven Repository" at "http://repo1.maven.org/maven2/",
     
      libraryDependencies ++= Dependencies.traceAkka,
     
      scalacOptions += "-language:postfixOps",
       javaOptions in run ++= Seq(
         "-javaagent:c://software//typesafe//typesafe-console-developer-1.2.0//lib/weaver//aspectjweaver.jar",
         "-Dorg.aspectj.tracing.factory=default",
         "-Djava.library.path=c://software//typesafe//typesafe-console-developer-1.2.0//lib/sigar"
       ),
           Keys.fork in run := true 

    // Add your own project settings here      
 
  
  //connectInput in run := true
  )

 object Dependencies {
    
	  object V {
	    val Akka = "2.1.4"
	    val Atmos = "1.2.0"
	    val Logback = "1.0.7"
	  }
	  
	 //val atmosTrace = "com.typesafe.atmos" % "trace-akka-2.2.0_2.11.0-M3" % "1.2.0-M6"
	  
	 val atmosTrace ="com.typesafe.atmos" % "trace-akka-2.1.4" % "1.2.0"


	  val logback = "ch.qos.logback" % "logback-classic" % V.Logback
	  
	  val traceAkka= Seq(atmosTrace,logback)
  }
}
