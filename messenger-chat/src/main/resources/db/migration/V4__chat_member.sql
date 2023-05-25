ALTER TABLE chat_member_ids
    ADD member_name varchar(255) NOT NULL DEFAULT '';

ALTER TABLE chat_member_ids
    ADD member_avatar_id UUID;