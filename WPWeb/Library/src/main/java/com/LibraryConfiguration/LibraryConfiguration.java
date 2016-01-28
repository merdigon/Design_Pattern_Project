package com.LibraryConfiguration;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by piotrek on 21.11.15.
 */
//public enum LibraryConfiguration {
//    INSTANCE;
//
//
//    private static AtomicInteger borrowedDays = new AtomicInteger(30);
//    private static AtomicReference<Double> interests = new AtomicReference<>(0.2);
//    private static AtomicInteger maxBorrowedBooks = new AtomicInteger(3);
//    private static AtomicInteger maxReservedBooks = new AtomicInteger(3);
//    private static AtomicInteger expirationSessionMinutes = new AtomicInteger(30);
//
//
//    public static LibraryConfiguration getInstance() {
//        return INSTANCE;
//    }
//
//    public int getBorrowedDays() {
//        return borrowedDays.get();
//    }
//
//    public void setBorrowedDays(int borrowedDays) {
//        this.borrowedDays.set(borrowedDays);
//    }
//
//    public double getInterests() {
//        return this.interests.get();
//    }
//
//    public void setInterests(double interests) {
//        this.interests.set(interests);
//    }
//
//    public int getMaxBorrowedBooks() {
//        return this.maxBorrowedBooks.get();
//    }
//
//    public void setMaxBorrowedBooks(int maxBorrowedBooks) {
//        this.maxBorrowedBooks.set(maxBorrowedBooks);
//    }
//
//    public int getMaxReservedBooks() {
//        return this.maxReservedBooks.get();
//    }
//
//    public void setMaxReservedBooks(int maxReservedBooks) {
//        this.maxReservedBooks.set(maxReservedBooks);
//    }
//
//    public int getExpirationSessionMinutes() {
//        return this.expirationSessionMinutes.get();
//    }
//
//    public void setExpirationSessionMinutes(int expirationSessionMinutes) {
//        this.expirationSessionMinutes.set(expirationSessionMinutes);
//    }
//
//    public String toString() {
//        return "{" +
//                "\"borrowedDays\":\"" + borrowedDays + '\"' +
//                ", \"interests\":\"" + interests + '\"' +
//                ", \"maxBorrowedBooks\":\"" + maxBorrowedBooks + '\"' +
//                ", \"maxReservedBooks\":\"" + maxReservedBooks + '\"' +
//                ", \"expirationSessionMinutes\":\"" + expirationSessionMinutes + '\"' +
//                '}';
//    }
//
//}

public class LibraryConfiguration{

    private static AtomicInteger borrowedDays = new AtomicInteger(30);
    private static AtomicReference<Double> interests = new AtomicReference<>(0.2);
    private static AtomicInteger maxBorrowedBooks = new AtomicInteger(3);
    private static AtomicInteger maxReservedBooks = new AtomicInteger(3);
    private static AtomicInteger expirationSessionMinutes = new AtomicInteger(30);

    private static LibraryConfiguration instance;
    protected LibraryConfiguration(){}

    private static class LibraryConfigurationHolder{
        private static final LibraryConfiguration instance = new LibraryConfiguration();
    }

    public static LibraryConfiguration getInstance(){
        return LibraryConfigurationHolder.instance;
    }

    public int getBorrowedDays() {
        return borrowedDays.get();
    }

    public void setBorrowedDays(int borrowedDays) {
        this.borrowedDays.set(borrowedDays);
    }

    public double getInterests() {
        return this.interests.get();
    }

    public void setInterests(double interests) {
        this.interests.set(interests);
    }

    public int getMaxBorrowedBooks() {
        return this.maxBorrowedBooks.get();
    }

    public void setMaxBorrowedBooks(int maxBorrowedBooks) {
        this.maxBorrowedBooks.set(maxBorrowedBooks);
    }

    public int getMaxReservedBooks() {
        return this.maxReservedBooks.get();
    }

    public void setMaxReservedBooks(int maxReservedBooks) {
        this.maxReservedBooks.set(maxReservedBooks);
    }

    public int getExpirationSessionMinutes() {
        return this.expirationSessionMinutes.get();
    }

    public void setExpirationSessionMinutes(int expirationSessionMinutes) {
        this.expirationSessionMinutes.set(expirationSessionMinutes);
    }

    public String toString() {
        return "{" +
                "\"borrowedDays\":\"" + borrowedDays + '\"' +
                ", \"interests\":\"" + interests + '\"' +
                ", \"maxBorrowedBooks\":\"" + maxBorrowedBooks + '\"' +
                ", \"maxReservedBooks\":\"" + maxReservedBooks + '\"' +
                ", \"expirationSessionMinutes\":\"" + expirationSessionMinutes + '\"' +
                '}';
    }
}
