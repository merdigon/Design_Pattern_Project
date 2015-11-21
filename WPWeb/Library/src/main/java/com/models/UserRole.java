package com.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by pietrek on 10.11.15.
 */
@Entity
@Table
public class UserRole {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "uuid", unique = true)
    private String uuid;

    private String type = UserRoleType.USER.getUserRoleType();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    @Override
    public String toString() {
        return "UserProfile [uuid=" + uuid + ",  type=" + type  + "]";
    }


}
