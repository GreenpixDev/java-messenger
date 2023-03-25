CREATE TABLE "user"
(
    id                     UUID         NOT NULL,
    username               VARCHAR(255) NOT NULL,
    full_name              VARCHAR(255) NOT NULL,
    hashed_password        VARCHAR(255) NOT NULL,
    registration_timestamp TIMESTAMP WITHOUT TIME ZONE,
    birth_date             date         NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_username UNIQUE (username);