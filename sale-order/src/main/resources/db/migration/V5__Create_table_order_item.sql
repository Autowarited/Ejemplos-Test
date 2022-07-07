CREATE TABLE IF NOT EXISTS order_item
(
    id bigserial not null primary key ,
    item_id bigint NOT NULL,
    order_id bigint NOT NULL
)

