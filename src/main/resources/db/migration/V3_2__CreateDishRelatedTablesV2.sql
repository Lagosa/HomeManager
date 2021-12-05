CREATE TABLE dishTypes(
    id SERIAL PRIMARY KEY ,
    type varchar(100) NOT NULL UNIQUE
);

INSERT INTO dishTypes (type) VALUES ('meal');
INSERT INTO dishTypes (type) VALUES ('appetizer');
INSERT INTO dishTypes (type) VALUES ('side');
INSERT INTO dishTypes (type) VALUES ('breakfast');
INSERT INTO dishTypes (type) VALUES ('smoothie');
INSERT INTO dishTypes (type) VALUES ('snack');
INSERT INTO dishTypes (type) VALUES ('soup');
INSERT INTO dishTypes (type) VALUES ('salad');
INSERT INTO dishTypes (type) VALUES ('dessert');

CREATE TABLE ingredients (
    id SERIAL PRIMARY KEY ,
    ingredient varchar(300) NOT NULL UNIQUE ,
    measurementUnit varchar(50) NOT NULL
);

CREATE TABLE dishes (
    id SERIAL PRIMARY KEY ,
    familyId UUID NOT NULL REFERENCES families(id),
    submitter UUID NOT NULL REFERENCES users(id),
    name varchar(300) NOT NULL,
    type int NOT NULL REFERENCES dishTypes(id),
    recipe text NOT NULL,
    visibility varchar(50) NOT NULL
);

CREATE TABLE dish_ingredients (
    id SERIAL PRIMARY KEY ,
    dish int NOT NULL REFERENCES dishes(id),
    ingredient int NOT NULL REFERENCES ingredients(id),
    quantity varchar(10) NOT NULL
);



