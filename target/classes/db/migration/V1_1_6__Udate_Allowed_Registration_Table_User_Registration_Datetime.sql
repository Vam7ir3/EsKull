UPDATE tbl_allowed_registration INNER JOIN tbl_user ON tbl_allowed_registration.email = tbl_user.email_address
 SET tbl_allowed_registration.registered_date_time = tbl_user.created_date;