package com.example.authenservice.domain.utils;

public enum  RoleEnum {
    ADMIN(1, "ADMIN") , MEMBER(2, "MEMBER");
    private int roleId;
    private String roleName;

    RoleEnum(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
