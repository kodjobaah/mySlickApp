package controllers

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.concurrent.future

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.number
import play.api.data.Forms.of
import play.api.data.Forms.text
import play.api.data.format.Formats.doubleFormat
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.db.slick.DBAction
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.pattern.AskTimeoutException
import akka.util.Timeout

import models.Coffee
import com.valtech.actors.supervisors.{DatabaseReaderSupervisor, OptimisticLockSupervisor, children}
import DatabaseReaderSupervisor.ReadCoffeesAndSuppliers
import OptimisticLockSupervisor._
import children._
import FetchCoffeesAndSuppliers.CoffeesAndSuppliers
import UpdateCoffeesAndRelationsActor.UpdateResults
import com.valtech.actors.services._

object ActorController extends Controller {

  val system = ActorSystem("optimistic-lock-supervisor-system")
  
  //NOTE: Should we just be passing one database access service..or should each actor get a copy of their own
  val optimisticLockSupervisor = system.actorOf(OptimisticLockSupervisor.props(AccessDatabaseService()), "optimistic-lock-supervisor")
  val databaseReaderSupervisor = system.actorOf(DatabaseReaderSupervisor.props(AccessDatabaseService()), "database-reader-supervisor")

  //Used by ?(ask)
  implicit val timeout = Timeout(1 seconds)

  val coffeeForm = Form(
    mapping(
      "name" -> text,
      "supID" -> number,
      "price" -> of[Double],
      "sales" -> number,
      "total" -> number,
      "version" -> number)(Coffee.apply)(Coffee.unapply))

  def performOperation = Action { implicit request =>
    var coffee = coffeeForm.bindFromRequest.get

    val response: Future[Any] = ask(optimisticLockSupervisor, UpdateCoffee(coffee)).mapTo[Any]
    Async {
      //Updating the tables
      var results = response.flatMap(
    		   {
    		     case UpdateResults(results) => future(results)
    		     case ProblemsWithUpdate => future("Something Went Wrong")
    		   }
    		  
    		  ).recover {
    		  	case _: AskTimeoutException => future(Ok(views.html.timeout("TIME OUT")))
      		  }

      //Fetching the new updated tables
      val secondResponse: Future[CoffeesAndSuppliers] = ask(databaseReaderSupervisor, ReadCoffeesAndSuppliers).mapTo[CoffeesAndSuppliers]
      secondResponse flatMap {
        case CoffeesAndSuppliers(allSupplierCoffees) => {
          val result = results map { message =>  message}
          
          val actualResult = result.value.getOrElse("No Message").toString()
          
          actualResult match {
            case "Something Went Wrong" => future(Ok("Something went wrong"))
           // case "No Message" => future(Ok("No Message"))
            case message: String =>{
              println("********************** DISPLAYING PAY")
              future(Ok(views.html.updateCoffees(actualResult, coffeeForm, allSupplierCoffees._1, allSupplierCoffees._2, allSupplierCoffees._3)).withHeaders(CACHE_CONTROL -> "no-cache"))
            }
          }
          
          
        }
      }

    } }

  def getCoffees = Action {

    val response: Future[CoffeesAndSuppliers] = ask(databaseReaderSupervisor, ReadCoffeesAndSuppliers).mapTo[CoffeesAndSuppliers]
    Async {
      response.flatMap({
        case CoffeesAndSuppliers(allSupplierCoffees) => {
          future(Ok(views.html.updateCoffees("None", coffeeForm, allSupplierCoffees._1, allSupplierCoffees._2, allSupplierCoffees._3))
            .withHeaders(CACHE_CONTROL -> "no-cache"))
        }
      }) recover {
       case _: AskTimeoutException => Ok("TIME OUT") 
      }
      
    }

  }
 
}
