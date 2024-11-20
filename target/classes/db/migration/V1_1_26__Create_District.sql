-- tbl_district
CREATE TABLE `tbl_district`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `district`    VARCHAR(255) NOT NULL,
    `district_name`    VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;