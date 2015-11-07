package com.dao;

import com.models.Condition;
import com.models.Conditions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by piotrek on 07.11.15.
 */
@Repository
public class ConditionDAO extends DatabaseDAO<Condition>{


    public void save(Condition condition) {

        getSession().save(condition);
    }


    public Condition get(int key) {

        return getSession().get(com.models.Condition.class, key);
    }

    public List<Condition> getAll() {

        Query query = getSession().createQuery("from Condition");
        List<Condition> list = query.list();
        return list;

    }

}
