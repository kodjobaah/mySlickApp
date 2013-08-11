package models

//import slick.driver.MySQLDriver.simple.Table
import com.typesafe.slick.driver.oracle.OracleDriver.Table
import scala.slick.lifted.PrimaryKey

case class Coffee(name: String, supID: Int, price: Double, sales:Int, total:Int, version: Int)

object Coffees extends Table[Coffee]("COFFEES") {
	def name = column[String]("COF_NAME",O.PrimaryKey)
	def supID = column[Int]("SUP_ID")
	def price = column[Double]("PRICE")
	def sales = column[Int]("SALES")
	def total = column[Int]("TOTAL")
	def version = column[Int]("VERSION",O.AutoInc)
	def * = name ~ supID ~ price ~ sales ~ total ~ version <> (Coffee.apply _, Coffee.unapply _)
	// A reified foreign key relation that can be navigate to create a join
	def supplier = foreignKey("sup_fk", supID, Suppliers)(_.id)
}