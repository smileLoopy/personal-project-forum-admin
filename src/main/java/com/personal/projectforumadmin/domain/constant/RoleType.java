package com.personal.projectforumadmin.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleType {

    USER("ROLE_USER", "User"),
    MANAGER("ROLE_MANAGER", "Manager"),
    DEVELOPER("ROLE_DEVELOPER", "Developer"),
    ADMIN("ROLE_ADMIN", "Admin");

    @Getter
    private final String roleName;
    @Getter
    private final String description;

}