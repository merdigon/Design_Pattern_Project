package com.dao;

import com.models.Book;
import com.models.UserModel;
import org.hibernate.Query;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Date;
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


    public UserModel get(int key) {

        return getSession().get(com.models.UserModel.class, key);
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
        UserModel user = getUser(login);
        user.addBook(book);
        getSession().update(user);
    }


    public void removeBook(UserModel user, Book book){
        user.removeBook(book);
        getSession().update(user);

        System.out.println(getUser(user.getLogin()));
    }

    public List<UserModel> getAll() {
        Query query = getSession().createQuery("from UserModel");
        List<UserModel> list = query.list();
        return list;
    }

    public void addDebt(UserModel user, LocalDate borrowedDate){
        int borrowedDays = Days.daysBetween(new LocalDate(), borrowedDate).getDays();
        if(borrowedDays>0){
            double debt = user.getDebt();
            user.setDebt(debt + borrowedDays * 0.20);
            getSession().update(user);
        }
    }

    public UserModel getUser(String login){
        Query query = getSession().createQuery("from UserModel where login='" + login + "'");
        return (UserModel)query.list().get(0);
    }
}