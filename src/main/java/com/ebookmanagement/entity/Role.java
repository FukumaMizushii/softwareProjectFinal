package com.ebookmanagement.entity;

/**
 * Application roles used for role-based access control.
 * Spring Security will see these prefixed as ROLE_USER / ROLE_ADMIN.
 */
public enum Role {
    USER,
    ADMIN
}
