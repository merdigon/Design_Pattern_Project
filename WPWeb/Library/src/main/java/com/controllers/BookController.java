package com.controllers;

import com.dao.DateDAO;
import com.dao.SectionDAO;
import com.models.*;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.LocalDate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class BookController extends BaseController {


    @RequestMapping(value = {"/admin/addBook", "/addBook"}, method = RequestMethod.GET)
    public String addBook(Model model) {
        model.addAttribute("sections", sectionDAO.getAll());
        model.addAttribute("typesOfBooks", typeOfBookDAO.getAll());
        return "addBook";
    }

    @RequestMapping(value = {"/admin/saveBook", "/saveBook"}, method = RequestMethod.POST)
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

    @RequestMapping(value ={"/showBooks","/user/showBooks", "/admin/showBooks",} , method = RequestMethod.GET)
    public String showBook(Model model) {
        model.addAttribute("books", bookDAO.getAll());
        return "showBook";
    }

    @RequestMapping(value = {"/showBooks", "/user/showBooks", "/admin/showBooks"}, method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Book> showBooksAjax() {
       return bookDAO.getAll();
    }

    @RequestMapping(value = "/searchBooks", method = RequestMethod.GET)
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


    @RequestMapping(value = {"/showBooksJsp"}, method = RequestMethod.GET)
    public String showBooksAjax(Model model) {
        model.addAttribute("books", bookDAO.getAll());
        return "showBook";

    }

    @RequestMapping(value = "/borrowBook", method = RequestMethod.POST)
    @ResponseBody
    public String borrowBook(@RequestParam("uuid") String uuid){

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userModelDAO.getByLogin(login);
        LocalDate currentDate = new LocalDate();
        LocalDate planningReturnDate = new LocalDate().plusMonths(1);

        Book book = bookDAO.get(uuid);

        BookDate date = new BookDate();
        date.setBorrowedDate(currentDate);
        date.setPlanningReturnDate(planningReturnDate);
        date.setLogin(login);

        dateDAO.saveIfNotInDB(date);

        Condition condition = new Condition(Conditions.valueOf("Borrowed"));
        condition = conditionDAO.saveIfNotInDB(condition);
        bookDAO.changeCondition(book, condition);
        bookDAO.addDate(book, date);

        userModelDAO.addBook(login, book);
        return "success";
    }

    @RequestMapping(value = "/returnBook", method = RequestMethod.POST)
    @ResponseBody
    public String returnBook(@RequestParam("uuid") String uuid){

        Book book = bookDAO.get(uuid);
        BookDate date = bookDAO.getLastDate(book);

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userModelDAO.getByLogin(login);

        userModelDAO.removeBook(user, book);

        date.setReturnDate(new LocalDate());
        dateDAO.updateDate(date);

        Condition condition = new Condition(Conditions.valueOf("Available"));
        condition = conditionDAO.saveIfNotInDB(condition);
        bookDAO.changeCondition(book, condition);

            return "success";
    }

    @RequestMapping(value = "/editBook/{uuid}", method = RequestMethod.GET)
    public String editBook(@PathVariable("uuid")String uuid,Model model) {
        model.addAttribute("book", bookDAO.get(uuid));
        model.addAttribute("conditions", conditionDAO.getAll());
        model.addAttribute("types", typeOfBookDAO.getAll());
        model.addAttribute("sections", sectionDAO.getAll());
        return "editBook";

    }

    @RequestMapping(value = "/editBook", method = RequestMethod.POST)
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




}