CREATE TABLE users (
    id UUID NOT NULL PRIMARY KEY,
    familyid UUID NOT NULL REFERENCES families(id),
    nickname varchar(200) NOT NULL,
    role varchar(10) NOT NULL,
    birthdate date NOT NULL
);