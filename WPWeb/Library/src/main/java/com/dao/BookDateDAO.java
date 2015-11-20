//package com.dao;
//
//import com.models.Book;
//import com.models.BookDate;
//import org.hibernate.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
///**
// * Created by pietrek on 17.11.15.
// */
//
//@Repository
//public class BookDateDAO extends DatabaseDAO<BookDate>{
//
//
//    @Override
//    public void save(BookDate bookDate) {
//        getSession().save(bookDate);
//    }
//
//    @Override
//    public BookDate get(int key) {
//        return getSession().get(com.models.BookDate.class, key);
//    }
//
//    @Override
//    public List<BookDate> getAll() {
//        Query query = getSession().createQuery("from BookDate");
//        List<BookDate> list = query.list();
//        return list;
//    }
//}
