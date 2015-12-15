package com.controllers;


import com.LibraryConfiguration.Conf;
import com.dao.SectionDAO;
import com.models.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by piotrek on 21.11.15.
 */
@RestController
public class RestfulBookController extends BaseController {


    /**
     * Pobieranie wszystkich ksiazek. Dostep wszyscy
     * @return lista ksiazek
     */

    @RequestMapping(value = "/rest/getBooks/", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> listAllbooks() {
        List<Book> books = bookDAO.getAll();
        if (books.isEmpty()) {
            return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }

    /**
     * Sprawdzenie stanu ksiazki. Dostep wszyscy
     * @param request -"uuid"
     * @return stan ksiazki
     */
    @RequestMapping(value = "/rest/getCondition/", method = RequestMethod.GET)
    public ResponseEntity<Condition> condition(HttpServletRequest request) {
        String bookUuid = request.getParameter("uuid");
        Book book = bookDAO.get(bookUuid);
        if (book == null) {
            return new ResponseEntity<Condition>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Condition>(book.getCondition(), HttpStatus.OK);
    }


    /**
     * Zwraca ksiazke o podanych uuid. Dostep wszyscy.
     * @param request -"uuid"
     * @return ksiazka o podanym id
     */

    @RequestMapping(value = "/rest/getBook/", method = RequestMethod.GET)
    public ResponseEntity<Book> getBook(HttpServletRequest request) {
        String uuid = request.getParameter("uuid");

        Book book = bookDAO.get(uuid);
        if (book == null) {
            System.out.println("book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    /**
     * Dodanie ksiazki. Tylko admin
     * na razie dziala tylko dla jednego autora.
     * @param request -"token", "title", "authorName", "authorSurname", "authorBornYear", "condition", "type", "section", "year"
     * @return ksiazka
     */
    @RequestMapping(value = "/rest/addBook/", method = RequestMethod.POST)
    public ResponseEntity<String> addBook(HttpServletRequest request) {

        String token = request.getParameter("token");
        String title = request.getParameter("title");
        String authorName = request.getParameter("authorName");
        String authorSurname = request.getParameter("authorSurname");
        String authorBornYear = request.getParameter("authorBornYear");
        String condition = request.getParameter("condition");
        String type = request.getParameter("type");
        String section = request.getParameter("section");
        String year = request.getParameter("year");
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure bad token\"}",HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<String>("{\"Status\" : \"Failure no permission\"}",HttpStatus.FORBIDDEN);

        Author author = new Author();
        author.setName(authorName);
        author.setSurname(authorSurname);
        author.setBornYear(Integer.parseInt(authorBornYear));

        Book book = new Book();

        book.setAuthors(Arrays.asList(authorDAO.saveIfNotInDB(author)));
        book.setCondition(conditionDAO.saveIfNotInDB(new Condition(Conditions.valueOf(condition))));
        book.setTypeOfBook(typeOfBookDAO.saveIfNotInDB(new TypeOfBook(type)));
        book.setSection(sectionDAO.saveIfNotInDB(new Section(section)));
        book.setTitle(title);
        book.setYear(Integer.parseInt(year));
        bookDAO.save(book);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>("{\"Status\" : \"Success\"}",headers, HttpStatus.CREATED);
    }

    /**
     * Edycja ksiazki o podanych uuid. tylko admin
     * @param request -"uuid", "title", "token", "authorName", "authorSurname", "authorBornYear", "type", "section"
     * @return zmieniona ksiazka
     */
    @RequestMapping(value = "/rest/updateBook/", method = RequestMethod.PUT)
    public ResponseEntity<Book> updateBook(HttpServletRequest request) {

        String token = request.getParameter("token");
        String uuid = request.getParameter("uuid");
        String title = request.getParameter("title");
        String authorName = request.getParameter("authorName");
        String authorSurname = request.getParameter("authorSurname");
        String authorBornYear = request.getParameter("authorBornYear");
        String condition = request.getParameter("condition");
        String type = request.getParameter("type");
        String section = request.getParameter("section");

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<Book>(HttpStatus.FORBIDDEN);

        Book currentBook = bookDAO.get(uuid);

        if (currentBook == null) {
            System.out.println("Book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }
        Author author = new Author(authorName, authorSurname, Integer.parseInt(authorBornYear));

        currentBook.setAuthors(Arrays.asList(authorDAO.saveIfNotInDB(author)));
        currentBook.setCondition(conditionDAO.saveIfNotInDB(new Condition(Conditions.valueOf(condition))));
        currentBook.setTypeOfBook(typeOfBookDAO.saveIfNotInDB(new TypeOfBook(type)));
        currentBook.setSection(sectionDAO.saveIfNotInDB(new Section(section)));
        currentBook.setTitle(title);

        bookDAO.update(currentBook);
        return new ResponseEntity<Book>(currentBook, HttpStatus.OK);
    }

    /**
     * Usuwa ksiazke o podanym uuid. Tylko admin
     * @param request -"uuid", "token"
     * @return status
     */

    @RequestMapping(value = "/rest/deleteBook/", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBook(HttpServletRequest request) {

        String token = request.getParameter("token");
        String uuid= request.getParameter("uuid");

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure bad token\"}",HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<String>("{\"Status\" : \"Failure no permission\"}",HttpStatus.FORBIDDEN);

        Book book = bookDAO.get(uuid);
        if (book == null) {
            System.out.println("Unable to delete. Book with uuid " + uuid + " not found");
            return new ResponseEntity<String>("{\"Status\" : \"Failure book not found\"}",HttpStatus.NOT_FOUND);
        }

        bookDAO.delete(uuid);
        return new ResponseEntity<String>("{\"Status\" : \"Success\"}",HttpStatus.OK);
    }

    /**
     * Przenosi ksiazke o podanym uuid do innego dzialu. Tylko admin
     * @param request -"uuid", "token", "section"
     * @return ksiazka w postaci jsona
     */

    @RequestMapping(value = "/rest/moveBook/", method = RequestMethod.PUT)
    public ResponseEntity<Book> moveBook(HttpServletRequest request) {

        String uuid = request.getParameter("uuid");
        String token = request.getParameter("token");
        String section = request.getParameter("section");
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<Book>(HttpStatus.FORBIDDEN);

        Book currentBook = bookDAO.get(uuid);

        if (currentBook == null) {
            System.out.println("Book with uuid " + uuid + " not found");
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
        }

        currentBook.setSection(sectionDAO.saveIfNotInDB(new Section(section)));
        bookDAO.update(currentBook);
        return new ResponseEntity<Book>(currentBook, HttpStatus.OK);
    }

    /**
     * Zmiana stanu ksiazki. Tylko admin
     * @param request -"uuid", "token", "condition"
     * @return zminiona ksiazka
     */

    @RequestMapping(value = "/rest/changeCondition/", method = RequestMethod.PUT)
    public ResponseEntity<Book> changeCondition(HttpServletRequest request) {

        String token = request.getParameter("token");
        String uuid = request.getParameter("uuid");
        String condition = request.getParameter("condition");
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<Book>(HttpStatus.FORBIDDEN);

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

    /**
     * Rezerwacja ksiazki. Tylko zalogowani uzytkownicy
     * @param request -"bookUuid", "token", "userUuid"
     * @return status
     */
    @RequestMapping(value = "/rest/reserveBook/", method = RequestMethod.PUT)
    public ResponseEntity<UserModel> reserveBook(HttpServletRequest request) {
        String token = request.getParameter("token");
        String bookUuid= request.getParameter("bookUuid");
        String userUuid = request.getParameter("userUuid");
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);


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

        if(user.getReservedBooks().size()+1>=Conf.getMaxReservedBooks())
            return new ResponseEntity<UserModel>(HttpStatus.IM_USED);

        if (!book.getCondition().equals(Conditions.valueOf("Available")))
            return new ResponseEntity<UserModel>(HttpStatus.IM_USED);

        Condition condition = new Condition(Conditions.valueOf("Reserved"));
        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        bookDAO.update(book);
        userModelDAO.addReservedBook(user, book);

        return new ResponseEntity<UserModel>(user, HttpStatus.OK);
    }

    /**
     * Anuluje rezerwacje ksiazki. wszyscy zalogowani maja dostep
     * @param request -"bookUuid", "token", "userUuid"
     * @return status
     */

    @RequestMapping(value = "/rest/cancelReservedBook/", method = RequestMethod.PUT)
    public ResponseEntity<String> cancelReserveBook(HttpServletRequest request) {

        String token = request.getParameter("token");
        String bookUuid = request.getParameter("bookUuid");
        String userUuid= request.getParameter("userUuid");

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure bad token\"}",HttpStatus.NOT_FOUND);


        Book book = bookDAO.get(bookUuid);
        UserModel user = userModelDAO.get(userUuid);
        if (book == null) {
            System.out.println("Book with uuid " + bookUuid + " not found");
            return new ResponseEntity<String>("{\"Status\" : \"Failure book not found\"}",HttpStatus.NOT_FOUND);
        }

        if (user == null) {
            System.out.println("user with uuid " + userUuid + " not found");
            return new ResponseEntity<String>("{\"Status\" : \"Failure user not found\"}",HttpStatus.NOT_FOUND);
        }

        if (!book.getCondition().equals(Conditions.valueOf("Reserved")))
            return new ResponseEntity<String>("{\"Status\" : \"Failure book is not reserved\"}",HttpStatus.IM_USED);


        Condition condition = new Condition(Conditions.valueOf("Available"));
        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        bookDAO.update(book);
        userModelDAO.removeReservedBook(user, book);


        return new ResponseEntity<String>("{\"Status\" : \"Success\"}",HttpStatus.OK);
    }

    /**
     * zwraca liste wszystkich wypozyczonych ksiazkek uzytkownika. tylko admin
     * @param request -"userUuid", "token"
     * @return lista ksiazek
     */

    @RequestMapping(value = "/rest/getUserBooks/", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> listAllUserBooks(HttpServletRequest request) {

        String token = request.getParameter("token");
        String userUuid = request.getParameter("userUuid");

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<List<Book>>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<List<Book>>(HttpStatus.FORBIDDEN);

        UserModel user = userModelDAO.get(userUuid);
        if (user == null) {
            System.out.println("user with uuid " + userUuid + " not found");
            return new ResponseEntity<List<Book>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Book>>(user.getBooks(), HttpStatus.OK);
    }


    /**
     *  zwraca liste wszystkich zarezerwowanych ksiazek uzytkownika. tylko admin
     * @param request -"userUuid", "token"
     * @return lista ksiazek
     */
    @RequestMapping(value = "/rest/getUserReservedBooks/", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> listAllUserReservedBooks(HttpServletRequest request) {

        String token = request.getParameter("token");
        String userUuid = request.getParameter("userUuid");

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<List<Book>>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<List<Book>>(HttpStatus.FORBIDDEN);

        UserModel user = userModelDAO.get(userUuid);
        if (user == null) {
            System.out.println("user with uuid " + userUuid + " not found");
            return new ResponseEntity<List<Book>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Book>>(user.getReservedBooks(), HttpStatus.OK);
    }


    /**
     * Wypozyczenie ksiazki o podanych uuid. tylko admin
     * @param request -"bookUuid", "token", "userUuid"
     * @return status
     */

    @RequestMapping(value = "/rest/borrowBook/", method = RequestMethod.PUT)
    public ResponseEntity<String> borrowBook(HttpServletRequest request) {

        String token = request.getParameter("token");
        String bookUuid= request.getParameter("bookUuid");
        String userUuid = request.getParameter("userUuid");

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure bad session\"}",HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<String>("{\"Status\" : \"Failure no permission\"}",HttpStatus.FORBIDDEN);

        Book book = bookDAO.get(bookUuid);
        UserModel user = userModelDAO.get(userUuid);

        if (book == null) {
            System.out.println("Book with uuid " + bookUuid + " not found");
            return new ResponseEntity<String>("{\"Status\" : \"Failure book not found\"}",HttpStatus.NOT_FOUND);
        }


        if (user == null) {
            System.out.println("User with uuid " + userUuid + " not found");
            return new ResponseEntity<String>("{\"Status\" : \"Failed user not found\"}",HttpStatus.NOT_FOUND);
        }

        if(user.getBooks().size()+1>=Conf.getMaxBorrowedBooks())
            return new ResponseEntity<String>("{\"Status\" : \"Failure you exceed the limit of borrowed books\"}",HttpStatus.IM_USED);

        if (!book.getCondition().equals(Conditions.valueOf("Available")))
            return new ResponseEntity<String>("{\"Status\" : \"Failure book not available\"}",HttpStatus.IM_USED);


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
        return new ResponseEntity<String>("{\"Status\" : \"Success\"}", HttpStatus.OK);
    }

    /**
     * Zwracanie wypozyczonej ksiazki tylko admin
     * @param request -"userUuid", "token", "bookUuid"
     * @return httpstatus
     */

    @RequestMapping(value = "/rest/returnBook/", method = RequestMethod.PUT)
    public ResponseEntity<String> returnBorrowBook(HttpServletRequest request) {

        String token = request.getParameter("token");
        String userUuid= request.getParameter("userUuid");
        String bookUuid = request.getParameter("bookUuid");


        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure bad token\"}",HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<String>("{\"Status\" : \"Failure no permission\"}",HttpStatus.FORBIDDEN);

        Book book = bookDAO.get(bookUuid);
        UserModel user = userModelDAO.get(userUuid);

        if (book == null) {
            System.out.println("Book with uuid " + bookUuid + " not found");
            return new ResponseEntity<String>("{\"Status\" : \"Failure book not found\"}",HttpStatus.NOT_FOUND);
        }


        if (user == null) {
            System.out.println("User with uuid " + userUuid + " not found");
            return new ResponseEntity<String>("{\"Status\" : \"Failure user not found\"}",HttpStatus.NOT_FOUND);
        }


        if (!book.getCondition().equals(Conditions.valueOf("Borrowed")))
            return new ResponseEntity<String>("{\"Status\" : \"Failure book is borrowed\"}",HttpStatus.IM_USED);

        BookDate date = bookDAO.getLastDate(book);

        double debt = 0;
        if (date.getPlanningReturnDate().isBefore(new LocalDate())) {
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
        return new ResponseEntity<String>("{\"Status\" : \"Success\"}",HttpStatus.OK);
    }


}