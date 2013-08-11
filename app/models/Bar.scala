package models

//import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.Ref
import com.typesafe.slick.driver.oracle.OracleDriver.simple.Table
case class Bar(id: Option[Int] = None, name: String) 


object Bars extends Table[Bar]("BAR") {
   def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
   
   // This the primary key column
   def name = column[String]("name")

   //Every table needs a * projection with the same type as the table's type parameter
   def * = id.? ~ name <>(Bar, Bar.unapply _)

}
