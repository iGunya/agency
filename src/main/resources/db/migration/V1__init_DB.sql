create table buyers
(
    id_buyer int8 generated by default as identity,
    description varchar(255),
    fio varchar(50) not null,
    passport varchar(10) not null UNIQUE,
    phone varchar(255) not null,
    primary key (id_buyer)
);
create table contract_seller
(
    id_contract int8 not null,
    id_seller int8 not null
);
create table contracts
(
    id_contract int8 generated by default as identity,
    date_buyer varchar(255),
    date_seller varchar(255),
    url_contract varchar(255),
    id_buyer int8,
    primary key (id_contract)
);
create table objects
(
    id_object int8 generated by default as identity,
    adress varchar(255),
    count_floor int4 not null,
    count_room int4 not null,
    description varchar(255),
    id_contract int4, price int8,
    real_price int8,
    square varchar(255),
    id_type_move int8,
    id_type_object int8,
    primary key (id_object)
);
create table photos
(
    id_photo int8 generated by default as identity,
    url_photo varchar(255) not null,
    id_object int8,
    primary key (id_photo)
);
create table sellers
(
    id_seller int8 generated by default as identity,
    fio varchar(50) not null,
    passport varchar(10) not null UNIQUE,
    phone varchar(255) not null,
    primary key (id_seller)
);
create table type_moves
(
    id_type_move int8 generated by default as identity,
    type_move varchar(255) not null,
    primary key (id_type_move)
);
create table type_objects
(
    id_type_object int8 generated by default as identity,
    type_object varchar(255) not null,
    primary key (id_type_object)
);
create table user_object
(
    id_user int8 not null,
    id_object int8 not null
);
create table users
(
    id_user int8 generated by default as identity,
    login varchar(25) not null UNIQUE,
    password varchar(255) not null,
    role varchar(255) not null,
    primary key (id_user)
);
alter table
    if exists contract_seller
    add constraint FK_contract_seller
    foreign key (id_seller)
    references sellers;
alter table
    if exists contract_seller
    add constraint FK_seller_contract
    foreign key (id_contract)
    references contracts;
alter table
    if exists contracts
    add constraint FK_contracts
    foreign key (id_buyer)
    references buyers;
alter table
    if exists objects
    add constraint FK_objects_type_move
    foreign key (id_type_move)
    references type_moves;
alter table
    if exists objects
    add constraint FK_objects_type_object
    foreign key (id_type_object)
    references type_objects;
alter table
    if exists photos
    add constraint FK_photos
    foreign key (id_object)
    references objects;
alter table
    if exists user_object
    add constraint FK_user_object
    foreign key (id_object)
    references objects;
alter table
    if exists user_object
    add constraint FK_object_user
    foreign key (id_user)
    references users;