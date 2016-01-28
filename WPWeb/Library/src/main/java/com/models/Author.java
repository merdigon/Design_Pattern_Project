package com.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;


@Entity
public class Author extends DatabaseObject {
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid")
        @Column(name = "uuid", unique = true)
        private String uuid;

        private String name;

        private String surname;

        private int bornYear;

        public String getUuid() {
                return uuid;
        }

        public void setUuid(String uuid) {
                this.uuid = uuid;
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

        public Author() {
        }



        @Override
        public String toString() {
                return "{" +
                        "\"uuid\":\"" + uuid + '\"' +
                        ", \"name\":\"" + name + '\"' +
                        ", \"surname\":\"" + surname + '\"' +
                        ", \"bornYear\":\"" + bornYear + '\"' +
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
