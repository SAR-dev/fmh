DROP TABLE IF EXISTS "users";

CREATE TABLE "users" (
	"id" SERIAL NOT NULL,
	"username" TEXT NOT NULL primary key,
	"password" TEXT NOT NULL,
	"coin" INTEGER NOT NULL
);

INSERT INTO "users" ("username", "password", "coin") VALUES ('zenn', 'zenn', '900');