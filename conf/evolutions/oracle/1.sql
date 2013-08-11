# Bars schema

# --- !Ups
create table bar ( 
    id number PRIMARY KEY,
    name varchar2(40)
);


# --- !Downs

drop table bar;
