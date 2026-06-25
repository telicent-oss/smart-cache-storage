--
-- Copyright (C) Telicent Ltd
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
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
    billingaddressid   bigint
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
