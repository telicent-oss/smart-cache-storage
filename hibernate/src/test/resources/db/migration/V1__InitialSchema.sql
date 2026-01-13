--
-- Copyright (C) 2024-2025 Telicent Limited
--

create sequence addresses_seq
    increment by 50;

create sequence line_items_seq
    increment by 50;

create sequence orders_seq
    increment by 50;

create sequence products_seq
    increment by 50;

create table if not exists addresses
(
    id           bigint not null
    primary key,
    city         varchar(255),
    nameornumber varchar(255),
    postalcode   varchar(255),
    recipient    varchar(255),
    street       varchar(255)
    );

create table if not exists orders
(
    billingadressid   bigint
    constraint billingaddress_fk
    references addresses,
    id                bigint       not null
    primary key,
    shippingaddressid bigint
    constraint shippingaddress_fk
    references addresses,
    orderid           varchar(255) not null
    unique
    );

create table if not exists products
(
    price       numeric(38, 2),
    available   bigint,
    id          bigint       not null
    primary key,
    code        varchar(255) not null
    unique,
    description varchar(255),
    name        varchar(255) not null
    );

create table if not exists line_items
(
    quantity   integer,
    id         bigint not null
    primary key,
    items      bigint
    constraint order_fk
    references orders,
    product_id bigint
    constraint product_fk
    references products
);
