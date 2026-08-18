CREATE OR REPLACE FUNCTION remove_polish_chars(input_text TEXT)
RETURNS TEXT
LANGUAGE plpgsql
IMMUTABLE
STRICT
AS $$
BEGIN
RETURN translate(
        input_text,
        'ąćęłńóśźżĄĆĘŁŃÓŚŹŻ',
        'acelnoszzACELNOSZZ'
       );
END;
$$;

CREATE OR REPLACE FUNCTION generate_slug(input_text TEXT)
RETURNS TEXT
LANGUAGE sql
IMMUTABLE
STRICT
AS $$
SELECT regexp_replace(
               regexp_replace(
                       lower(remove_polish_chars(input_text)),
                       '[^a-z0-9]+',
                       '-',
                       'g'
               ),
               '(^-|-$)',
               '',
               'g'
       );
$$;