create table users (
  id                    bigserial,
  username              varchar(500) not null,
  password              varchar(500) not null,
  email                 varchar(100) unique,
  primary key (id)
);
create table roles (
  id                    serial,
  name                  varchar(50) not null,
  primary key (id)
);


insert into users (username, password, email)
values
('ReViZe', '$2a$10$U0s0KEZPxvLus8Oc0VjE0eIMe9pT1k0rVCaDUKoa2n70Zhca.mMLK', 'mr.revize.com@gmail.com'),
('firstUser', '$2a$10$xgi/uS2HWAckQPaX9GJRXeBiQTpVt6HBRKctutAHi8tiy2Doipycm', 'vasyapupkin@mail.ru');
insert into roles (name) values ('ROLE_USER'), ('ROLE_ADMIN'), ('DELETE_USERS_PERMISSION');


CREATE TABLE users_roles (
  user_id               bigint not null,
  role_id               integer not null,
  primary key (user_id, role_id),
  foreign key (user_id) references users (id),
  foreign key (role_id) references roles (id)
);
insert into users_roles (user_id, role_id) values (1,2), (2,1);


create table clothes (id bigserial primary key, title varchar(500), sex varchar(1), description varchar(5000), price integer);
create table category (id bigserial primary key, name varchar(500));


insert into clothes (title, sex, description, price)
values
('Кроссовки', 'М', 'Санкционные бренда Abibas', 5000),
('Джинсы', 'Ж', 'Вываренные самим Ашотом с Черкизона', 2000),
('Футболка', 'Ж', 'Размера XXL с принтом: "Владимир Путин - молодец"', 1300),
('Шапка', 'М', 'Ушанка с эмблемой ВС РФ', 500),
('Носки', 'М', 'б/у, протёртые на пятке', 50),
('Трусы', 'Ж', 'Семейные от фабрики "Озорная комсомолка"', 300);
insert into category (name) values ('Обувь'), ('Штаны'), ('Нижнее бельё'), ('Головные уборы'), ('Носки'), ('Футболки и майки');


create table clothes_category (
clothes_id              bigint not null,
category_id             bigint not null,
foreign key (clothes_id) references clothes(id),
foreign key (category_id) references category(id)
);
insert into clothes_category (clothes_id, category_id)
values
(1, 1), (2, 2), (3, 6), (4, 4), (5, 5), (6, 3);

create table users_clothes (
users_id                bigint,
clothes_id              bigint,
foreign key (users_id) references users(id),
foreign key (clothes_id) references clothes(id)
);