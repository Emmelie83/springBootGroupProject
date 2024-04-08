ALTER TABLE message
    ADD created_date date NULL;

ALTER TABLE message
    ADD last_modified_by VARCHAR(255) NULL;

ALTER TABLE message
    ADD last_modified_date date NULL;

ALTER TABLE message
    DROP COLUMN date;