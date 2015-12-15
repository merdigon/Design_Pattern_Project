package com.dao;

import com.configuration.CryptWithMD5;
import com.models.Book;
import com.models.UserModel;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import sun.invoke.empty.Empty;

import java.util.List;
import java.util.Optional;

/**
 * Created by pietrek on 13.11.15.
 */
@Repository
public class UserModelDAO extends DatabaseDAO<UserModel> {


    @Autowired
    CryptWithMD5 cryptWithMD5;

    public void save(UserModel user) {
        user.setPassword(cryptWithMD5.encode(user.getPassword()));
        getSession().save(user);
    }

    public boolean isValidUser(String login, String password) {
        UserModel user = getByLogin(login);
        return (user!=null && user.getPassword().equals(password));
    }


    public UserModel get(String uuid) {

        return getSession().get(com.models.UserModel.class, uuid);
    }


    public UserModel getByLogin(String login) {
        Query query = getSession().createQuery("from UserModel where login LIKE ?");
        query.setString(0, login);
        if(query.list().isEmpty())
            return null;
        query.setString(0, login);
        return (UserModel) query.list().get(0);
    }

    public void addBook(UserModel user, Book book) {
        user.addBook(book);
        getSession().update(user);
    }

    public void addReservedBook(UserModel user, Book book) {
        user.addReservedBook(book);
        getSession().update(user);
    }

    public void addBook(String login, Book book) {
        UserModel user = getByLogin(login);
        user.addBook(book);
        getSession().update(user);
    }

    public void addDebt(UserModel user, double debt) {
        user.setDebt(user.getDebt() + debt);
        getSession().update(user);
    }


    public void removeBook(UserModel user, Book book) {
        user.removeBook(book);
        getSession().update(user);
    }

    public void removeReservedBook(UserModel user, Book book) {
        user.removeReservedBook(book);
        getSession().update(user);
    }

    public List<UserModel> getAll() {
        Query query = getSession().createQuery("from UserModel");
        List<UserModel> list = query.list();
        return list;
    }


    public void update(UserModel user) {
        getSession().update(user);
    }

    public void delete(String uuid) {
        getSession().delete(get(uuid));
    }

    public boolean isLogin(String login){
        Query query = getSession().createQuery("from UserModel where login='" + login + "'");
        return !query.list().isEmpty();
    }

    public boolean isMail(String mail){
        Query query = getSession().createQuery("from UserModel where mail='" + mail + "'");
        return !query.list().isEmpty();
    }

    public UserModel getByIdNumber(int idNumber){
        Query query = getSession().createQuery("from UserModel where idNumber='" + idNumber + "'");
        if(query.list().isEmpty())
            return null;
        return (UserModel)query.list().get(0);
    }

}