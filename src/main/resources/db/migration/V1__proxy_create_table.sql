CREATE TABLE proxy
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(120) UNIQUE NOT NULL,
    type     VARCHAR(255) NOT NULL,
    hostname VARCHAR(120) UNIQUE NOT NULL,
    port     INTEGER,
    username VARCHAR(80),
    password VARCHAR(80),
    active   BOOLEAN
);