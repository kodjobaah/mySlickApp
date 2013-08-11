package com.valtech.actors.services

import play.api.cache.Cache
import play.api.Play.current

import models.{Coffee, Supplier}
import com.valtech.utils.CacheVariables



class CoffeesService {

   def getFieldsToUpdate(coffee: Coffee): List[String] = {
    val coffeesSuppliers: Tuple2[List[Coffee], List[Supplier]] = Cache.getOrElse[Tuple2[List[Coffee], List[Supplier]]](CacheVariables.coffees) {
      val coffees = Seq(Coffee("", 0, 0, 0, 0, 0))
      val suppliers = Seq(Supplier(0, "", "", "", "", ""))
      (coffees.toList, suppliers.toList)
    }
    val coffees: List[Coffee] = coffeesSuppliers._1
    val prevCoffee: Coffee = coffees.filter(_.name == coffee.name)(0)

    val differences = getDifference(coffee, prevCoffee)
    differences
  }


  private def getDifference(updateCoffee: Coffee, prevCoffee: Coffee): List[String] = {

    var differences: List[String] = Seq().toList
    if (prevCoffee.name != updateCoffee.name) {
      differences = differences :+ "name"
    }

    if (prevCoffee.price != updateCoffee.price) {
      differences = differences :+ "price"
    }

    if (prevCoffee.sales != updateCoffee.sales) {
      differences = differences :+ "sales"
    }

    if (prevCoffee.supID != updateCoffee.supID) {
      differences = differences :+ "supID"
    }

    if (prevCoffee.total != updateCoffee.total) {
      differences = differences :+ "total"
    }

    if (prevCoffee.version != updateCoffee.version) {
      differences = differences :+ "version"
    }

    differences.toList

  }

}

object CoffeesService {

  def apply() = new CoffeesService()
    
}
