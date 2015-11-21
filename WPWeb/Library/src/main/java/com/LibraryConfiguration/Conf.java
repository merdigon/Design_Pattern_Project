package com.LibraryConfiguration;

/**
 * Created by piotrek on 21.11.15.
 */
public class Conf {
    private static int borrowedDays = 30;
    private static double interests = 0.2;

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
}
