# Bars schema

# --- !Ups
create table bar( 
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL
) ;


# --- !Downs

drop table bar;
