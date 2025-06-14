-- The TRIGGER will call a FUNCTION on every row UPDATE
DROP FUNCTION IF EXISTS notify_hero_update;

CREATE FUNCTION notify_hero_update() RETURNS trigger AS $$
BEGIN
  PERFORM pg_notify('hero_update', row_to_json(NEW)::text);
  RAISE NOTICE 'The "hero" table row with id: % updated.', NEW.id;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER on_hero_update
AFTER UPDATE ON public.hero
FOR EACH ROW EXECUTE FUNCTION notify_hero_update();

-- We can see registered triggers
SELECT trigger_name, event_object_table
FROM information_schema.triggers
WHERE event_object_table = 'hero';
