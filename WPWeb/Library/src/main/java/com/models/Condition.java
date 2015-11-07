package com.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Szymon on 2015-10-13.
 */
@Entity
public class Condition extends DatabaseObject
{
    @Id
    @GeneratedValue
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Condition(Conditions condition) {
        this.condition = condition;
    }
    public Condition(){}

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id  + '\"' +
                ", \"Condition\":\"" + condition.name() + '\"' + +
                '}';
    }

}