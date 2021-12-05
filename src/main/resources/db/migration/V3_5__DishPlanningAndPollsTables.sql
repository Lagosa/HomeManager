CREATE TABLE dishPolls (
    id SERIAL PRIMARY KEY ,
    family UUID NOT NULL REFERENCES families(id),
    message varchar(300),
    status varchar(10) NOT NULL DEFAULT 'OPEN' CHECK (status = 'OPEN' OR status = 'CLOSED'),
    lastUpdated DATE NOT NULL DEFAULT NOW()
);

CREATE TABLE dishPolls_dish (
    id SERIAL PRIMARY KEY ,
    poll int NOT NULL REFERENCES dishPolls(id),
    dish int NOT NULL REFERENCES dishes(id)
);

CREATE TABLE dishPoll_dish_user (
    id SERIAL PRIMARY KEY ,
    dish int NOT NULL REFERENCES dishPolls_dish(id),
    userId UUID NOT NULL REFERENCES users(id),
    vote varchar(10) NOT NULL CHECK (vote = 'FOR' OR vote = 'AGAINST')
);



