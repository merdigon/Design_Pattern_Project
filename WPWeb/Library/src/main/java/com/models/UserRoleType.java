package com.models;

/**
 * Created by pietrek on 10.11.15.
 */
public enum UserRoleType {
    USER("USER"),
    ADMIN("ADMIN");

    private String userRoleType;

    private UserRoleType(String userRoleType){
        this.userRoleType = userRoleType;
    }

    public String getUserRoleType(){
        return userRoleType;
    }
}
