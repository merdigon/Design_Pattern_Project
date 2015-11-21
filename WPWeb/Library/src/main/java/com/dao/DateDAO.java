package com.dao;

import com.models.Book;
import com.models.BookDate;
import org.hibernate.Query;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by pietrek on 18.11.15.
 */
@Repository
public class DateDAO extends DatabaseDAO<BookDate>{


    public void save(BookDate date) {

        getSession().save(date);
    }

    public BookDate saveIfNotInDB(BookDate date) {
        Optional<BookDate> dateOptional = isContain(date);
        if(dateOptional.isPresent()){
            return dateOptional.get();
        }else{
            save(date);
            return date;
        }
    }

    public List<BookDate> saveIfNotInDB(List<BookDate> dates) {
        List<BookDate> dates1 = new ArrayList<>();

        for(BookDate date: dates){
            dates1.add(saveIfNotInDB(date));
        }

        return dates1;
    }

    public void addReturnDate(BookDate bookDate, LocalDate date ){
        bookDate.setReturnDate(date);
        getSession().update(bookDate);
    }


    public BookDate get(String uuid) {

        return getSession().get(com.models.BookDate.class, uuid);
    }

    public List<BookDate> getAll() {

        Query query = getSession().createQuery("from BookDate");
        List<BookDate> list = query.list();
        return list;
    }

    public Optional<BookDate> isContain(final BookDate date) {

        return getAll().stream().filter(a -> a.equals(date)).findFirst();
    }

    public void updateDate(BookDate date){
        getSession().update(date);
    }



}
