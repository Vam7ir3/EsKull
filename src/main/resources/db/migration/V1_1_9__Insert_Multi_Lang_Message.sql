INSERT INTO `tbl_multi_lang_message` (id, created_date, modified_date, code, english, swedish)
VALUES -- Authority
       (1, NOW(), NOW(), 'AUT001', 'Authority already exists.', 'Auktoritet finns redan.'),
       (2, NOW(), NOW(), 'AUT002', 'Authority does not exist.', 'Auktoritet finns inte.'),

       -- About Us
       (3, NOW(), NOW(), 'ABT001', 'About Us does not exist.', 'Om användning finns inte.'),

       -- Cell
       (4, NOW(), NOW(), 'CELL001', 'Cell does not exist.', 'Cellen finns inte.'),
       (5, NOW(), NOW(), 'CELL002', 'Cell already exists.', 'Cellen finns redan.'),

       -- City
       (6, NOW(), NOW(), 'CIT001', 'City already exists.', 'Staden finns redan.'),
       (7, NOW(), NOW(), 'CIT002', 'City does not exist.', 'Staden finns inte.'),
       (8, NOW(), NOW(), 'CIT003', 'There are addresses associated with this city.', 'Det finns adresser kopplade till denna stad.'),

       -- Country
       (9, NOW(), NOW(), 'COU001', 'Country does not exist.', 'Land finns inte.'),
       (10, NOW(), NOW(), 'COU002', 'Country already exists.', 'Landet finns redan.'),

       -- County
       (11, NOW(), NOW(), 'COUNTY001', 'County does not exist.', 'Länet finns inte.'),
       (12, NOW(), NOW(), 'COUNTY002', 'County already exists.', 'Länet finns redan.'),

       -- District
       (13, NOW(), NOW(), 'DISTRICT001', 'District does not exist.', 'Distriktet finns inte.'),
       (14, NOW(), NOW(), 'DISTRICT002', 'District already exists.', 'Distriktet finns redan.'),

       -- General Error
       (15, NOW(), NOW(), 'ERR001', 'Something went wrong', 'Något gick fel'),
       (16, NOW(), NOW(), 'ERR002', 'Username and/or password is wrong.', 'Fel användarnamn och / eller lösenord'),
       (17, NOW(), NOW(), 'ERR003', 'You are not Authorized.', 'Du är inte auktoriserad'),
       (18, NOW(), NOW(), 'ERR004', 'Invalid token.', 'Ogiltigt token.'),
       (19, NOW(), NOW(), 'ERR005', 'Super Admin cannot be deleted.', 'Superadmin kan inte raderas.'),
       (20, NOW(), NOW(), 'ERR006', 'No token found.', 'Ingen token hittades.'),
       (21, NOW(), NOW(), 'ERR007', 'Check for Duplicate data', 'Sök efter duplicerade data'),

       -- ExtHpv
       (22, NOW(), NOW(), 'EXTHPV001', 'ExtHpv does not exist.', 'ExtHpv finns inte.'),
       (23, NOW(), NOW(), 'EXTHPV002', 'ExtHpv already exists.', 'ExtHpv finns redan.'),

       -- Group
       (24, NOW(), NOW(), 'GRP001', 'Group already exists.', 'Gruppen finns redan.'),
       (25, NOW(), NOW(), 'GRP002', 'Group does not exist.', 'Gruppen finns inte.'),
       (26, NOW(), NOW(), 'GRP003', 'Group questionnaire does not exist.', 'Grupp enkät finns inte.'),
       (27, NOW(), NOW(), 'GRP004', 'User has finished answer for this group questionnaire.', 'Användaren har slutfört svaret på detta gruppenkät'),
       (28, NOW(), NOW(), 'GRP005', 'Group Ids not provided.', 'SökGrupp-id anges inte'),

       -- Hpv
       (29, NOW(), NOW(), 'HPV001', 'Hpv does not exist.', 'Hpv finns inte.'),
       (30, NOW(), NOW(), 'HPV002', 'ExtHpv already exists.', 'Hpv finns redan.'),

       -- InvitationType
       (31, NOW(), NOW(), 'INVITATIONTYPE001', 'InvitationType does not exist.', 'InvitationType finns inte.'),
       (32, NOW(), NOW(), 'INVITATIONTYPE002', 'InvitationType already exists.', 'InvitationType finns redan.'),

       -- Klartext
       (33, NOW(), NOW(), 'KLARTEXT001', 'Klartext does not exist.', 'Klartext finns inte.'),
       (34, NOW(), NOW(), 'KLARTEXT002', 'Klartext already exists.', 'Klartext finns redan.'),

       -- Laboratory
       (35, NOW(), NOW(), 'LABORATORY001', 'Laboratory does not exist.', 'Laboratoriet finns inte.'),
       (36, NOW(), NOW(), 'LABORATORY002', 'Laboratory already exists.', 'Laboratoriet finns redan.'),

       -- Lab
       (37, NOW(), NOW(), 'LAB001', 'Lab does not exist.', 'Labbet finns inte.'),
       (38, NOW(), NOW(), 'LAB002', 'Lab already exists.', 'Labbet finns redan.'),

       -- Municipality
       (39, NOW(), NOW(), 'MUNICIPALITY001', 'Municipality does not exist.', 'Kommunen finns inte.'),
       (40, NOW(), NOW(), 'MUNICIPALITY002', 'Municipality already exists.', 'Kommunen finns redan.'),

       -- Parish
       (41, NOW(), NOW(), 'PARISH001', 'Parish does not exist.', 'Församlingen finns inte.'),
       (42, NOW(), NOW(), 'PARISH002', 'Parish already exists.', 'Församlingen finns redan.'),

       -- Person
       (43, NOW(), NOW(), 'PERSON001', 'Person does not exist.', 'Församlingen finns inte.'),
       (44, NOW(), NOW(), 'PERSON002', 'Person already exists.', 'Församlingen finns redan.'),

       -- Questionnaire
       (45, NOW(), NOW(), 'QUE001', 'Questionnaire does not exist.', 'Enkät finns inte.'),
       (46, NOW(), NOW(), 'QUE002', 'Question does not exist.', 'Frågan finns inte.'),
       (47, NOW(), NOW(), 'QUE003', 'Already answered.', 'Har redan svarat'),
       (48, NOW(), NOW(), 'QUE004', 'Answer data is missing.', 'Svarsdata saknas'),
       (49, NOW(), NOW(), 'QUE005', 'Question option does not exist.', 'Frågan finns inte'),
       (50, NOW(), NOW(), 'QUE006', 'Questionnaire already exists.', 'Questionnaire already exists.'),
       (51, NOW(), NOW(), 'QUE007', 'Question Questionnaire does not exist.', 'Enkät finns inte'),
       (52, NOW(), NOW(), 'QUE008', 'Please delete associated questions before deleting questionnaire.', 'Ta bort associerade frågor innan du tar bort enkäten.'),
       (53, NOW(), NOW(), 'QUE009', 'Question already exists.', 'Frågan finns redan.'),
       (54, NOW(), NOW(), 'QUE010', 'Question Type does not exist.', 'Frågetyp finns inte.'),
       (55, NOW(), NOW(), 'QUE011', 'Please delete associated answers first.', 'Ta bort associerade svar först.'),
       (56, NOW(), NOW(), 'QUE012', 'Question Title required.', 'Frågetitel krävs.'),
       (57, NOW(), NOW(), 'QUE013', 'Question body required.', 'Frågeställning krävs.'),

       -- Role
       (58, NOW(), NOW(), 'ROL001', 'Role does not exist.', 'Roll finns inte.'),
       (59, NOW(), NOW(), 'ROL002', 'Role already exists.', 'Roll finns redan.'),
       (60, NOW(), NOW(), 'ROL003', 'Restricted Role.', 'Begränsad roll.'),
       (61, NOW(), NOW(), 'ROL004', 'Role Id not provided.', 'Roll-id anges inte.'),

       -- Sample
       (62, NOW(), NOW(), 'SAMPLE001', 'Sample already exists.', 'Provet finns redan.'),
       (63, NOW(), NOW(), 'SAMPLE002', 'Sample does not exist.', 'Provet finns inte.'),
       -- State
       (64, NOW(), NOW(), 'STA001', 'State already exists.', 'Staten finns redan.'),
       (65, NOW(), NOW(), 'STA002', 'State does not exist.', 'Staten finns inte.'),

       -- User
       (66, NOW(), NOW(), 'USR001', 'User does not exist.', 'Användare finns inte.'),
       (67, NOW(), NOW(), 'USR002', 'User already exists.', 'Användare finns redan.'),
       (68, NOW(), NOW(), 'USR003', 'You do not have permission to change the password.', 'Du har inte behörighet att ändra lösenordet.'),
       (69, NOW(), NOW(), 'USR004', 'Email Address is missing.', 'E-postadress saknas.'),
       (70, NOW(), NOW(), 'USR005', 'Email Address already exists.', 'Emailadressen finns redan.'),
       (71, NOW(), NOW(), 'USR006', 'Mobile Number already exists.', 'Mobilnummer finns redan.'),
       (72, NOW(), NOW(), 'USR007', 'User with this email address already exists.', 'Användare med den här e-postadressen finns redan.'),
       (73, NOW(), NOW(), 'USR008', 'User with this mobile number already exists.', 'Användare med det här mobilnumret finns redan.'),
       (74, NOW(), NOW(), 'USR009', 'Allowed registration does not exist.', 'Tillåten registrering finns inte.'),
       (75, NOW(), NOW(), 'USR010', 'Device does not exist.', 'Enheten finns inte.'),

       -- Verification
       (76, NOW(), NOW(), 'VER001', 'Verification details are missing.', 'Verifieringsinformation saknas.'),
       (77, NOW(), NOW(), 'VER002', 'Verification code is wrong or expired.',
        'Verifieringskoden är felaktig eller är inte längre giltig.'),
       (78, NOW(), NOW(), 'VER003', 'Email address did not match.', 'E-postadressen matchade inte.'),
       (79, NOW(), NOW(), 'VER004', 'Mobile number did not match.', 'Mobilnumret matchade inte.'),
       (80, NOW(), NOW(), 'VER005', 'Email Address or Mobile Number is missing.', 'E-postadress eller mobilnummer saknas.'),
       (81, NOW(), NOW(), 'VER006', 'Your Email Address has not been added to the allowed registration list. Please contact the support team.', 'Din e-postadress har inte lagts till i den tillåtna registreringslistan. Kontakta supporten team.'),
       (82, NOW(), NOW(), 'VER007', 'Identity is not provided.', 'Identitet inte tillhandahålls'),
       (83, NOW(), NOW(), 'VER008', 'Password is empty.', 'Lösenordet är tomt'),

       -- SMS
       (84, NOW(), NOW(), 'SMS001', 'Failed to send SMS.', 'Det gick inte att skicka SMS'),

       -- Link
       (85, NOW(), NOW(), 'LNK001', 'Link already exists.', 'Länken finns redan.'),
       (86, NOW(), NOW(), 'LNK002', 'Link does not exist.', 'Länk finns inte.'),

       -- Notification
       (88, NOW(), NOW(), 'NTI001', 'Notification does not exist.', 'Meddelande finns inte.'),

       -- Setting
       (89, NOW(), NOW(), 'SET001', 'Setting does not exist.', 'Inställningen finns inte.'),
       (90, NOW(), NOW(), 'SET002', 'Welcome does not exist.', 'Välkomsttexten finns inte.')
;