//package com.models;
//
//
//import org.hibernate.annotations.NotFound;
//import org.hibernate.annotations.NotFoundAction;
//
//import javax.persistence.*;
//import java.util.List;
//
///**
// * Created by Szymon on 2015-10-13.
// */
//@Entity
//@Table
//public class UserModel extends DatabaseObject
//{
//        @Id
//        @GeneratedValue
//        private int id;
//
//        @OneToMany
//        @NotFound(action = NotFoundAction.IGNORE)
//        private List<Book> books;
//
//        private String code;
//
//        private String login;
//
//        private String password;
//
//        private String name;
//
//        private String surname;
//
//        private double debt;
//
//        @OneToMany
//        private  List<UserRole> userRole;
//
//        public int getId() {
//                return id;
//        }
//
//        public void setId(int id) {
//                this.id = id;
//        }
//
//        public List<Book> getBooks() {
//                return books;
//        }
//
//        public void setBooks(List<Book> books) {
//                this.books = books;
//        }
//
//        public String getCode() {
//                return code;
//        }
//
//        public void setCode(String code) {
//                this.code = code;
//        }
//
//        public String getLogin() {
//                return login;
//        }
//
//        public void setLogin(String login) {
//                this.login = login;
//        }
//
//        public String getPassword() {
//                return password;
//        }
//
//        public void setPassword(String password) {
//                this.password = password;
//        }
//
//        public String getName() {
//                return name;
//        }
//
//        public void setName(String name) {
//                this.name = name;
//        }
//
//        public String getSurname() {
//                return surname;
//        }
//
//        public void setSurname(String surname) {
//                this.surname = surname;
//        }
//
//        public double getDebt() {
//                return debt;
//        }
//
//        public void setDebt(double debt) {
//                this.debt = debt;
//        }
//
//        public List<UserRole> getUserRole() {
//                return userRole;
//        }
//
//        public void setUserRole(List<UserRole> userRole) {
//                this.userRole = userRole;
//        }
//
//
//
//        @Override
//        public String toString() {
//                return "UserModel [id=" + id + ", login=" + login + ", password=" + password
//                        + ", Name=" + name + ", surname=" + surname
//                        + ", debt=" + debt + ", userRole=" + userRole +"]";
//        }
//}
