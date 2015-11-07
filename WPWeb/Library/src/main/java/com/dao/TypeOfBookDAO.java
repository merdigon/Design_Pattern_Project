package com.dao;

import com.models.TypeOfBook;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by piotrek on 07.11.15.
 */
@Repository
public class TypeOfBookDAO extends DatabaseDAO<TypeOfBook> {



    public void save(TypeOfBook typeOfBook) {

        getSession().save(typeOfBook);
    }


    public TypeOfBook get(int key) {

        return getSession().get(com.models.TypeOfBook.class, key);
    }

    public List<TypeOfBook> getAll() {

        Query query = getSession().createQuery("from TypeOfBook");
        List<TypeOfBook> list = query.list();
        return list;

    }

}
