CREATE TABLE orders (
    id uuid PRIMARY KEY,
    user_id uuid UNIQUE NOT NULL,
    user_name varchar(50) NOT NULL,
    user_phone varchar(12) NOT NULL,
    courier_id uuid,
    courier_name varchar(50),
    courier_phone varchar(12),
    order_number integer NOT NULL,
    destination varchar(100) NOT NULL,
    status varchar(20),
    delivery_datetime timestamp,
    pickup_datetime timestamp,
    comments varchar(200),
    created_at timestamp,
    updated_at timestamp
);
