package models

//import slick.driver.MySQLDriver.simple.Table
import com.typesafe.slick.driver.oracle.OracleDriver.Table
case class Supplier(id: Int, name: String, street: String, city:String, state: String, zip: String)

object Suppliers extends Table[Supplier]("SUPPLIERS"){

  def id = column[Int]("SUP_ID",O.PrimaryKey) // This is the primay key column
  def name = column[String]("SUP_NAME")
  def street = column[String]("STREET")
  def city = column[String]("CITY")
  def state = column[String]("STATE")
  def zip = column[String]("ZIP")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id ~ name ~ street ~ city ~ state ~ zip <> (Supplier.apply _, Supplier.unapply _)
}
