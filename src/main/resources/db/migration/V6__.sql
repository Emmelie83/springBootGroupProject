ALTER TABLE user
    ADD CONSTRAINT uc_user_user_name UNIQUE (user_name);