CREATE TABLE `tbl_multi_lang_message`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `code`          VARCHAR(50)  NOT NULL,
    `english`       VARCHAR(1000) NOT NULL,
    `swedish`       VARCHAR(1000) NULL,
    `spanish`       VARCHAR(1000) NULL,
    PRIMARY KEY (`id`),
    UNIQUE `UK_multi_lang_message_code` (`code`)
) ENGINE = INNODB;

-- Insert authorities of Multi Lang Message
INSERT INTO tbl_authority (created_date, modified_date, title, description)
VALUES (NOW(), NOW(), 'MULTI_LANG_MESSAGE', 'Multi Lang Message'),
       (NOW(), NOW(), 'MULTI_LANG_MESSAGE_C', 'Multi Lang Message (Create)'),
       (NOW(), NOW(), 'MULTI_LANG_MESSAGE_U', 'Multi Lang Message (Update)'),
       (NOW(), NOW(), 'MULTI_LANG_MESSAGE_D', 'Multi Lang Message (Delete)');

-- Insert Multi Lang Message List authorities to provide access to Super Admin
INSERT INTO tbl_role_authority(role_id, authority_id)
VALUES ((SELECT id FROM tbl_role WHERE title = 'Super Admin'), (SELECT id FROM tbl_authority WHERE title = 'MULTI_LANG_MESSAGE')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'), (SELECT id FROM tbl_authority WHERE title = 'MULTI_LANG_MESSAGE_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'), (SELECT id FROM tbl_authority WHERE title = 'MULTI_LANG_MESSAGE_U')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'), (SELECT id FROM tbl_authority WHERE title = 'MULTI_LANG_MESSAGE_D'));