CREATE VIEW interviews_view as
SELECT
    i.id,
    i.candidate_id,
    i.application_id,
    i.organization_id,
    concat(c.first_name, ' ', c.last_name) as candidate_name,
    c.email as candidate_email,
    i.status,
    i.feedback,
    i.scheduled_at,
    i.created_at,
    i.created_by,
    i.updated_at,
    concat(u.first_name, ' ', u.last_name) as created_name
    FROM interviews i
    INNER JOIN applications a on i.application_id = a.id
    INNER JOIN candidates c on c.id = i.candidate_id
    INNER JOIN users u on i.created_by = u.id