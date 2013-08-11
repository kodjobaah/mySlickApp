# suppliers schema

# --- !Ups

CREATE SEQUENCE coffees_version_seq;


create table suppliers (
       	     sup_id number PRIMARY KEY,
	     sup_name VARCHAR(255),
	     street  VARCHAR(255),
	     city    VARCHAR(255),
	     state   VARCHAR(255),
	     zip VARCHAR(255));
INSERT INTO suppliers(sup_id,sup_name,street, city, state,zip) 
       	    values (101, 'Acme, Inc.', '99 Market Street', 'Groundsville', 'CA', '95199');
INSERT INTO suppliers(sup_id,sup_name,street, city, state,zip) 
       	    values (49, 'Superior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460');
INSERT INTO suppliers(sup_id,sup_name,street, city, state,zip) 
       	    values (150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966');


create table coffees (
       	     cof_name varchar(255) PRIMARY KEY,
	     sup_id number,
	     price decimal,
	     sales number,
	     total number,
	     version number,
	     constraint version_unique UNIQUE (version)
	     );
	     
	    
INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ('Columbian', 101, 7.99, 0, 0,coffees_version_seq.nextval);
INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ('French_Roast', 49, 8.99, 0,0, coffees_version_seq.nextval);
INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ('Espresso',150, 9.99, 0, 0,coffees_version_seq.nextval);
INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ('Colombian_Decaf',101, 8.99, 0, 0,coffees_version_seq.nextval);
INSERT INTO coffees (cof_name, sup_id, price, sales, total,version) 
       	    values ('French_Roast_Decaf',49, 9.99, 0, 0,coffees_version_seq.nextval);

create table coffeeversions (
  version_id number PRIMARY KEY,
  coffee_name  varchar(255),
  CONSTRAINT coffee_fk FOREIGN KEY (coffee_name) REFERENCES coffees(cof_name)
  );
  

# --- !Downs
drop table suppliers;
drop table coffeeversions;
drop table coffees;
DROP SEQUENCE coffees_version_seq;

