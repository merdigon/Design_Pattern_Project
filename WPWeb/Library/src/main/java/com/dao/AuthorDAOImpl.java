package com.dao;

import com.models.Author;
import com.models.Book;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
public class AuthorDAOImpl extends DatabaseDAO<Author> implements AuthorDAO{

    public void save(Author author) {
        getSession().save(author);
    }

    public Author saveIfNotInDB(Author author) {
        Optional<Author> authorOptional = isContain(author);
        if(authorOptional.isPresent()){
            return authorOptional.get();
        }else{
            save(author);
            return author;
        }
    }

    public List<Author> saveIfNotInDB(List<Author> authors) {
        List<Author> authors1 = new ArrayList<>();

        for(Author author: authors){
            authors1.add(saveIfNotInDB(author));
        }
        return authors1;
    }

    public Author get(String uuid) {

        return getSession().get(com.models.Author.class, uuid);
    }

    public List<Author> getAll() {

        Query query = getSession().createQuery("from Author");
        List<Author> list = query.list();
        return list;

    }

    public List<Author> get(String name, String surname, String bornYear) {

        Query query = getSession().createQuery("from Author where name LIKE lower(?) and surname LIKE lower(?) and CAST(bornYear as text) like lower(?)");
        query.setString(0, "%" + name + "%");
        query.setString(1, "%" + surname + "%");
        query.setString(2, "%" + bornYear + "%");
        return query.list();

    }

    public Optional<Author> isContain(final Author author) {

        return getAll().stream().filter(a -> a.equals(author)).findFirst();
    }
}
