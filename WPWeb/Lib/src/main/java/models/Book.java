package src.main.java.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by pietrek on 11.10.15.
 */
@Entity
public class Book {
    @Id
    @GeneratedValue
    private int id;
    private String author;
    private String title;
    private String year;

    public int getId(){
        return id;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id  + '\"' +
                ", \"author\":\"" + author + '\"' +
                ", \"title\":\"" + title + '\"' +
                ", \"year\":\"" + year + '\"' +
                '}';
    }
}
