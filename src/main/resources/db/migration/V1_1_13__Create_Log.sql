-- tbl_log
CREATE TABLE `tbl_log`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `description`          VARCHAR(255) NOT NULL,
    `operation`      VARCHAR(50)    NOT NULL ,
    `user_id`      BIGINT       NOT NULL,
    `timestamp`     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    KEY `FK_log_user_id)` (`user_id`),
    CONSTRAINT `FK_log_user_id` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;