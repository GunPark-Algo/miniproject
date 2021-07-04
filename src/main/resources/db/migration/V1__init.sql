-- USE mini;

CREATE TABLE IF NOT EXISTS `menus` (
    `code` varchar(10) NOT NULL,
    `name` varchar(20) NOT NULL,
    `price` bigint NOT NULL,
    `quantity` bigint NOT NULL,
    PRIMARY KEY (`code`)
) ENGINE=InnoDB;

INSERT INTO menus (code, name, price, quantity) VALUES ("MENU_01", "jjajang", 6000, 10);
INSERT INTO menus (code, name, price, quantity) VALUES ("MENU_02", "friedrice", 8000, 10);
INSERT INTO menus (code, name, price, quantity) VALUES ("MENU_03", "sandwich", 5000, 10);
INSERT INTO menus (code, name, price, quantity) VALUES ("MENU_04", "pigfoot", 20000, 10);
INSERT INTO menus (code, name, price, quantity) VALUES ("MENU_05", "chicken", 12000, 10);

CREATE TABLE IF NOT EXISTS `orders` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `distance` int DEFAULT NULL,
    `total_price` int DEFAULT NULL,
    `order_no` varchar(20) DEFAULT NULL,
    `shop_no` varchar(20) DEFAULT NULL,
    `result` varchar(20) DEFAULT NULL,
    `access_time` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE INDEX i_orders ON orders(order_no);

CREATE TABLE IF NOT EXISTS `riders` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `rider_id` varchar(10) NOT NULL,
    `rider_name` varchar(20) NOT NULL,
    `vehicle` varchar(20) NOT NULL,
    `speed` int NOT NULL,
    `delivery_id` bigint DEFAULT 0,
    `remain_distance` int DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  AUTO_INCREMENT=1;;

CREATE INDEX i_riders ON riders(rider_id);

INSERT INTO riders (rider_id, rider_name, vehicle, speed, delivery_id) VALUES ("RIDER_A01", "Im", "motorcycle", 100, 0);
INSERT INTO riders (rider_id, rider_name, vehicle, speed, delivery_id) VALUES ("RIDER_A02", "Ko", "bicycle", 50, 0);

CREATE TABLE IF NOT EXISTS `delivery` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `order_no` varchar(10) NOT NULL,
--     `rider_id` varchar(20) DEFAULT NULL,
    `status` varchar(20) DEFAULT NULL,
    `distance` int NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  AUTO_INCREMENT=1;
CREATE INDEX i_delivery_orderno_orders ON delivery(order_no);
CREATE INDEX i_delivery_status_orders ON delivery(status);