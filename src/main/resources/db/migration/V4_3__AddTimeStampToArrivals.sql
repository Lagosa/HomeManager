ALTER TABLE arrivals
ADD COLUMN timestampCol TIMESTAMP NOT NULL DEFAULT now();

SET timezone = 'Europe/Bucharest';