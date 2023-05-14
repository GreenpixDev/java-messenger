CREATE TABLE notification
(
    id                 UUID         NOT NULL,
    delivery_timestamp TIMESTAMP    NOT NULL,
    reading_timestamp  TIMESTAMP,
    text               VARCHAR(255) NOT NULL,
    type               INTEGER      NOT NULL,
    user_id            UUID         NOT NULL,
    CONSTRAINT pk_notification PRIMARY KEY (id)
);

