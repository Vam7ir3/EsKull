CREATE TABLE `tbl_verification`
(
    `id`                           BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`                   BIGINT       NULL,
    `created_date`                 DATETIME     NOT NULL,
    `modified_by`                  BIGINT       NULL,
    `modified_date`                DATETIME     NOT NULL,
    `deadline`                     DATETIME     NOT NULL,
    `email_address`                VARCHAR(256) NULL,
    `mobile_number`                VARCHAR(256) NULL,
    `verification_code`            VARCHAR(256) NOT NULL,
    `verification_code_sent_count` INT          NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;