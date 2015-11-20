package com.models;


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class UserModel extends DatabaseObject
{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(unique = true, nullable = false)
        private String login;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String name;


        @Column(nullable = false)
        private String surname;


        @Column(unique = true, nullable = false)
        private String mail;


        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name="user_userRole",
                        joinColumns = {@JoinColumn(name="userId")},
                        inverseJoinColumns = {@JoinColumn(name="userProfileId")})

        private Set<UserRole> userRole = new HashSet<UserRole>();
        @OneToMany
        @NotFound(action = NotFoundAction.IGNORE)
        private List<Book> books= new ArrayList<>();

//        private String code;
//

        private double debt=0;


        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public List<Book> getBooks() {
                return books;
        }

        public void setBooks(List<Book> books) {
                this.books = books;
        }

        public void addBook(Book book){
                this.books.add(book);
        }

        public boolean removeBook(Book book){

                for(int i=0; i<this.books.size(); i++){
                        if(this.books.get(i).getId() == book.getId()) {
                                this.books.remove(i);
                                return true;
                        }
                }
                return false;
        }

        public String getLogin() {
                return login;
        }

        public void setLogin(String login) {
                this.login = login;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
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

        public String getMail() {
                return mail;
        }

        public void setMail(String mail) {
                this.mail = mail;
        }

        public Set<UserRole> getUserRole() {
                return userRole;
        }

        public void setUserRole(Set<UserRole> userRole) {
                this.userRole = userRole;
        }

        public double getDebt() {
                return debt;
        }

        public void setDebt(double debt) {
                this.debt = debt;
        }

        @Override
        public String toString() {
                return "{" +
                        "\"id\":\"" + id  + '\"' +
                        ", \"login\":\"" + login + '\"'+
                        ", \"name\":\"" + name + '\"' +
                        ", \"surname\":\"" + surname + '\"' +
                        ", \"mail\":\"" + mail + '\"' +
                        ", \"books\":" + books  +
                        ", \"debt\":\"" + debt  + '\"' +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                UserModel userModel = (UserModel) o;

                return !(login != null ? !login.equals(userModel.login) : userModel.login != null);

        }

        @Override
        public int hashCode() {
                return login != null ? login.hashCode() : 0;
        }
}
