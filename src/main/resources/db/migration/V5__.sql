ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);