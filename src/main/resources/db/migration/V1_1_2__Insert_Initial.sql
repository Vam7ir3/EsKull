-- tbl_authority
INSERT INTO `tbl_authority` (id, created_date, modified_date, title, description)
VALUES (1, NOW(), NOW(), 'COUNTRY_C', 'Country (Create)'),
       (2, NOW(), NOW(), 'COUNTRY_U', 'Country (Update)'),
       (3, NOW(), NOW(), 'COUNTRY_D', 'Country (Delete)'),

       (4, NOW(), NOW(), 'STATE_C', 'State (Create)'),
       (5, NOW(), NOW(), 'STATE_U', 'State (Update)'),
       (6, NOW(), NOW(), 'STATE_D', 'State (Delete)'),

       (7, NOW(), NOW(), 'CITY_C', 'City (Create)'),
       (8, NOW(), NOW(), 'CITY_U', 'City (Update)'),
       (9, NOW(), NOW(), 'CITY_D', 'City (Delete)'),

       (10, NOW(), NOW(), 'AUTHORITY', 'Authority'),
       (11, NOW(), NOW(), 'AUTHORITY_C', 'Authority (Create)'),
       (12, NOW(), NOW(), 'AUTHORITY_U', 'Authority (Update)'),
       (13, NOW(), NOW(), 'AUTHORITY_D', 'Authority (Delete)"'),

       (14, NOW(), NOW(), 'ROLE', 'Role'),
       (15, NOW(), NOW(), 'ROLE_RA', 'Role (Read All)'),
       (16, NOW(), NOW(), 'ROLE_C', 'Role (Create)'),
       (17, NOW(), NOW(), 'ROLE_U', 'Role (Update)'),
       (18, NOW(), NOW(), 'ROLE_D', 'Role (Delete)'),

       (19, NOW(), NOW(), 'USER', 'User'),
       (20, NOW(), NOW(), 'USER_RA', 'User (Create)'),
       (21, NOW(), NOW(), 'USER_C', 'User (Read All)'),
       (22, NOW(), NOW(), 'USER_U', 'User (Update)'),
       (23, NOW(), NOW(), 'USER_D', 'User (Delete)'),
       (24, NOW(), NOW(), 'CHANGE_PASSWORD', 'Change Password');

-- tbl_role
INSERT INTO `tbl_role` (id, created_date, modified_date, title)
VALUES (1, NOW(), NOW(), 'Super Admin');

INSERT INTO `tbl_role` (id, created_date, modified_date, title)
VALUES (2, NOW(), NOW(), 'Researcher Admin');


-- tbl_role_authority
INSERT INTO `tbl_role_authority` (role_id, authority_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15),
       (1, 16),
       (1, 17),
       (1, 18),
       (1, 19),
       (1, 20),
       (1, 21),
       (1, 22),
       (1, 23),
       (1, 24),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (2, 8),
       (2, 9),
       (2, 10),
       (2, 11),
       (2, 12),
       (2, 13),
       (2, 14),
       (2, 15),
       (2, 16),
       (2, 17),
       (2, 18),
       (2, 19),
       (2, 20),
       (2, 21),
       (2, 22),
       (2, 23),
       (2, 24);


-- tbl_user
INSERT INTO `tbl_user` (id, created_date, modified_date, mobile_number, email_address, password, first_name, last_name,
                        gender, email_address_verified, last_password_reset_date, role_id)
VALUES (1, NOW(), NOW(), 0123456789, 'sa@yopmail.com', '$2a$04$ND04Es6dx5AtjOuNzmbvPOkrwMCrs2oF9Zj/focP5TG9KeFaZ8okq',
        'Super', 'Admin', 'MALE', true, NOW(), 1);

INSERT INTO `tbl_user` (id, created_date, modified_date, mobile_number, email_address, password, first_name, last_name,
                        gender, email_address_verified, last_password_reset_date, role_id)
VALUES (2, NOW(), NOW(), 9876543210, 'researcher@yopmail.com', '$2a$04$ND04Es6dx5AtjOuNzmbvPOkrwMCrs2oF9Zj/focP5TG9KeFaZ8okq',
        'Researcher', 'Admin', 'MALE', true, NOW(), 2);
