package com.dao;

import com.models.Author;
import com.models.Book;
import com.models.BookDate;
import com.models.Condition;
import org.hibernate.Query;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by piotrek on 07.11.15.
 */
@Repository
public class BookDAO extends DatabaseDAO<Book>{


    public void save(Book book) {

        getSession().persist(book);
    }

    public Book get(int key) {

        return getSession().get(com.models.Book.class, key);
    }

    public List<Book> getAll() {

        Query query = getSession().createQuery("from Book");
        List<Book> list = query.list();
        System.out.println(list);
        return list;

    }

    //only to column type String
    public List<Book> findByColumn(String column, String expression){

        return getSession().createQuery("from Book where " + column + " LIKE lower('%" + expression +"%')").list();
    }

    public List<Book> getAllByAuthor(Author author){
        List<Book> books = new ArrayList<Book>();
        for(Book book: getAll()){
            if(book.getAuthors().stream().filter(x->x.equals(author)).findAny().isPresent())
                books.add(book);
        }

        return books;
    }

    public List<Book> getAllByYear(String year){
        return getSession().createQuery("from Book where CAST(year as text) Like '%" + year + "%'").list();
    }

    public void addDate(Book book, BookDate date){
        book.addDate(date);
        getSession().update(book);
    }

    public void addReturnDate(Book book, LocalDate date){
        book.getDates().get(book.getDates().size()-1).setReturnDate(date);
        getSession().update(book);
    }

    public List<Book> getAllByCondition(Condition condition){
        List<Book> books = new ArrayList<Book>();
        for(Book book: getAll()){
            if(book.getCondition().equals(condition))
                books.add(book);
        }

        return books;
    }
    public void changeCondition(Book book, Condition condition){
        book.setCondition(condition);
        getSession().update(book);
    }


}
