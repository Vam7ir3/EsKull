-- tbl_student
CREATE TABLE `tbl_student`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `first_name`     VARCHAR(255) NOT NULL,
    `middle_name`    VARCHAR(255),
    `last_name`      VARCHAR(255) NOT NULL,
    `batch_number`   VARCHAR(255) NOT NULL,
    `grade`          VARCHAR(255) NOT NULL,
    `date_of_birth`   BIGINT       NOT NULL,
    `year`          VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;