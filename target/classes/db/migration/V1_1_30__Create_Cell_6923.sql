-- tbl_cell_6923
CREATE TABLE `tbl_cell_6923`
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`          BIGINT NULL,
    `created_date`        DATETIME     NOT NULL,
    `modified_by`         BIGINT NULL,
    `modified_date`       DATETIME     NOT NULL,
    `person_id`           BIGINT       NOT NULL,
    `laboratory_id`       BIGINT       NOT NULL,
    `county_id`           BIGINT       NOT NULL,
    `sample_date`         BIGINT       NOT NULL,
    `sample_type`         VARCHAR(255),
    `referral_number`     INTEGER      NOT NULL,
    `referral_type`       BIGINT       NOT NULL,
    `reference_site`      VARCHAR(255),
    `residc`              INTEGER,
    `residk`              INTEGER,
    `x_sample_date`       BIGINT     NOT NULL,
    `x_registration_date` BIGINT     NOT NULL,
    `x_snomed`            VARCHAR(255) NOT NULL,
    `diag_id`             VARCHAR(255),
    `ans_clinic`          VARCHAR(255),
    `deb_clinic`          VARCHAR(255),
    `rem_clinic`          VARCHAR(255),
    `registration_date`   BIGINT       NOT NULL,
    `scr_type`            INTEGER,
    `snomed`              VARCHAR(255) NOT NULL,
    `response_date`       BIGINT,
    `x_response_date`     BIGINT,
    `diff_days`           INTEGER,


    PRIMARY KEY (`id`),
    KEY                   `Fk_person_cell6923_id` (`person_id`),
    CONSTRAINT `Fk_person_cell6923_id` FOREIGN KEY (`person_id`) REFERENCES `tbl_person` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    KEY                   `FK_laboratory_cell6923_id` (`laboratory_id`),
    CONSTRAINT `FK_laboratory_cell6923_id` FOREIGN KEY (`laboratory_id`) REFERENCES `tbl_laboratory` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    KEY                   `FK_county_cell6923_id` (`county_id`),
    CONSTRAINT `FK_county_cell6923_id` FOREIGN KEY (`county_id`) REFERENCES `tbl_county` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    KEY                   `FK_referralType_cell6923_id` (`referral_type`),
    CONSTRAINT `FK_referralType_cell6923_id` FOREIGN KEY (`referral_type`) REFERENCES `tbl_reference_type` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE = INNODB;