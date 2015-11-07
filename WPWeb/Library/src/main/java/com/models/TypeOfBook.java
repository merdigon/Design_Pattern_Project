package com.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Szymon on 2015-10-13.
 */
@Entity
public class TypeOfBook extends DatabaseObject
{
    @Id
    @GeneratedValue
    private int id;
    private String code;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeOfBook(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public TypeOfBook(){};

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id  + '\"' +
                ", \"code\":\"" + code + '\"' +
                ", \"name\":\"" + name + '\"' +
                '}';
    }
}
