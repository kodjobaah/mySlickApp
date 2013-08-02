package models

import scala.slick.driver.MySQLDriver.simple._

object Supplier extends Table[(String,String,Int,String)]("supplier") {
	def snum = column[String]("snum")
	def sname = column[String]("sname")
	def status = column[Int]("status")
	def city = column[String]("city")
	def * = snum ~ sname ~ status ~ city
}