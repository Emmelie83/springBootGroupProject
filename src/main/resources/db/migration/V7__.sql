ALTER TABLE user
    ADD avatar_url VARCHAR(255) NULL;

ALTER TABLE user
    ADD full_name VARCHAR(255) NULL;

ALTER TABLE user
    ADD git_id INT NULL;

ALTER TABLE user
    ADD CONSTRAINT uc_user_git UNIQUE (git_id);

ALTER TABLE user
    DROP COLUMN first_name;

ALTER TABLE user
    DROP COLUMN last_name;