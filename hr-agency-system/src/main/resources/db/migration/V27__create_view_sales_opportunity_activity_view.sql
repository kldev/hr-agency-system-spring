CREATE VIEW sales_opportunity_activities_view as
SELECT
    a.id,
    a.organization_id,
    a.sales_opportunity_id,
    a.type,
    a.note,
    a.occurred_at,
    a.created_at,
    a.created_by,
    concat(b.first_name, ' ', b.last_name) as created_by_full_name
FROM sales_opportunity_activities a
    INNER JOIN users b on a.created_by = b.id