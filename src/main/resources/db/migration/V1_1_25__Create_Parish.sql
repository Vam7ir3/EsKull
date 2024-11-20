-- tbl_parish

CREATE TABLE `tbl_parish`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`     BIGINT NULL,
    `created_date`   DATETIME     NOT NULL,
    `modified_by`    BIGINT NULL,
    `modified_date`  DATETIME     NOT NULL,
    `name`           VARCHAR(255) NOT NULL,
    `register_date`  BIGINT       NOT NULL,
    `divided_other_county`  VARCHAR(255) NOT NULL,
    `municipality_id`      BIGINT DEFAULT NULL,
    `county_id`      BIGINT DEFAULT NULL,

    PRIMARY KEY (`id`),
    KEY `Fk_parish_municipality_id` (`municipality_id`),
    CONSTRAINT `Fk_parish_municipality_id` FOREIGN KEY (`municipality_id`) REFERENCES `tbl_municipality` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    KEY `Fk_parish_county_id` (`county_id`),
    CONSTRAINT `Fk_parish_county_id` FOREIGN KEY (`county_id`) REFERENCES `tbl_county` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=INNODB;
