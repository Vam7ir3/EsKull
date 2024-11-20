-- tbl_person
CREATE TABLE `tbl_person`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `date_of_birth` BIGINT       NOT NULL,
    `pnr`           INTEGER      NOT NULL,
    `is_validpnr`   BOOLEAN      NOT NULL,
    `is_by_year`    BOOLEAN      DEFAULT FALSE,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;