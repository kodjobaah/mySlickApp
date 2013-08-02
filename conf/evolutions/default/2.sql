# supplier schema

# --- !Ups
create table supplier (
       snum char(2) NOT NULL PRIMARY KEY,
       sname varchar(255) NOT NULL,
       status integer NOT NULL,
       city varchar(255) NOT NULL
);


INSERT INTO supplier (snum, sname, status, city) VALUES('S1','Smith',20,'London');
INSERT INTO supplier (snum, sname, status, city) VALUES('S2','Jones',10,'Paris');
INSERT INTO supplier (snum, sname, status, city) VALUES('S3','Blake',30,'Paris');
INSERT INTO supplier (snum, sname, status, city) VALUES('S4','Clarke',20,'London');
INSERT INTO supplier (snum, sname, status, city) VALUES('S5','Adams',30,'Athens');

# --- !Downs

drop table supplier;
