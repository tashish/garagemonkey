DROP DATABASE IF EXISTS `garagemonkey`;
CREATE DATABASE garagemonkey;

CREATE USER garageadmin IDENTIFIED BY 'garageADMIN%123456';
GRANT ALL PRIVILEGES ON garagemonkey.* TO 'garageadmin'@'%';

USE garagemonkey;

DROP TABLE IF EXISTS `parking_spots`;

CREATE TABLE `parking_spots` (
  `spot_id` int(11) NOT NULL AUTO_INCREMENT,
  `level` int(11) NOT NULL,
  `row` varchar(2) NOT NULL,
  `spot_number` int(11) NOT NULL,
  `size` int(11) NOT NULL,
  `hourly_rate` decimal(3,0) NOT NULL,
  `date_created` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`spot_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_details`;

CREATE TABLE `user_details` (
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `f_name` varchar(45) NOT NULL,
  `l_name` varchar(45) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `user_type` int(1) NOT NULL,
  `active` int(1) NOT NULL,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `reservations`;

CREATE TABLE `reservations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicle_id` varchar(10) NOT NULL,
  `spot_id` int(11) NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `reserved_by` varchar(50) NOT NULL,
  `reserved_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `comments` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `spot_id_idx` (`spot_id`),
  CONSTRAINT `fkey_spot_id` FOREIGN KEY (`spot_id`) REFERENCES `parking_spots` (`spot_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `reservations_history`;

CREATE TABLE `reservations_history` (
  `id` int(11) NOT NULL,
  `vehicle_id` varchar(10) NOT NULL,
  `spot_id` int(11) NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `reserved_by` varchar(50) NOT NULL,
  `cancelled` tinyint(4) NOT NULL,
  `reserved_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `comments` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO user_details(email, password, f_name, l_name, phone, user_type, active) VALUES ('admin@garagemonkey.com', 'changeme', 'Admin', 'User', '1234567890', 3, 1);
