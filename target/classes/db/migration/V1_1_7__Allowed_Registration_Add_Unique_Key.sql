DELETE t1
FROM `tbl_allowed_registration` t1
         INNER JOIN `tbl_allowed_registration` t2
WHERE t1.id < t2.id
  AND t1.email = t2.email;

ALTER TABLE `tbl_allowed_registration`
    ADD CONSTRAINT UK_allowed_registration_email UNIQUE (`email`);