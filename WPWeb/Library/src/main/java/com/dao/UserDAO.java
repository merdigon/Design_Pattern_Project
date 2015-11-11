//package com.dao;
//
//import com.models.UserModel;
//import org.hibernate.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
///**
// * Created by pietrek on 10.11.15.
// */
//@Repository
//public class UserDAO extends DatabaseDAO<UserModel> {
//
//    public void save(UserModel userModel) {
//
//        getSession().save(userModel);
//
//    }
//
//    public UserModel get(int key) {
//
//        return getSession().get(UserModel.class, key);
//    }
//
//    public List<UserModel> getAll() {
//
//        Query query = getSession().createQuery("from User");
//        List<UserModel> list = query.list();
//        return list;
//
//    }
//
//    public List<UserModel> findByLogin(String login){
//        return getSession().createQuery("from User where login LIKE lower(" + login +")").list();
//    }
//
//    public List<UserModel> findByColumn(String column, String expression){
//
//        return getSession().createQuery("from User where " + column + " LIKE lower('%" + expression +"%')").list();
//
//    }
//
//
//}
