-- tbl_klartext
CREATE TABLE `tbl_klartext`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `snomed_text`    VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;