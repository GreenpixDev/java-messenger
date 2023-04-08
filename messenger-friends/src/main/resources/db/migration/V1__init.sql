CREATE TABLE blocked_user
(
    blocked_user_full_name  VARCHAR(255)           NOT NULL,
    addition_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deletion_timestamp TIMESTAMP WITHOUT TIME ZONE,
    blocked_user_id    UUID                        NOT NULL,
    target_user_id     UUID                        NOT NULL,
    CONSTRAINT pk_blacklist PRIMARY KEY (blocked_user_id, target_user_id)
);

CREATE TABLE friend
(
    friend_full_name   VARCHAR(255)                NOT NULL,
    addition_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deletion_timestamp TIMESTAMP WITHOUT TIME ZONE,
    friend_user_id     UUID                        NOT NULL,
    target_user_id     UUID                        NOT NULL,
    CONSTRAINT pk_friend PRIMARY KEY (friend_user_id, target_user_id)
);