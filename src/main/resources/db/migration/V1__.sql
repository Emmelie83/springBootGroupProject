CREATE TABLE message
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    date          date                  NULL,
    message_title VARCHAR(255)          NULL,
    message_body  VARCHAR(255)          NULL,
    user          VARCHAR(255)          NULL,
    is_public     BIT(1)                NOT NULL,
    user_id       BIGINT                NULL,
    CONSTRAINT pk_message PRIMARY KEY (id)
);

CREATE TABLE user
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_name  VARCHAR(255)          NULL,
    first_name VARCHAR(255)          NULL,
    last_name  VARCHAR(255)          NULL,
    e_mail     VARCHAR(255)          NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE message
    ADD CONSTRAINT FK_MESSAGE_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);