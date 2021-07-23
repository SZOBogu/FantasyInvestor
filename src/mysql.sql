DROP SCHEMA IF EXISTS `FantasyInvestor`;

CREATE SCHEMA `FantasyInvestor`;

use `FantasyInvestor`;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `user` (
                        `user_id` int NOT NULL AUTO_INCREMENT,
                        `username` varchar(45) DEFAULT NULL,
                        `password` varchar(45) DEFAULT NULL,
                        `role` varchar(45) DEFAULT NULL,
                        `portfolio_id` int DEFAULT NULL,
                        PRIMARY KEY (`user_id`),
                        UNIQUE KEY `TITLE_UNIQUE` (`username`),
                        KEY `FK_DETAIL_idx` (`portfolio_id`),
                        CONSTRAINT `FK_DETAIL` FOREIGN KEY (`portfolio_id`)
                            REFERENCES `portfolio` (`portfolio_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `portfolio`;

CREATE TABLE `portfolio` (
                             `portfolio_id` int NOT NULL AUTO_INCREMENT,
                             `asset_id` int DEFAULT NULL,

                             PRIMARY KEY (`portfolio_id`),
                             KEY `FK_DETAIL_idx` (`asset_id`),
                             CONSTRAINT `FK_DETAIL1` FOREIGN KEY (`asset_id`)
                                 REFERENCES `asset` (`asset_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `asset`;

CREATE TABLE `asset` (
                         `asset_id` int NOT NULL AUTO_INCREMENT,
                         `quantity` int DEFAULT NULL,
                         `buyPrice` int DEFAULT NULL,
                         `stock_id` int DEFAULT NULL,
                         `portfolio_id` int DEFAULT NULL,
                         PRIMARY KEY (`asset_id`),
                         KEY `FK_DETAIL_idx` (`stock_id`),
                         CONSTRAINT `FK_DETAIL2` FOREIGN KEY (`stock_id`)
                             REFERENCES `stock` (`stock_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
                         CONSTRAINT `FK_DETAIL5` FOREIGN KEY (`portfolio_id`)
                             REFERENCES `portfolio` (`portfolio_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `stock`;

CREATE TABLE `stock` (
                         `stock_id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(45) DEFAULT NULL,
                         `currentPrice` DOUBLE DEFAULT NULL,
                         `priceAtTheStartOfTheDay` DOUBLE DEFAULT NULL,
                         `priceAtTheStartOfTheWeek` DOUBLE DEFAULT NULL,
                         `priceAtTheStartOfTheMonth` DOUBLE DEFAULT NULL,
                         `priceAtTheStartOfTheYear` DOUBLE DEFAULT NULL,
                         `asset_id` int DEFAULT NULL,
                         PRIMARY KEY (`stock_id`),
                         UNIQUE KEY `TITLE_UNIQUE` (`name`),
                         KEY `FK_DETAIL12_idx` (`asset_id`),
                         CONSTRAINT `FK_DETAIL13` FOREIGN KEY (`asset_id`)
                             REFERENCES `asset` (`asset_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
                         KEY `FK_DETAIL_idx` (`asset_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


SET FOREIGN_KEY_CHECKS = 1;

SELECT * FROM user;
SELECT * FROM portfolio;
SELECT * FROM asset;
SELECT * FROM stock;

INSERT INTO stock (name, currentPrice, priceAtTheStartOfTheDay, priceAtTheStartOfTheWeek, priceAtTheStartOfTheMonth, priceAtTheStartOfTheYear)
VALUES ("Cash", 1, 1, 1, 1, 1);

SHOW INDEXES FROM asset;