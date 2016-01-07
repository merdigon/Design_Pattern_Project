package com.LibraryConfiguration;

/**
 * Created by piotrek on 21.11.15.
 */
public class Conf {
    private static int borrowedDays = 30;
    private static double interests = 0.2;
    private static int maxBorrowedBooks = 3;
    private static int maxReservedBooks = 3;
    private static int expirationSessionMinutes = 30;

    public static int getBorrowedDays() {
        return borrowedDays;
    }

    public static void setBorrowedDays(int borrowedDays) {
        Conf.borrowedDays = borrowedDays;
    }

    public static double getInterests() {
        return interests;
    }


    public static void setInterests(double interests) {
        Conf.interests = interests;
    }


    public static int getMaxBorrowedBooks() {
        return maxBorrowedBooks;
    }

    public static void setMaxBorrowedBooks(int maxBorrowedBooks) {
        Conf.maxBorrowedBooks = maxBorrowedBooks;
    }

    public static int getMaxReservedBooks() {
        return maxReservedBooks;
    }

    public static void setMaxReservedBooks(int maxReservedBooks) {
        Conf.maxReservedBooks = maxReservedBooks;
    }

    public static int getExpirationSessionMinutes() {
        return expirationSessionMinutes;
    }

    public static void setExpirationSessionMinutes(int expirationSessionMinutes) {
        Conf.expirationSessionMinutes = expirationSessionMinutes;
    }

    public String toString(){
        return "{" +
                "\"borrowedDays\":\"" + borrowedDays  + '\"' +
                ", \"interests\":\"" + interests + '\"' +
                ", \"maxBorrowedBooks\":\"" + maxBorrowedBooks + '\"' +
                ", \"maxReservedBooks\":\"" + maxReservedBooks + '\"' +
                ", \"expirationSessionMinutes\":\"" + expirationSessionMinutes + '\"' +
                '}';
    }

}
