package com.dao;

import com.models.TypeOfBook;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by piotrek on 07.11.15.
 */
@Repository
public class TypeOfBookDAO extends DatabaseDAO<TypeOfBook> {



    public void save(TypeOfBook typeOfBook) {

        getSession().save(typeOfBook);
    }

    public TypeOfBook saveIfNotInDB(TypeOfBook type) {
        Optional<TypeOfBook> typeOptional = isContain(type);
        if(typeOptional.isPresent()){
            return typeOptional.get();
        }else{
            save(type);
            return type;
        }
    }


    public TypeOfBook get(String uuid) {

        return getSession().get(com.models.TypeOfBook.class, uuid);
    }

    public List<TypeOfBook> getAll() {

        Query query = getSession().createQuery("from TypeOfBook");
        List<TypeOfBook> list = query.list();
        return list;

    }

    public Optional<TypeOfBook> isContain(final TypeOfBook type) {

        return getAll().stream().filter(a -> a.equals(type)).findFirst();
    }


}
