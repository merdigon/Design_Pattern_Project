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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "uuid", unique = true)
    private String uuid;


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

    @OneToMany
    @JoinTable(name="BOOK_DATE", joinColumns = @JoinColumn(name="BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name="DATE_ID"))
    private List<BookDate> dates = new ArrayList<BookDate>();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<BookDate> getDates() {
        return dates;
    }

    public void setDates(List<BookDate> dates) {
        this.dates = dates;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
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

    public void addDate(BookDate date){
        dates.add(date);
    }

    public Book(){}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return !(uuid != null ? !uuid.equals(book.uuid) : book.uuid != null);

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{" +
                "\"uuid\":\"" + uuid  + '\"' +
                ", \"authors\":" + authors +
                ", \"title\":\"" + title + '\"' +
                ", \"year\":\"" + year + '\"' +
                ", \"condition\":" + condition  +
                ", \"typeOfBook\":" + typeOfBook  +
                ", \"section\":" + section  +
                '}';
    }

}
