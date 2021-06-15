DROP SCHEMA IF EXISTS `FantasyInvestor`;

CREATE SCHEMA `FantasyInvestor`;

use `FantasyInvestor`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `first_name` varchar(45) DEFAULT NULL,
                              `last_name` varchar(45) DEFAULT NULL,
                              `email` varchar(45) DEFAULT NULL,
                              `instructor_detail_id` int(11) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `FK_DETAIL_idx` (`instructor_detail_id`),
                              CONSTRAINT `FK_DETAIL` FOREIGN KEY (`instructor_detail_id`)
                                  REFERENCES `instructor_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `portfolio`;

CREATE TABLE `portfolio` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `title` varchar(128) DEFAULT NULL,
                          `instructor_id` int(11) DEFAULT NULL,

                          PRIMARY KEY (`id`),

                          UNIQUE KEY `TITLE_UNIQUE` (`title`),

                          KEY `FK_INSTRUCTOR_idx` (`instructor_id`),

                          CONSTRAINT `FK_INSTRUCTOR`
                              FOREIGN KEY (`instructor_id`)
                                  REFERENCES `instructor` (`id`)

                                  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `asset`;

CREATE TABLE `asset` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `first_name` varchar(45) DEFAULT NULL,
                        `last_name` varchar(45) DEFAULT NULL,
                        `email` varchar(45) DEFAULT NULL,
                        `instructor_detail_id` int(11) DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `FK_DETAIL_idx` (`instructor_detail_id`),
                        CONSTRAINT `FK_DETAIL` FOREIGN KEY (`instructor_detail_id`)
                            REFERENCES `instructor_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `stock`;

CREATE TABLE `stock` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `first_name` varchar(45) DEFAULT NULL,
                        `last_name` varchar(45) DEFAULT NULL,
                        `email` varchar(45) DEFAULT NULL,
                        `instructor_detail_id` int(11) DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `FK_DETAIL_idx` (`instructor_detail_id`),
                        CONSTRAINT `FK_DETAIL` FOREIGN KEY (`instructor_detail_id`)
                            REFERENCES `instructor_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


SET FOREIGN_KEY_CHECKS = 1;