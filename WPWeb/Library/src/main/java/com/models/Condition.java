package com.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @GeneratedValue
    private int id;
    private Conditions condition;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id  + '\"' +
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