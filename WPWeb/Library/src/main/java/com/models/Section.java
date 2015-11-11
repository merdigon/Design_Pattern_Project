package com.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 2015-10-13.
 */
@Entity
public class Section extends DatabaseObject
{
    @Id
    @GeneratedValue
    private int id;
    private String code;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

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

    public Section(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Section(){}

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id  + '\"' +
                ", \"code\":\"" + code + '\"' +
                ", \"name\":\"" + name + '\"' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (code != null ? !code.equals(section.code) : section.code != null) return false;
        return !(name != null ? !name.equals(section.name) : section.name != null);

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
