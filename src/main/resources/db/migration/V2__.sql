ALTER TABLE message
    ADD is_public BIT(1) NULL;

UPDATE message
    SET is_public = 0
    WHERE is_public IS NULL;

ALTER TABLE message
    MODIFY is_public BIT (1) NOT NULL;