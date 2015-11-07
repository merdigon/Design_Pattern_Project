package com.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Szymon on 2015-10-13.
 */
@Entity
public class Author extends DatabaseObject
{
        @Id
        @GeneratedValue
        private int id;

        public String name;

        public String surname;

        public int bornYear;

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

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

        public Author(){};

        @Override
        public String toString() {
                return "{" +
                        "\"id\":\"" + id  + '\"' +
                        ", \"author\":\"" + name + '\"' +
                        ", \"title\":\"" + surname + '\"' +
                        ", \"year\":\"" + bornYear + '\"' +
                        '}';
        }
}
