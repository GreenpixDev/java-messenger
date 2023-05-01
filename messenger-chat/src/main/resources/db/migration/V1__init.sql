CREATE TABLE chat
(
    type               VARCHAR(31) NOT NULL,
    id                 UUID        NOT NULL,
    admin_user_id      UUID,
    avatar_id          UUID,
    creation_timestamp TIMESTAMP WITHOUT TIME ZONE,
    name               VARCHAR(255),
    CONSTRAINT pk_chat PRIMARY KEY (id)
);

CREATE TABLE chat_member_ids
(
    chat_id   UUID NOT NULL,
    member_id UUID,
    CONSTRAINT uc_chat_member UNIQUE (chat_id, member_id),
    CONSTRAINT fk_member_chat FOREIGN KEY (chat_id) REFERENCES chat
);

CREATE TABLE message
(
    id                 UUID         NOT NULL,
    creation_timestamp timestamp    NOT NULL,
    text               varchar(500) NOT NULL,
    chat_id            UUID         NOT NULL,
    CONSTRAINT pk_message PRIMARY KEY (id),
    CONSTRAINT fk_message_chat FOREIGN KEY (chat_id) REFERENCES chat
);

CREATE TABLE attachment
(
    id         UUID         NOT NULL,
    file_id    UUID         NOT NULL,
    file_name  varchar(255) NOT NULL,
    message_id UUID         NOT NULL,
    CONSTRAINT pk_attachment PRIMARY KEY (id),
    CONSTRAINT fk_attachment_message FOREIGN KEY (message_id) REFERENCES message
);

