ALTER TABLE "user"
    ADD avatar_id UUID;

ALTER TABLE "user"
    ADD city VARCHAR(255);

ALTER TABLE "user"
    ADD email VARCHAR(255);

ALTER TABLE "user"
    ADD phone VARCHAR(255);

ALTER TABLE "user"
    ALTER COLUMN email SET NOT NULL;

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_email UNIQUE (email);