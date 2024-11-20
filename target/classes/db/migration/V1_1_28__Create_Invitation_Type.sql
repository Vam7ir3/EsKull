-- tbl_invitation_type
CREATE TABLE `tbl_invitation_type`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `type`          VARCHAR(255) NOT NULL,
    `x_type`        VARCHAR(255) NOT NULL,
    `description`  VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;