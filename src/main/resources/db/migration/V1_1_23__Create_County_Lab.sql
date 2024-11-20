-- tbl_county_lab

CREATE TABLE `tbl_county_lab`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`     BIGINT NULL,
    `created_date`   DATETIME     NOT NULL,
    `modified_by`    BIGINT NULL,
    `modified_date`  DATETIME     NOT NULL,
    `county_id`      BIGINT       NOT NULL,
    `lab_id`      BIGINT       NOT NULL,

    PRIMARY KEY (`id`),
    KEY `Fk_county_lab_county_id` (`county_id`),
    CONSTRAINT `Fk_county_lab_county_id` FOREIGN KEY (`county_id`) REFERENCES `tbl_county` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    KEY `Fk_county_lab_lab_id` (`lab_id`),
    CONSTRAINT `Fk_county_lab_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `tbl_lab` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=INNODB;
