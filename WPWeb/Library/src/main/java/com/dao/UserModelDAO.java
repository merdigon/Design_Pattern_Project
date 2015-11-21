package com.dao;

import com.models.Book;
import com.models.UserModel;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by pietrek on 13.11.15.
 */
@Repository
public class UserModelDAO extends DatabaseDAO<UserModel>{


    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        getSession().save(user);
    }


    public UserModel get(String uuid) {

        return getSession().get(com.models.UserModel.class, uuid);
    }

    public UserModel getByLogin(String login){
        Query query = getSession().createQuery("from UserModel where login LIKE ?");
        query.setString(0, login);
        return (UserModel)query.list().get(0);
    }

    public void addBook(UserModel user, Book book){
            user.addBook(book);
            getSession().update(user);
    }

    public void addBook(String login, Book book){
        UserModel user = getByLogin(login);
        user.addBook(book);
        getSession().update(user);
    }


    public void removeBook(UserModel user, Book book){
        user.removeBook(book);
        getSession().update(user);
    }

    public List<UserModel> getAll() {
        Query query = getSession().createQuery("from UserModel");
        List<UserModel> list = query.list();
        return list;
    }

}