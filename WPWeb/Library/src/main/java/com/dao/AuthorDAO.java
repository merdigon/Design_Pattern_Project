package com.dao;

import com.models.Author;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * Created by piotrek on 07.11.15.
 */
@Repository
public class AuthorDAO extends DatabaseDAO<Author> {

    public void save(Author author) {

        getSession().save(author);

    }


    public Author get(int key) {

        return getSession().get(com.models.Author.class, key);
    }

    public List<Author> getAll() {

        Query query = getSession().createQuery("from Author");
        List<Author> list = query.list();
        return list;

    }


}
