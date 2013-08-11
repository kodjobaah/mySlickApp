package models

//import slick.driver.MySQLDriver.simple.Table
import com.typesafe.slick.driver.oracle.OracleDriver.Table
import scala.slick.lifted.PrimaryKey

case class CoffeeVersion(version: Int, coffeeName: String)

object CoffeeVersions  extends Table[CoffeeVersion]("COFFEEVERSIONS") {
	def version = column[Int]("VERSION_ID",O.PrimaryKey)
	def coffeeName = column[String]("COFFEE_NAME")
	def * = version ~ coffeeName <> (CoffeeVersion.apply _, CoffeeVersion.unapply _)
	// A reified foreign key relation that can be navigate to create a join
	def supplier = foreignKey("coffee_fk", coffeeName, Coffees)(_.name)
	

}