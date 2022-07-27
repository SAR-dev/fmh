DROP TABLE IF EXISTS "users";
DROP TABLE IF EXISTS "refreshTokens";

CREATE TABLE "users" (
	"id" SERIAL NOT NULL,
	"username" TEXT NOT NULL primary key,
	"password" TEXT NOT NULL,
	"coin" INTEGER NOT NULL
);

CREATE TABLE "refreshTokens" (
	"id" SERIAL NOT NULL,
	"user_id" INTEGER NOT NULL primary key,
	"token" TEXT NOT NULL,
	"expiry_date" TIMESTAMP NOT NULL
);

INSERT INTO "users" ("username", "password", "coin") VALUES ('zenn', 'zenn', '900');