CREATE TABLE dishPlans(
    id SERIAL PRIMARY KEY ,
    family UUID NOT NULL REFERENCES families(id),
    dish int NOT NULL REFERENCES dishes(id),
    day DATE NOT NULL
);