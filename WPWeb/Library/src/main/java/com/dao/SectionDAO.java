package com.dao;

import com.models.Section;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by piotrek on 07.11.15.
 */
@Repository
public class SectionDAO extends DatabaseDAO<Section>{

    public void save(Section section) {

        getSession().save(section);
    }


    public Section get(int key) {

        return getSession().get(com.models.Section.class, key);
    }

    public List<Section> getAll() {

        Query query = getSession().createQuery("from Section");
        List<Section> list = query.list();
        return list;

    }
}
