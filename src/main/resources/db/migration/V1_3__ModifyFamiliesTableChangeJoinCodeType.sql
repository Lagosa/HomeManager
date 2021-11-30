ALTER TABLE families
ALTER COLUMN joincode TYPE int USING (joincode::integer);