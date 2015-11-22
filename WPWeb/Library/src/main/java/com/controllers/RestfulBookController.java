package com.controllers;


import com.LibraryConfiguration.Conf;
import com.controllers.BaseController;
import com.models.*;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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



//    //-------------------Retrieve All Books-------------------------------------------------------- Ok
//
    @RequestMapping(value = "/rest/getBooks", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> listAllbooks() {
        List<Book> books = bookDAO.getAll();
        if (books.isEmpty()) {
            return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }

    //    //-------------------Condition of Books-------------------------------------------------------- Ok
//
    @RequestMapping(value = "/rest/getCondition/{bookUuid}", method = RequestMethod.GET)
    public ResponseEntity<Condition> condition(@PathVariable("bookUuid") String bookUuid) {
        Book book = bookDAO.get(bookUuid);
        if (book==null) {
            return new ResponseEntity<Condition>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Condition>(book.getCondition(), HttpStatus.OK);
    }
//
//    //--------------------- get book by uuid -----------------Ok
//
    @RequestMapping(value = "/rest/getBook/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBook(@PathVariable("uuid") String uuid) {
        Book book = bookDAO.get(uuid);
        if (book == null) {
            System.out.println("book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }
//
//    //-------------------Add Book--------------------------------------------------------   Ok
//
    @RequestMapping(value = "/rest/addBook", method = RequestMethod.POST)
    public ResponseEntity<Void> addBook(@RequestBody Book book, UriComponentsBuilder ucBuilder) {

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

    //-------------------Update Book-------------------------------------------------------- Ok

    @RequestMapping(value = "/rest/updateBook/{uuid}", method = RequestMethod.PUT)
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

    //-------------------Delete a Book-------------------------------------------------------- Ok

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

    //-------------------Move Book to other section-------------------------------------------------------- Ok

    @RequestMapping(value = "/rest/moveBook/{uuid}/{section}", method = RequestMethod.PUT)
    public ResponseEntity<Book> moveBook(@PathVariable("uuid") String uuid,
                                         @PathVariable("section") String section) {

        Book currentBook = bookDAO.get(uuid);

        Section section1 = new Section();
        section1.setName(section);
        if (currentBook == null) {
            System.out.println("Book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }

        section1 = sectionDAO.saveIfNotInDB(section1);
        currentBook.setSection(section1);
        bookDAO.update(currentBook);
        return new ResponseEntity<Book>(currentBook, HttpStatus.OK);
    }

    //--------------------------Change condition of book------------------------- Ok

    @RequestMapping(value = "/rest/changeCondition/{uuid}/{condition}", method = RequestMethod.PUT)
    public ResponseEntity<Book> changeCondition(@PathVariable("uuid") String uuid,
                                         @PathVariable("condition") String condition) {

        Book book = bookDAO.get(uuid);

        if (book == null) {
            System.out.println("Book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }

        Condition cond = conditionDAO.saveIfNotInDB(new Condition(Conditions.valueOf(condition)));

        book.setCondition(cond);
        bookDAO.update(book);
        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    //----------------------------------------reserve Book--------------------------------------------------------------------- OK

    @RequestMapping(value = "/rest/reserveBook/{bookUuid}/{userUuid}", method = RequestMethod.PUT)
    public ResponseEntity<UserModel> reserveBook(@PathVariable("bookUuid") String bookUuid,
                                            @PathVariable("userUuid") String userUuid) {

        Book book = bookDAO.get(bookUuid);
        UserModel user = userModelDAO.get(userUuid);
        if (book == null) {
            System.out.println("Book with uuid " + bookUuid + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }

        if (user == null) {
            System.out.println("user with uuid " + userUuid + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }

        if(!book.getCondition().equals(Conditions.valueOf("Available")))
            return  new ResponseEntity<UserModel>(HttpStatus.IM_USED);

        Condition condition = new Condition(Conditions.valueOf("Reserved"));
        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        bookDAO.update(book);
        userModelDAO.addReservedBook(user, book);

        return new ResponseEntity<UserModel>(user, HttpStatus.OK);
    }

    //----------------------------------------cancel reserve Book-----------------------------------------------Ok

    @RequestMapping(value = "/rest/cancelReservedBook/{bookUuid}/{userUuid}", method = RequestMethod.PUT)
    public ResponseEntity<Book> cancelReserveBook(@PathVariable("bookUuid") String bookUuid,
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

        if(!book.getCondition().equals(Conditions.valueOf("Reserved")))
            return  new ResponseEntity<Book>(HttpStatus.IM_USED);


        Condition condition = new Condition(Conditions.valueOf("Available"));
        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        bookDAO.update(book);
        userModelDAO.removeReservedBook(user, book);


        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    //----------------------all user borrowed book--------------------------------------  OK

    @RequestMapping(value = "/rest/getUserBooks/{userUuid}", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> listAllUserBooks(@PathVariable("userUuid") String userUuid) {

        UserModel user = userModelDAO.get(userUuid);
        if (user == null) {
            System.out.println("user with uuid " + userUuid + " not found");
            return new ResponseEntity<List<Book>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Book>>(user.getBooks(), HttpStatus.OK);
    }

    //----------------------all user reserved book---------------------------------------- Ok

    @RequestMapping(value = "/rest/getUserReservedBooks/{userUuid}", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> listAllUserReservedBooks(@PathVariable("userUuid") String userUuid) {

        UserModel user = userModelDAO.get(userUuid);
        if (user == null) {
            System.out.println("user with uuid " + userUuid + " not found");
            return new ResponseEntity<List<Book>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Book>>(user.getReservedBooks(), HttpStatus.OK);
    }




    //-------------------------------------borrow book-----------------------------------   Ok
    @RequestMapping(value = "/rest/borrowBook/{userUuid}/{bookUuid}", method = RequestMethod.PUT)
    public ResponseEntity<Void> borrowBook(@PathVariable("userUuid") String userUuid,
                                           @PathVariable("bookUuid") String bookUuid) {

        Book book = bookDAO.get(bookUuid);
        UserModel user = userModelDAO.get(userUuid);

        if (book == null) {
            System.out.println("Book with uuid " + bookUuid + " not found");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }


        if (user == null) {
            System.out.println("User with uuid " + bookUuid + " not found");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }


        if (!book.getCondition().equals(Conditions.valueOf("Available")))
            return new ResponseEntity<Void>(HttpStatus.IM_USED);


        LocalDate planningReturnDate = new LocalDate().plusDays(Conf.getBorrowedDays());


        BookDate date = new BookDate();
        date.setBorrowedDate(new LocalDate());
        date.setPlanningReturnDate(planningReturnDate);
        date.setLogin(user.getLogin());

        Condition condition = new Condition(Conditions.valueOf("Borrowed"));

        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        book.addDate(dateDAO.saveIfNotInDB(date));
        bookDAO.update(book);
        userModelDAO.addBook(user, book);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


        //-------------------------Return borrowed book---------------------------  Ok

    @RequestMapping(value = "/rest/returnBook/{userUuid}/{bookUuid}", method = RequestMethod.PUT)
    public ResponseEntity<Void> returnBorrowBook(@PathVariable("userUuid") String userUuid,
                                           @PathVariable("bookUuid") String bookUuid) {

        Book book = bookDAO.get(bookUuid);
        UserModel user = userModelDAO.get(userUuid);

        if (book == null) {
            System.out.println("Book with uuid " + bookUuid + " not found");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }


        if (user == null) {
            System.out.println("User with uuid " + bookUuid + " not found");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }


        if (!book.getCondition().equals(Conditions.valueOf("Borrowed")))
            return new ResponseEntity<Void>(HttpStatus.IM_USED);

        BookDate date = bookDAO.getLastDate(book);

        double debt = 0;
        if(date.getPlanningReturnDate().isBefore(new LocalDate())){
            debt = Days.daysBetween(date.getPlanningReturnDate(), new LocalDate()).getDays() * Conf.getInterests();
        }

        user.addDebt(debt);
        user.removeBook(book);

        date.setReturnDate(new LocalDate());
        dateDAO.updateDate(date);

        Condition condition = new Condition(Conditions.valueOf("Available"));
        condition = conditionDAO.saveIfNotInDB(condition);
        book.setCondition(condition);
        bookDAO.update(book);
        userModelDAO.update(user);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}