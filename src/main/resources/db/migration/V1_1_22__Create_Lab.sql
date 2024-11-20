-- tbl_lab
CREATE TABLE `tbl_lab`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `name`    VARCHAR(255) NOT NULL,
    `is_in_use`     BOOLEAN NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;