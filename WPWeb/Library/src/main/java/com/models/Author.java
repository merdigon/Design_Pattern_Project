package com.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Szymon on 2015-10-13.
 */
@Entity
public class Author extends DatabaseObject {
        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private int id;

        private String name;

        private String surname;

        private int bornYear;

//        @ManyToMany(fetch = FetchType.EAGER)
//        private List<Book> books = new ArrayList<>();

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

//        public List<Book> getBooks() {
//                return books;
//        }
//
//        public void setBooks(List<Book> books) {
//                this.books = books;
//        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getSurname() {
                return surname;
        }

        public void setSurname(String surname) {
                this.surname = surname;
        }

        public int getBornYear() {
                return bornYear;
        }

        public void setBornYear(int deathYear) {
                this.bornYear = deathYear;
        }

        public Author(String name, String surname, int bornYear) {
                this.name = name;
                this.surname = surname;
                this.bornYear = bornYear;
        }

        public Author() {
        }



        @Override
        public String toString() {
                return "{" +
                        "\"id\":\"" + id + '\"' +
                        ", \"name\":\"" + name + '\"' +
                        ", \"surname\":\"" + surname + '\"' +
                        ", \"bornYear\":\"" + bornYear + '\"' +
//                        ", \"books\":" + books +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Author author = (Author) o;

                if (bornYear != author.bornYear) return false;
                if (name != null ? !name.equals(author.name) : author.name != null) return false;
                return !(surname != null ? !surname.equals(author.surname) : author.surname != null);

        }

        @Override
        public int hashCode() {
                int result = name != null ? name.hashCode() : 0;
                result = 31 * result + (surname != null ? surname.hashCode() : 0);
                result = 31 * result + bornYear;
                return result;
        }
}
