CREATE TABLE venue (
  id int AUTO_INCREMENT PRIMARY KEY,
  user_id int NOT NULL,
  fs_id varchar(255) NOT NULL,
  added_at datetime NOT NULL,
  name varchar(255),
  address varchar(255),
  phone varchar(255),
  FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE category (
   id int AUTO_INCREMENT PRIMARY KEY,
   fs_id varchar(255) UNIQUE,
   name varchar(255)
);

CREATE TABLE venue_category (
    id int AUTO_INCREMENT PRIMARY KEY,
    venue_id int NOT NULL,
    category_id int NOT NULL,
    FOREIGN KEY (venue_id) REFERENCES venue(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
)