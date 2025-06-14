-- show table content before the update
SELECT * FROM public.hero;

-- Update the row. This will trigger an event.
UPDATE public.hero
SET last_name = 'Smollett'
WHERE last_name = 'Nemo';

-- show table content after the update
SELECT * FROM public.hero;
