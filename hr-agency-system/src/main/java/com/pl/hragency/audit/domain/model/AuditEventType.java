package com.pl.hragency.audit.domain.model;


public enum AuditEventType {

    CREATED,
    UPDATED,
    DELETED,

    ASSIGNED,
    UNASSIGNED,

    STATUS_CHANGED,

    APPROVED,
    REJECTED,

    HIRED,

    TERMINATED,
    SALES_OWNER_CHANGED
}
