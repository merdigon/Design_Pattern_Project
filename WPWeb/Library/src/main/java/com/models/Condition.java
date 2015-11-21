package com.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 2015-10-13.
 */
@Entity
public class Condition extends DatabaseObject
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "uuid", unique = true)
    private String uuid;

    private Conditions condition;

    public String ToString()
    {
        return String.valueOf(condition);
    }

    public boolean equals(Condition cond)
    {
        return condition.equals(cond.condition);
    }


    public boolean equals(Conditions conds)
    {
        return condition.equals(conds);
    }

    public Conditions getCondition() {
        return condition;
    }

    public void setCondition(Conditions condition) {
        this.condition = condition;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Condition(Conditions condition) {
        this.condition = condition;
    }
    public Condition(){}

    @Override
    public String toString() {
        return "{" +
                "\"uuid\":\"" + uuid  + '\"' +
                ", \"Condition\":\"" + condition.name() + '\"' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Condition condition1 = (Condition) o;

        return condition == condition1.condition;

    }

    @Override
    public int hashCode() {
        return condition != null ? condition.hashCode() : 0;
    }
}