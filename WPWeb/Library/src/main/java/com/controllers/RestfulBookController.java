package com.controllers;


import com.controllers.BaseController;
import com.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotrek on 21.11.15.
 */
@RestController
public class RestfulBookController extends BaseController {





//    //-------------------Retrieve All Books--------------------------------------------------------
//
    @RequestMapping(value = "/rest/getBooks/", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> listAllUsers() {
        List<Book> books = bookDAO.getAll();
        if (books.isEmpty()) {
            return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }
//
//    //--------------------- Retrieve single book by uuid
//
    @RequestMapping(value = "/rest/getBook/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getUser(@PathVariable("uuid") String uuid) {
        Book book = bookDAO.get(uuid);
        if (book == null) {
            System.out.println("book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }
//
//    //-------------------Create a Book--------------------------------------------------------
//
    @RequestMapping(value = "/rest/book/", method = RequestMethod.POST)
    public ResponseEntity<Void> createBook(@RequestBody Book book, UriComponentsBuilder ucBuilder) {

        if (bookDAO.isExist(book)) {
            System.out.println("A Book " + book.getTitle() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        book.setAuthors(authorDAO.saveIfNotInDB(book.getAuthors()));
        book.setCondition(conditionDAO.saveIfNotInDB(book.getCondition()));
        book.setTypeOfBook(typeOfBookDAO.saveIfNotInDB(book.getTypeOfBook()));
        book.setSection(sectionDAO.saveIfNotInDB(book.getSection()));
        book.setDates(dateDAO.saveIfNotInDB(book.getDates()));

        bookDAO.save(book);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/book/{uuid}").buildAndExpand(book.getUuid()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //-------------------Update a Book--------------------------------------------------------

    @RequestMapping(value = "/rest/book/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<Book> updateBook(@PathVariable("uuid") String uuid, @RequestBody Book book) {

        Book currentBook = bookDAO.get(uuid);

        if (currentBook == null) {
            System.out.println("Book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }

        book.setAuthors(authorDAO.saveIfNotInDB(book.getAuthors()));
        book.setCondition(conditionDAO.saveIfNotInDB(book.getCondition()));
        book.setTypeOfBook(typeOfBookDAO.saveIfNotInDB(book.getTypeOfBook()));
        book.setSection(sectionDAO.saveIfNotInDB(book.getSection()));
        book.setDates(dateDAO.saveIfNotInDB(book.getDates()));

        currentBook.setCondition(book.getCondition());
        currentBook.setAuthors(book.getAuthors());
        currentBook.setTitle(book.getTitle());
        currentBook.setYear(book.getYear());
        currentBook.setTypeOfBook(book.getTypeOfBook());
        currentBook.setDates(book.getDates());
        bookDAO.update(currentBook);
        return new ResponseEntity<Book>(currentBook, HttpStatus.OK);
    }

    //-------------------Delete a Book--------------------------------------------------------

    @RequestMapping(value = "/rest/deleteBook/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<Book> deleteBook(@PathVariable("uuid") String uuid) {

        Book book = bookDAO.get(uuid);
        if (book == null) {
            System.out.println("Unable to delete. Book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }

        bookDAO.delete(uuid);
        return new ResponseEntity<Book>(HttpStatus.NO_CONTENT);
    }

    //-------------------Move Book--------------------------------------------------------

    @RequestMapping(value = "/rest/moveBook/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<Book> moveBook(@PathVariable("uuid") String uuid, @RequestBody Section section) {

        Book currentBook = bookDAO.get(uuid);

        if (currentBook == null) {
            System.out.println("Book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }

        sectionDAO.saveIfNotInDB(section);
        currentBook.setSection(section);
        bookDAO.update(currentBook);
        return new ResponseEntity<Book>(currentBook, HttpStatus.OK);
    }

    //----------------------------------------reserve Book---------------------------------------------------------------------

    @RequestMapping(value = "/rest/book/{bookUuid}/{userUuid}", method = RequestMethod.GET)
    public ResponseEntity<Book> reserveBook(@PathVariable("bookUuid") String bookUuid,
                                            @PathVariable("userUuid") String userUuid) {

        Book book = bookDAO.get(bookUuid);
        UserModel user = userModelDAO.get(userUuid);
        if (book == null) {
            System.out.println("Book with uuid " + bookUuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }

        if (user == null) {
            System.out.println("user with uuid " + userUuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }

        if(!book.getCondition().equals(Conditions.valueOf("Available")))
            return  new ResponseEntity<Book>(HttpStatus.NOT_FOUND);

        Condition condition = new Condition(Conditions.valueOf("Reserved"));
        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        bookDAO.update(book);
        userModelDAO.addReservedBook(user, book);

        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

}