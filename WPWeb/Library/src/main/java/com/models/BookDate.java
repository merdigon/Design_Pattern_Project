package com.models;

import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table
public class BookDate {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private LocalDate borrowedDate;
    private LocalDate planningReturnDate;
    private LocalDate returnDate;

    private String login;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDate getPlanningReturnDate() {
        return planningReturnDate;
    }

    public void setPlanningReturnDate(LocalDate planningReturnDate) {
        this.planningReturnDate = planningReturnDate;
    }
}
