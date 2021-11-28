CREATE TABLE families (
    id UUID NOT NULL PRIMARY KEY,
    email varchar(200) NOT NULL UNIQUE,
    password varchar(50) NOT NULL,
    joinCode varchar(50)
);