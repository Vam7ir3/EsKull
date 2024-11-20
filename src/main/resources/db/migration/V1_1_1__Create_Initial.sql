-- tbl_country
CREATE TABLE `tbl_country`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `name`          VARCHAR(500) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_country_name` (`name`)
) ENGINE = INNODB;


-- tbl_state
CREATE TABLE `tbl_state`
(
    `id`            BIGINT        NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT        NULL,
    `created_date`  DATETIME      NOT NULL,
    `modified_by`   BIGINT        NULL,
    `modified_date` DATETIME      NOT NULL,
    `name`          VARCHAR(1000) NOT NULL,
    `country_id`    BIGINT        NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_state_country_id` (`country_id`),
    CONSTRAINT `FK_state_country_id` FOREIGN KEY (`country_id`) REFERENCES tbl_country (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;


-- tbl_city
CREATE TABLE `tbl_city`
(
    `id`            BIGINT        NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT        NULL,
    `created_date`  DATETIME      NOT NULL,
    `modified_by`   BIGINT        NULL,
    `modified_date` DATETIME      NOT NULL,
    `name`          VARCHAR(1000) NOT NULL,
    `state_id`      BIGINT        NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_city_state_id` (`state_id`),
    CONSTRAINT `FK_city_state_id` FOREIGN KEY (`state_id`) REFERENCES `tbl_state` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;


-- tbl_address
CREATE TABLE `tbl_address`
(
    `id`            BIGINT        NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT        NULL,
    `created_date`  DATETIME      NOT NULL,
    `modified_by`   BIGINT        NULL,
    `modified_date` DATETIME      NOT NULL,
    `street`        VARCHAR(1000) NOT NULL,
    `city_id`       BIGINT        NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_address_city_id` (`city_id`),
    CONSTRAINT `FK_address_city_id` FOREIGN KEY (`city_id`) REFERENCES `tbl_city` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;


-- tbl_authority
CREATE TABLE `tbl_authority`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `title`         VARCHAR(256) NOT NULL,
    `description`   VARCHAR(256) DEFAULT '',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_authority_title` (`title`)
) ENGINE = INNODB;


-- tbl_role
CREATE TABLE `tbl_role`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `title`         VARCHAR(500) NOT NULL,
    `description`   VARCHAR(256) DEFAULT '',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_role_title` (`title`)
) ENGINE = INNODB;


-- tbl_role_authority
CREATE TABLE `tbl_role_authority`
(
    `role_id`      BIGINT NOT NULL,
    `authority_id` BIGINT NOT NULL,
    PRIMARY KEY (`role_id`, `authority_id`),
    KEY `FK_role_authority_role_id` (`role_id`),
    KEY `FK_role_authority_authority_id` (`authority_id`),
    CONSTRAINT `FK_role_authority_role_id` FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_role_authority_authority_id` FOREIGN KEY (`authority_id`) REFERENCES `tbl_authority` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;


-- tbl_user
CREATE TABLE `tbl_user`
(
    `id`                         BIGINT                           NOT NULL AUTO_INCREMENT,
    `created_by`                 BIGINT                           NULL,
    `created_date`               DATETIME                         NOT NULL,
    `modified_by`                BIGINT                           NULL,
    `modified_date`              DATETIME                         NOT NULL,
    `mobile_number`              VARCHAR(50)                      NOT NULL,
    `email_address`              VARCHAR(256)                     NOT NULL,
    `password`                   VARCHAR(256)                     NOT NULL,
    `first_name`                 VARCHAR(256)                     NOT NULL,
    `last_name`                  VARCHAR(256)                     NOT NULL,
    `date_of_birth`              DATE                             NULL,
    `gender`                     ENUM ('MALE', 'FEMALE', 'OTHER') NULL,
    `last_password_reset_date`   DATETIME                         NOT NULL,
    `image_url`                  VARCHAR(2000)                    NULL,
    `verification_code`          VARCHAR(256)                     NULL,
    `verification_code_deadline` DATETIME                         NULL,
    `mobile_number_verified`     BOOLEAN                          NULL,
    `email_address_verified`     BOOLEAN                          NULL,
    `role_id`                    BIGINT                           NOT NULL,
    `address_id`                 BIGINT                           NULL,
    PRIMARY KEY (`id`),
    UNIQUE `UK_user_email_address_mobile_number` (`email_address`, `mobile_number`),
    KEY `FK_user_role_id` (`role_id`),
    KEY `FK_user_address_id` (`address_id`),
    CONSTRAINT `FK_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_user_address_id` FOREIGN KEY (`address_id`) REFERENCES `tbl_address` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;
