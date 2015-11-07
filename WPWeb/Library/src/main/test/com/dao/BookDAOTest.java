package com.dao;

import com.configuration.SpringConfiguration;
import com.models.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.*;

/**
 * Created by piotrek on 07.11.15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class BookDAOTest {

    @Autowired
    private BookDAO bookDAO;

    @Test
    public void addBook() {
        Book book = new Book();
        book.setYear(1234);
        book.setTitle("1234");
        bookDAO.save(book);
    }
}