CREATE TABLE notifications (
    id SERIAL PRIMARY KEY ,
    sender UUID NOT NULL REFERENCES users(id),
    receiver UUID NOT NULL REFERENCES users(id),
    title varchar(200) NOT NULL,
    messsage varchar(300) NOT NULl,
    dateSent DATE NOT NULL DEFAULT CURRENT_DATE,
    status varchar(10) NOT NULL DEFAULT 'SENT' CHECK(status = 'SENT' OR status = 'SEEN')
);

CREATE TABLE arrivals (
    id SERIAL PRIMARY KEY ,
    family UUID NOT NULL REFERENCES families(id),
    userId UUID NOT NULL REFERENCES users(id),
    status varchar(10) NOT NULL CHECK(status = 'ARRIVED' OR status = 'LEFT'),
    date DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE mementos (
    id SERIAL PRIMARY KEY ,
    family UUID NOT NULL REFERENCES families(id),
    title varchar(300) NOT NULL,
    dueDate DATE NOT NULL,
    status varchar(10) NOT NULL DEFAULT 'NOT_DONE' CHECK(status = 'NOT_DONE' OR status = 'DONE' OR status = 'OVERDUE')
);