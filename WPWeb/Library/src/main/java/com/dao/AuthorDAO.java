package com.dao;

import com.models.Author;
import com.models.Book;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by piotrek on 07.11.15.
 */
@Repository
public class AuthorDAO extends DatabaseDAO<Author> {

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

    public Author get(int key) {

        return getSession().get(com.models.Author.class, key);
    }

    public List<Author> getAll() {

        Query query = getSession().createQuery("from Author");
        List<Author> list = query.list();
        return list;

    }

    public List<Author> findByColumn(String column, String expression) {

        return getSession().createQuery("from author where " + column + " LIKE lower('%" + expression + "%')").list();

    }

    public Optional<Author> isContain(final Author author) {

        return getAll().stream().filter(a -> a.equals(author)).findFirst();
    }


}
