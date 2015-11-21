package com.dao;

import com.models.Section;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by piotrek on 07.11.15.
 */
@Repository
public class SectionDAO extends DatabaseDAO<Section>{

    public void save(Section section) {

        getSession().save(section);
    }

    public Section saveIfNotInDB(Section section) {
        Optional<Section> sectionOptional = isContain(section);
        if(sectionOptional.isPresent()){
            return sectionOptional.get();
        }else{
            save(section);
            return section;
        }
    }


    public Section get(String uuid) {

        return getSession().get(com.models.Section.class, uuid);
    }

    public List<Section> getAll() {

        Query query = getSession().createQuery("from Section");
        List<Section> list = query.list();
        return list;

    }

    public Optional<Section> isContain(final Section section) {

        return getAll().stream().filter(a -> a.equals(section)).findFirst();
    }

    public boolean isExist(Section section){
        return getSession().contains(section);
    }
}
