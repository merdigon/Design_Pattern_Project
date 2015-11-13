package com.dao;

import com.models.UserModel;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by pietrek on 13.11.15.
 */
@Repository
public class UserModelDAO extends DatabaseDAO<UserModel>{

    public void save(UserModel user) {
        getSession().save(user);
    }


    public UserModel get(int key) {

        return getSession().get(com.models.UserModel.class, key);
    }

    public UserModel getByLogin(String login){
        Query query = getSession().createQuery("from UserModel where login LIKE ?");
        query.setString(0, login);
        return (UserModel)query.list().get(0);
    }

    public List<UserModel> getAll() {

        Query query = getSession().createQuery("from UserModel");
        List<UserModel> list = query.list();
        return list;

    }
}