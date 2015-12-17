package com.models;


import com.LibraryConfiguration.Conf;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table
public class UserModel extends DatabaseObject
{
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid")
        @Column(name = "uuid", unique = true)

        private String uuid;

        private int idNumber;


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


        @ManyToOne(fetch = FetchType.EAGER)
        @JoinTable(name="user_userRole",
                        joinColumns = {@JoinColumn(name="userId")},
                        inverseJoinColumns = {@JoinColumn(name="userProfileId")})
        private UserRole userRole;
        @OneToMany
        @NotFound(action = NotFoundAction.IGNORE)
        @JoinTable(name="USER_BOOK", joinColumns = @JoinColumn(name="USER_UUID"),
                inverseJoinColumns = @JoinColumn(name="BOOK_UUID"))
        private List<Book> books= new ArrayList<>();

        @OneToMany
        @NotFound(action = NotFoundAction.IGNORE)
        @JoinTable(name="USER_RESERVEDBOOK", joinColumns = @JoinColumn(name="USER_UUID"),
                inverseJoinColumns = @JoinColumn(name="RESERVEDBOOK_UUID"))
        private List<Book> reservedBooks= new ArrayList<>();

        private double debt;

        public String getUuid() {
                return uuid;
        }

        public void setUuid(String uuid) {
                this.uuid = uuid;
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


        public void removeBook(Book book){
                this.books.remove(book);
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

        public double getDebt() {
                return debt;
        }

        public void setDebt(double debt) {
                this.debt = debt;
        }

        public String getMail() {
                return mail;
        }

        public void setMail(String mail) {
                this.mail = mail;
        }

        public UserRole getUserRole() {
                return userRole;
        }

        public void setUserRole(UserRole userRole) {
                this.userRole = userRole;
        }

        public List<Book> getReservedBooks() {
                return reservedBooks;
        }

        public void setReservedBooks(List<Book> reservedBooks) {
                this.reservedBooks = reservedBooks;
        }

        public void removeReservedBook(Book book){
                this.reservedBooks.remove(book);
        }

        public void addReservedBook(Book book){
                this.reservedBooks.add(book);
        }

        public double countDebt(){
                double debt = this.debt;
                int daysOver = 0;
                LocalDate planningReturnDay;
                for(Book book: books){
                         planningReturnDay = book.getDates().get(book.getDates().size()-1).getPlanningReturnDate();
                        if(planningReturnDay.isBefore(new LocalDate())){
                                debt += Days.daysBetween(planningReturnDay, new LocalDate()).getDays() * Conf.getInterests();
                        }

                }
                return debt;
        }

        public void addDebt(double debt){
                this.debt = this.debt+debt;
        }


        @Override
        public String toString() {
                return "{" +
                        "\"uuid\":\"" + uuid  + '\"' +
                        ", \"login\":\"" + login + '\"'+
                        ", \"name\":\"" + name + '\"' +
                        ", \"surname\":\"" + surname + '\"' +
                        ", \"mail\":\"" + mail + '\"' +
                        ", \"idNumber\":\"" + idNumber + '\"' +
                        ", \"debt\":\"" + debt + '\"' +
                        ", \"books\":" + books  +
                        ", \"reservedBooks\":" + reservedBooks  +
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

        public int getIdNumber() {
                return idNumber;
        }

        public void setIdNumber(int idNumber) {
                this.idNumber = idNumber;
        }
}
