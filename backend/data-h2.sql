INSERT INTO ROLE (ROLEID, ROLENAME) VALUES
(1, 'User'),
(2, 'Admin');

INSERT INTO USER (USERID, ROLEID, USERNAME, PASSWORD, EMAIL) VALUES
(1, 1, 'test_user', '31676025316fb555e0bfa12f0bcfd0ea43c4c20e', 'test_user'),
(2, 2, 'super_admin', '736a95382da0c1b931dc87529706d25c636954e7', 'super_admin');