INSERT INTO ROLE (ROLEID, ROLENAME) VALUES
(1, 'User'),
(2, 'Admin');

INSERT INTO USER (USERID, ROLEID, USERNAME, PASSWORD, EMAIL) VALUES
(1, 1, 'test_user', 'test_user', 'test_user'),
(2, 2, 'super_admin', 'super_admin', 'super_admin');