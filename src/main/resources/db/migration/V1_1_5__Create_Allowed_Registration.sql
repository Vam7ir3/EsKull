CREATE TABLE `tbl_allowed_registration`
(
    `id`                   BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`           BIGINT       NULL,
    `created_date`         DATETIME     NOT NULL,
    `modified_by`          BIGINT       NULL,
    `modified_date`        DATETIME     NOT NULL,
    `email`                VARCHAR(256) NOT NULL,
    `registered_date_time` DATETIME     NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;

-- Insert authorities of Allowed Registration
INSERT INTO tbl_authority (created_date, modified_date, title, description)
VALUES (NOW(), NOW(), 'ALLOWED_REGISTRATION','Allowed Registration'),
       (NOW(), NOW(), 'ALLOWED_REGISTRATION_C', 'Allowed Registration (Create)'),
       (NOW(), NOW(), 'ALLOWED_REGISTRATION_U', 'Allowed Registration (Update)'),
       (NOW(), NOW(), 'ALLOWED_REGISTRATION_D', 'Allowed Registration (Delete)');

-- Insert Allowed Registration List authorities to provide access to Super Admin
INSERT INTO tbl_role_authority(role_id, authority_id)
VALUES ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'ALLOWED_REGISTRATION')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'ALLOWED_REGISTRATION_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'ALLOWED_REGISTRATION_U')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'ALLOWED_REGISTRATION_D'));