-- test account
-- TODO: password is exposed. need to think of how to improve it
INSERT INTO admin_account
(user_id, user_password, role_types, nickname, email, memo, created_at, created_by, modified_at, modified_by)
VALUES
    ('eunah', '{noop}1234', 'ADMIN', 'Eunah', 'eunah@mail.com', 'I am Eunah.', now(), 'eunah', now(), 'eunah'),
    ('mark', '{noop}asdf1234', 'MANAGER', 'Mark', 'mark@mail.com', 'I am Mark.', now(), 'uno', now(), 'uno'),
    ('susan', '{noop}asdf1234', 'MANAGER,DEVELOPER', 'Susan', 'Susan@mail.com', 'I am Susan.', now(), 'uno', now(), 'uno'),
    ('jim', '{noop}asdf1234', 'USER', 'Jim', 'jim@mail.com', 'I am Jim.', now(), 'uno', now(), 'uno');
