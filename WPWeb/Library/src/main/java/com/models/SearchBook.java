package com.models;

/**
 * Created by piotrek on 26.12.15.
 */
public class SearchBook {
    private Book book;
    private String searchType;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}
