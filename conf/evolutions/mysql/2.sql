# suppliers schema

# --- !Ups
create table suppliers (
       	     sup_id INT NOT NULL PRIMARY KEY,
	     sup_name VARCHAR(255),
	     street  VARCHAR(255),
	     city    VARCHAR(255),
	     state   VARCHAR(255),
	     zip VARCHAR(255)) engine=innodb
;
INSERT INTO suppliers(sup_id,sup_name,street, city, state,zip) 
       	    values (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199");
INSERT INTO suppliers(sup_id,sup_name,street, city, state,zip) 
       	    values (49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460");
INSERT INTO suppliers(sup_id,sup_name,street, city, state,zip) 
       	    values (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966");


create table coffees (
       	     cof_name varchar(255) NOT NULL PRIMARY KEY,
	     sup_id int,
	     price decimal,
	     sales int,
	     total int,
	     version int) engine=innodb
;

INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ("Columbian", 101, 7.99, 0, 0,1);
INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ("French_Roast", 49, 8.99, 0, 0,2);
INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ("Espresso",150, 9.99, 0, 0,3);
INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ("Colombian_Decaf",101, 8.99, 0, 0,4);
INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ("French_Roast_Decaf",49, 9.99, 0, 0,5);


create table coffeeversions (
  version_id int NOT NULL PRIMARY KEY,
  coffee_name  varchar(255)
 )engine=innodb
;

# --- !Downs
drop table suppliers;
drop table coffeeversions;
drop table coffees;
