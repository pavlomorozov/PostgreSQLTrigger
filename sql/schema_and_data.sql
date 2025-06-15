-- Create new table with initial data. If table already exists - drop it first.
DROP TABLE IF EXISTS public.hero;

CREATE TABLE public.hero (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL
);

INSERT INTO public.hero (first_name, last_name) VALUES
('Jim', 'Hawkins'),
('Captain', 'Nemo'),
('Phileas', 'Fogg'),
('Huckleberry', 'Finn'),
('Edmond', 'Dantès'),
('Robinson', 'Crusoe');