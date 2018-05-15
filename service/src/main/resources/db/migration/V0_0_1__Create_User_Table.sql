create table user (
  id int auto_increment primary key,
  name varchar(255) not null,
  email varchar (255) not null,
  city varchar (255),
  password varchar (255) not null
);