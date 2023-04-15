ALTER TABLE friend
    ALTER COLUMN addition_timestamp TYPE date;

ALTER TABLE friend
    ALTER COLUMN deletion_timestamp TYPE date;

ALTER TABLE blocked_user
    ALTER COLUMN addition_timestamp TYPE date;

ALTER TABLE blocked_user
    ALTER COLUMN deletion_timestamp TYPE date;

ALTER TABLE friend
    RENAME COLUMN addition_timestamp TO addition_date;

ALTER TABLE friend
    RENAME COLUMN deletion_timestamp TO deletion_date;

ALTER TABLE blocked_user
    RENAME COLUMN addition_timestamp TO addition_date;

ALTER TABLE blocked_user
    RENAME COLUMN deletion_timestamp TO deletion_date;