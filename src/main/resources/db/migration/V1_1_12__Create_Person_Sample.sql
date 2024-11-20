-- tbl_person_sample

CREATE TABLE `tbl_person_sample`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`     BIGINT NULL,
    `created_date`   DATETIME     NOT NULL,
    `modified_by`    BIGINT NULL,
    `modified_date`  DATETIME     NOT NULL,
    `person_id`      BIGINT       NOT NULL,
    `sample_id`      BIGINT       NOT NULL,

    PRIMARY KEY (`id`),
    KEY `Fk_person_sample_person_id` (`person_id`),
    CONSTRAINT `Fk_person_sample_person_id` FOREIGN KEY (`person_id`) REFERENCES `tbl_person` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    KEY `FK_person_sample_sample_id` (`sample_id`),
    CONSTRAINT `FK_person_sample_sample_id` FOREIGN KEY (`sample_id`) REFERENCES `tbl_sample` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=INNODB;
