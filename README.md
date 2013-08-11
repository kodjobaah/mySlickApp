mySlickApp
==========

This application is a sandbox for learning the different aspects of play:

It currently utilizes the following:
 mysql, oracle, slick and typesafe management console.

Setup
======
Database: Myql or Oracle
application.conf as well as specifying the database properties..you
will need to specify which one to use by setting the following property:
mySlickApp.database = "oracle" 


Optimistic Lock using Play, Slick and Akka
==========

Actors: OptimisticLockSupervisor, UpdateCoffeesAndRelationsActor
Contollers: ActorController

The following example demonstrates how actors can be used to implement an 
optimistic lock strategy.

The design consists of a supervisor actor called OptimisticLockSupervisor. 
This actor is responsible for creating a child actor called 
UpdateCoffeesAndRelationsActor.  Within this actor slick is used to get the 
latest version from the Coffees table and then increment the value and then 
store this new value in a table called CoffeeVersions as well as updating the 
Coffees table.  The version column in the tables CoffeeVersions and Coffees 
table have to be unique. 

The number of instances of the UpdateCoffeesAndRelationsActor will be set to a 
value of 6. This will increase the probability of one of the database 
operations failing. If this occurs then within the supervisor the 
UpdateCofeesAndRelationsActor will be restarted.

After starting play..type the 
http://localhost:9000/actorIntegration






