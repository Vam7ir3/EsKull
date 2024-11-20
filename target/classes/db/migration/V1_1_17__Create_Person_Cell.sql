-- tbl_person_cel

CREATE TABLE `tbl_person_cell`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`     BIGINT NULL,
    `created_date`   DATETIME     NOT NULL,
    `modified_by`    BIGINT NULL,
    `modified_date`  DATETIME     NOT NULL,
    `person_id`      BIGINT       NOT NULL,
    `cell_id`      BIGINT       NOT NULL,

    PRIMARY KEY (`id`),
    KEY `FK_person_cell_person_id` (`person_id`),
    CONSTRAINT `FK_person_cell_person_id` FOREIGN KEY (`person_id`) REFERENCES `tbl_person` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    KEY `FK_person_cell_cell_id` (`cell_id`),
    CONSTRAINT `FK_person_cell_cell_id` FOREIGN KEY (`cell_id`) REFERENCES `tbl_cell` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=INNODB;
