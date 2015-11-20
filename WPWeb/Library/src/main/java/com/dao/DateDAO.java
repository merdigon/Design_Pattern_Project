package com.dao;

import com.models.BookDate;
import org.hibernate.Query;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

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

    public void addReturnDate(BookDate bookDate, LocalDate date ){
        bookDate.setReturnDate(date);
        getSession().update(bookDate);
    }


    public BookDate get(int key) {

        return getSession().get(com.models.BookDate.class, key);
    }

    public List<BookDate> getAll() {

        Query query = getSession().createQuery("from BookDate");
        List<BookDate> list = query.list();
        return list;
    }

    public Optional<BookDate> isContain(final BookDate date) {

        return getAll().stream().filter(a -> a.equals(date)).findFirst();
    }

}
