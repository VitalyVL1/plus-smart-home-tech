-- Создание схем для каждого микросервиса
CREATE SCHEMA IF NOT EXISTS telemetry;
CREATE SCHEMA IF NOT EXISTS commerce_cart;
CREATE SCHEMA IF NOT EXISTS commerce_store;
CREATE SCHEMA IF NOT EXISTS commerce_warehouse;
CREATE SCHEMA IF NOT EXISTS commerce_interaction; /* возможно данная схема лишняя, удалить если не будет использоваться*/