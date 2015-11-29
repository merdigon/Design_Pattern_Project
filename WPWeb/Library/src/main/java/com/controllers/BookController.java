package com.controllers;

import com.LibraryConfiguration.Conf;
import com.dao.DateDAO;
import com.dao.SectionDAO;
import com.dao.UserModelDAO;
import com.models.*;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class BookController extends BaseController {


    @RequestMapping(value = {"/admin/addBook"}, method = RequestMethod.GET)
    public String addBook(Model model) {
        model.addAttribute("sections", sectionDAO.getAll());
        model.addAttribute("typesOfBooks", typeOfBookDAO.getAll());
        return "addBook";
    }

    @RequestMapping(value = "/admin/saveBook", method = RequestMethod.POST)
    @ResponseBody
    public String saveBook(@RequestParam("authors") String authorData,
                           @RequestParam("title") String title,
                           @RequestParam("condition") String conditionData,
                           @RequestParam("uuidSection") String uuidSection,
                           @RequestParam("uuidType") String uuidType,
                           @RequestParam("year") int year) {

        String[] authorsString = authorData.split((","));
        List<Author> authors = new ArrayList<Author>();


        for(String authorString : authorsString) {
            Author author = new Author(authorString.split(" ")[0], authorString.split(" ")[1], Integer.parseInt(authorString.split(" ")[2]));
            authors.add(authorDAO.saveIfNotInDB(author));
        }
        Condition condition = conditionDAO.saveIfNotInDB(new Condition(Conditions.valueOf(conditionData)));
        Book book = new Book(authors,title,year,condition,typeOfBookDAO.get(uuidType),sectionDAO.get(uuidSection));
        bookDAO.save(book);
        return "zapisalo";
    }

    @RequestMapping(value ={"/showBooks"} , method = RequestMethod.GET)
    public String showBook(Model model) {
        model.addAttribute("books", bookDAO.getAll());
        return "showBooks";
    }

    @RequestMapping(value = {"/showBooks", "/user/showBooks", "/admin/showBooks"}, method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Book> showBooksAjax() {
       return bookDAO.getAll();
    }

    @RequestMapping(value = {"/searchBooks"}, method = RequestMethod.GET)
    public String searchBook(Model model) {
        model.addAttribute("conditions", conditionDAO.getAll());
        return "searchBooks";
    }

    @RequestMapping(value = "/searchBooks", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public Collection<Book> searchBookAjax(@RequestParam("searchType") String searchType,
                                     @RequestParam("authorName") String authorName,
                                     @RequestParam("authorSurname") String authorSurname,
                                     @RequestParam("authorYear") String authorYear,
                                     @RequestParam("title") String title,
                                     @RequestParam("year") String year,
                                     @RequestParam("condition") String condition){
        if(searchType.equals("author")){
            List<Book> books = new ArrayList<Book>();
            for(Author author : authorDAO.get(authorName, authorSurname, authorYear)){
                books.addAll(bookDAO.getAllByAuthor(author));
            }
            return new LinkedHashSet<>(books);

        }else if(searchType.equals("title")){
           return bookDAO.findByColumn("title", title);
        }else if(searchType.equals("year")){
            return bookDAO.getAllByYear(year);
        }else if(searchType.equals("condition")){
            return bookDAO.getAllByCondition(new Condition(Conditions.valueOf(condition)));
        }
        return null;
    }



    @RequestMapping(value = "/admin/borrowBook", method = RequestMethod.POST)
    @ResponseBody
    public String borrowBook(@RequestParam("userUuid") String userUuid,
                             @RequestParam("bookUuid") String bookUuid){

        UserModel user = userModelDAO.get(userUuid);
        Book book = bookDAO.get(bookUuid);

        if(user.getBooks().size()+1>=Conf.getMaxBorrowedBooks())
            return "Failure: you borrowed too many books";

        if(!book.getCondition().equals(Conditions.valueOf("Available")))
            return "Failure: book is not available";


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
        return "success";
    }

    @RequestMapping(value = "/admin/returnBook", method = RequestMethod.POST)
    @ResponseBody
    public String returnBook(@RequestParam("uuidUser") String uuidUser,
                             @RequestParam("uuidBook") String uuidBook){

        Book book = bookDAO.get(uuidBook);
        UserModel user = userModelDAO.get(uuidUser);

        if(!user.getBooks().contains(book))
            return "Failure: user doesn't borrowed this book";

        BookDate date = bookDAO.getLastDate(book);

        double debt = 0;
        if(date.getPlanningReturnDate().isBefore(new LocalDate())){
            debt = Days.daysBetween(date.getPlanningReturnDate(),new LocalDate()).getDays() * Conf.getInterests();
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
            return "success";
    }

    @RequestMapping(value = "/admin/editBook/{uuid}", method = RequestMethod.GET)
    public String editBook(@PathVariable("uuid")String uuid,Model model) {
        model.addAttribute("book", bookDAO.get(uuid));
        model.addAttribute("conditions", conditionDAO.getAll());
        model.addAttribute("types", typeOfBookDAO.getAll());
        model.addAttribute("sections", sectionDAO.getAll());
        return "editBook";

    }

    @RequestMapping(value = "/admin/editBook", method = RequestMethod.POST)
    @ResponseBody
    public String editBook(@RequestParam("author") String authorData,
                           @RequestParam("uuid") String uuid,
                           @RequestParam("title") String title,
                           @RequestParam("condition") String conditionData,
                           @RequestParam("uuidSection") String uuidSection,
                           @RequestParam("uuidTypeOfBook") String uuidType,
                           @RequestParam("year") int year) {

        Book book = bookDAO.get(uuid);

        String[] authorsString = authorData.split((","));
        List<Author> authors = new ArrayList<Author>();
        for(String authorString : authorsString) {
            Author author = new Author(authorString.split(" ")[0], authorString.split(" ")[1], Integer.parseInt(authorString.split(" ")[2]));
            authors.add(authorDAO.saveIfNotInDB(author));
        }
        book.setAuthors(authors);
        Condition condition = conditionDAO.saveIfNotInDB(new Condition(Conditions.valueOf(conditionData)));
        book.setCondition(condition);
        book.setTitle(title);
        book.setSection(sectionDAO.get(uuidSection));
        book.setTypeOfBook(typeOfBookDAO.get(uuidType));
        book.setYear(year);
        bookDAO.update(book);
        return "zapisalo";
    }

    @RequestMapping(value = "/reserveBook", method = RequestMethod.POST)
    @ResponseBody
    public String reserveBook(@RequestParam("userUuid") String userUuid,
                             @RequestParam("bookUuid") String bookUuid){

        UserModel user;
        Book book = bookDAO.get(bookUuid);

        if(userUuid==""){
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user = userModelDAO.getByLogin(userDetails.getUsername());
        }else
            user = userModelDAO.get(userUuid);



        if(book==null)
            return "Failure: there is no such book";
        if(user==null)
            return "Failure: there is no such user";

        if(user.getReservedBooks().size()+1>=Conf.getMaxReservedBooks())
            return "Failure: you reserved too many books";

        if(!book.getCondition().equals(Conditions.valueOf("Available")))
            return "Failure: book is not available";

        Condition condition = new Condition(Conditions.valueOf("Reserved"));
        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        bookDAO.update(book);
        userModelDAO.addReservedBook(user, book);
        return "success";
    }

    @RequestMapping(value = "/cancelReservedBook", method = RequestMethod.POST)
    @ResponseBody
    public String cancelReserveBook(@RequestParam("userUuid") String userUuid,
                              @RequestParam("bookUuid") String bookUuid){

        UserModel user;
        if(userUuid==""){
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user = userModelDAO.getByLogin(userDetails.getUsername());
        }else
            user = userModelDAO.get(userUuid);

        Book book = bookDAO.get(bookUuid);
        if(book==null)
            return "Failure: there is no such book";
        if(user==null)
            return "Failure: there is no such user";

        if(!book.getCondition().equals(Conditions.valueOf("Reserved")))
            return "Failure: book is not reserved";

        Condition condition = new Condition(Conditions.valueOf("Available"));
        book.setCondition(conditionDAO.saveIfNotInDB(condition));
        bookDAO.update(book);
        userModelDAO.removeReservedBook(user, book);

        return "success";
    }



}