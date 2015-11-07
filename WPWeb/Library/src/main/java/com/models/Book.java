package com.models;




import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Book extends DatabaseObject{

    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    private Author author;
    private String title;
    private int year;
    @OneToOne
    private Condition condition;
    @OneToOne
    private TypeOfBook typeOfBook;
    @OneToOne
    private Section section;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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

    public Book(Author author, String title, int year, Condition condition, TypeOfBook typeOfBook, Section section) {
        this.author = author;
        this.title = title;
        this.year = year;
        this.condition = condition;
        this.typeOfBook = typeOfBook;
        this.section = section;
    }

    public Book(){}

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id  + '\"' +
                ", \"author\":\"" + author + '\"' +
                ", \"title\":\"" + title + '\"' +
                ", \"year\":\"" + year + '\"' +
                ", \"condition\":\"" + condition + '\"' +
                ", \"typeOfBook\":\"" + typeOfBook + '\"' +
                ", \"section\":\"" + section + '\"' +
                '}';
    }


}
