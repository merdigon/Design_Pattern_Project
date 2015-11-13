package com.dao;

import com.models.UserRole;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by pietrek on 13.11.15.
 */

    @Repository
    public class UserRoleDAO extends DatabaseDAO<UserRole> {

        public void save(UserRole role) {
            getSession().save(role);
        }


        public UserRole get(int key) {

            return getSession().get(com.models.UserRole.class, key);
        }

        public List<UserRole> getAll() {

            Query query = getSession().createQuery("from UserRole");
            List<UserRole> list = query.list();
            return list;

        }



        public Optional<UserRole> isContain(final UserRole role) {

            return getAll().stream().filter(a -> a.equals(role)).findFirst();
        }



    }