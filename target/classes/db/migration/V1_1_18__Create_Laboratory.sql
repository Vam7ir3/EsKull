-- tbl_laboratory
CREATE TABLE `tbl_laboratory`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `name`          VARCHAR(255) NOT NULL,
    `is_in_Use`     BOOLEAN NOT NULL,
    `sos_lab`       INTEGER NOT NULL,
    `sos_lab_name`   VARCHAR(255) NOT NULL,
    `sos_long_name`  VARCHAR(300)  NOT NULL,
    `region`         VARCHAR(255)  NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;