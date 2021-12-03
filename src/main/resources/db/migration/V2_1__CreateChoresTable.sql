CREATE TABLE choreTypes(
    id SERIAL PRIMARY KEY ,
    type varchar(100) NOT NULL UNIQUE
);

INSERT INTO choreTypes (type) VALUES ('food');
INSERT INTO choreTypes (type) VALUES ('shop');
INSERT INTO choreTypes (type) VALUES ('wash');
INSERT INTO choreTypes (type) VALUES ('clean');
INSERT INTO choreTypes (type) VALUES ('repair');
INSERT INTO choreTypes (type) VALUES ('pet');
INSERT INTO choreTypes (type) VALUES ('other');

CREATE TABLE chores (
    id SERIAL PRIMARY KEY,
    family UUID NOT NULL REFERENCES families(id),
    submittedBy UUID NOT NULL REFERENCES users(id),
    doneBy UUID REFERENCES users(id),
    status varchar(10) NOT NULL DEFAULT 'NOT_DONE' CHECK (status = 'NOT_DONE' OR status = 'DONE' OR status = 'OVERDUE'),
    submissionDate date NOT NULL DEFAULT CURRENT_DATE,
    deadline date CHECK (deadline >= CURRENT_DATE),
    type int NOT NULL REFERENCES choreTypes(id),
    description text
);