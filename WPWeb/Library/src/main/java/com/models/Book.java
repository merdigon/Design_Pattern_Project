package com.models;




import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Book extends DatabaseObject{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="BOOK_AUTHOR", joinColumns = @JoinColumn(name="BOOK_ID", updatable = false, insertable = false),
            inverseJoinColumns = @JoinColumn(name="AUTHOR_ID"))
    private List<Author> authors = new ArrayList<Author>();
    private String title;
    private int year;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name="BOOK_CONDITION", joinColumns = @JoinColumn(name="BOOK_ID"),
                inverseJoinColumns = @JoinColumn(name="CONDITION_ID"))
    private Condition condition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name="BOOK_TYPE", joinColumns = @JoinColumn(name="BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name="TYPE_ID"))
    private TypeOfBook typeOfBook;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name="BOOK_SECTION", joinColumns = @JoinColumn(name="BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name="SECTION_ID"))
    private Section section;

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public TypeOfBook getTypeOfBook() {
        return typeOfBook;
    }

    public void setTypeOfBook(TypeOfBook typeOfBook) {
        this.typeOfBook = typeOfBook;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Book(List<Author> authors, String title, int year, Condition condition, TypeOfBook typeOfBook, Section section) {
        this.authors = authors;
        this.title = title;
        this.year = year;
        this.condition = condition;
        this.typeOfBook = typeOfBook;
        this.section = section;
    }

    public Book(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (year != book.year) return false;
        if (authors != null ? !authors.equals(book.authors) : book.authors != null) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;
        if (condition != null ? !condition.equals(book.condition) : book.condition != null) return false;
        if (typeOfBook != null ? !typeOfBook.equals(book.typeOfBook) : book.typeOfBook != null) return false;
        return !(section != null ? !section.equals(book.section) : book.section != null);

    }

    @Override
    public int hashCode() {
        int result = authors != null ? authors.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + year;
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (typeOfBook != null ? typeOfBook.hashCode() : 0);
        result = 31 * result + (section != null ? section.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id  + '\"' +
                ", \"authors\":" + authors +
                ", \"title\":\"" + title + '\"' +
                ", \"year\":\"" + year + '\"' +
                ", \"condition\":" + condition  +
                ", \"typeOfBook\":" + typeOfBook  +
                ", \"section\":" + section  +
                '}';
    }

}
