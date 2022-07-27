DROP TABLE IF EXISTS "users";
CREATE TABLE "users" (
	"id" SERIAL NOT NULL,
	"username" TEXT NOT NULL primary key,
	"password" TEXT NOT NULL,
	refreshtoken VARCHAR(64),
    refreshtoken_iat TIMESTAMP
);

INSERT INTO "users" ("username", "password") VALUES ('zenn', 'zenn');