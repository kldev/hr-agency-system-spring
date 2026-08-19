CREATE VIEW job_application_view AS
SELECT
    a.id,
    a.organization_id,
    a.candidate_id,
    a.job_posting_id,
    jp.company_id,
    jp.recruiter_id,
    c.first_name AS candidate_first_name,
    c.last_name AS candidate_last_name,
    c.email AS candidate_email,
    c.phone AS candidate_phone,
    concat(u.first_name, ' ', u.last_name) as recruiter_fullname,
    a.status,
    a.source,
    a.created_at
FROM applications a
         JOIN job_postings jp ON jp.id = a.job_posting_id
         JOIN candidates c ON c.id = a.candidate_id
         JOIN users u on jp.recruiter_id = u.id