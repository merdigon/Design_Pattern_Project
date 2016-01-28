package com.dao;

import com.models.Author;

import java.util.List;
import java.util.Optional;


public interface AuthorDAO {
    public void save(Author author);
    public Author saveIfNotInDB(Author author);
    public List<Author> saveIfNotInDB(List<Author> authors);
    public Author get(String uuid);
    public List<Author> getAll();
    public List<Author> get(String name, String surname, String bornYear);
    public Optional<Author> isContain(final Author author);
}
