package com.dao;

import com.models.Condition;
import com.models.Conditions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by piotrek on 07.11.15.
 */
@Repository
public class ConditionDAO extends DatabaseDAO<Condition>{


    public void save(Condition condition) {

        getSession().save(condition);
    }

    public Condition saveIfNotInDB(Condition condition) {
        Optional<Condition> conditionOptional = isContain(condition);
        if(conditionOptional.isPresent()){
            return conditionOptional.get();
        }else{
            save(condition);
            return condition;
        }
    }


    public Condition get(int key) {

        return getSession().get(com.models.Condition.class, key);
    }

    public List<Condition> getAll() {

        Query query = getSession().createQuery("from Condition");
        List<Condition> list = query.list();
        return list;
    }

    public Optional<Condition> isContain(final Condition condition) {

        return getAll().stream().filter(a -> a.equals(condition)).findFirst();
    }

}
