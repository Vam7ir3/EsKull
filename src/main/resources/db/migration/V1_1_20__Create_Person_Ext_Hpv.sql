-- tbl_person_ext_hpv

CREATE TABLE `tbl_person_ext_hpv`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`     BIGINT NULL,
    `created_date`   DATETIME     NOT NULL,
    `modified_by`    BIGINT NULL,
    `modified_date`  DATETIME     NOT NULL,
    `person_id`      BIGINT       NOT NULL,
    `ext_hpv_id`      BIGINT       NOT NULL,

    PRIMARY KEY (`id`),
    KEY `Fk_person_ext_hpv_person_id` (`person_id`),
    CONSTRAINT `Fk_person_ext_hpv_person_id` FOREIGN KEY (`person_id`) REFERENCES `tbl_person` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    KEY `FK_person_ext_hpv_ext_hpv_id` (`ext_hpv_id`),
    CONSTRAINT `FK_person_ext_hpv_ext_hpv_id` FOREIGN KEY (`ext_hpv_id`) REFERENCES `tbl_ext_hpv` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=INNODB;
