package com.dao;

import com.models.DatabaseObject;
import com.models.Test;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by piotrek on 21.11.15.
 */
@Repository
public class TestDAO extends DatabaseDAO<Test> {

    public void save(Test test) {

        getSession().save(test);
    }

    public Test saveIfNotInDB(Test test) {
        Optional<Test> testOptional = isContain(test);
        if(testOptional.isPresent()){
            return testOptional.get();
        }else{
            save(test);
            return test;
        }
    }


    public Test get(String uuid) {

        return getSession().get(com.models.Test.class, uuid);
    }

    public List<Test> getAll() {

        Query query = getSession().createQuery("from Test");
        List<Test> list = query.list();
        return list;
    }

    public Optional<Test> isContain(final Test test) {

        return getAll().stream().filter(a -> a.equals(test)).findFirst();
    }
}
