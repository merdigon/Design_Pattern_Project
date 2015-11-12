package com.controllers;

import com.dao.*;
import com.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.util.*;


@Controller
public class BookController extends BaseController {


    @RequestMapping(value = {"/admin/addBook", "/addBook"}, method = RequestMethod.GET)
    public ModelAndView addBook() {
        return new ModelAndView("addBook", "command", new Book());
    }

    @RequestMapping(value = {"/admin/saveBook", "/saveBook"}, method = RequestMethod.POST)
    @ResponseBody
    public String saveBook(@RequestParam("author") String authorData,
                           @RequestParam("title") String title,
                           @RequestParam("condition") String conditionData,
                           @RequestParam("section") String sectionData,
                           @RequestParam("typeOfBook") String typeData,
                           @RequestParam("year") int year) {


        String[] authorsString = authorData.split((","));
        List<Author> authors = new ArrayList<Author>();

        for(String authorString : authorsString) {
            Author author = new Author(authorString.split(" ")[0], authorString.split(" ")[1], Integer.parseInt(authorString.split(" ")[2]));
            authors.add(authorDAO.saveIfNotInDB(author));
        }

        Condition condition = conditionDAO.saveIfNotInDB(new Condition(Conditions.valueOf(conditionData)));
        Section section = sectionDAO.saveIfNotInDB(new Section(sectionData.split(" ")[0], sectionData.split(" ")[1]));
        TypeOfBook typeOfBook = typeOfBookDAO.saveIfNotInDB(new TypeOfBook(typeData.split(" ")[0], typeData.split(" ")[1]));
        Book book = new Book(authors,title,year,condition,typeOfBook,section);
        bookDAO.save(book);

        return "zapisalo";
    }

    @RequestMapping(value ={"/showBook","/user/showBook", "/admin/showBook",} , method = RequestMethod.GET)
    public String showBook() {
        return "showBook";
    }

    @RequestMapping(value = {"/showBooksAjax", "/user/showBooksAjax", "/admin/showBooksAjax"}, method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Book> showBooksAjax() {
      //  System.out.println("table "+bookDAO.getAll());
       return bookDAO.getAll();

    }

    @RequestMapping(value = "/searchBook", method = RequestMethod.GET)
    public String searchBook() {
            return "searchBooks";
    }

    @RequestMapping(value = "/searchBook", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public Collection<Book> searchBookAjax(@RequestParam("searchType") String searchType,
                                     @RequestParam("authorName") String authorName,
                                     @RequestParam("authorSurname") String authorSurname,
                                     @RequestParam("title") String title,
                                     @RequestParam("year") String year,
                                     @RequestParam("condition") String condition){
        if(searchType.equals("author")){
            List<Book> books = new ArrayList<Book>();
            for(Author author : authorDAO.get(authorName, authorSurname)){
                books.addAll(bookDAO.getAllByAuthor(author));
            }
            return new LinkedHashSet<>(books);

        }else if(searchType.equals("title")){
           return bookDAO.findByColumn("title", title);
        }else if(searchType.equals("year")){
            return bookDAO.findByColumn("year", year);
        }else if(searchType.equals("condition")){
            return bookDAO.getAllByCondition(new Condition(Conditions.valueOf(condition)));
        }
        return null;
    }


    @RequestMapping(value = {"/showBooksJsp"}, method = RequestMethod.GET)
    public String showBooksAjax(Model model) {
        model.addAttribute("books",bookDAO.getAll());
        return "showBook";

    }

    @RequestMapping(value = "/borrowBook", method = RequestMethod.POST)
    @ResponseBody
    public String searchBookAjax(@RequestParam("id") String id){
        Book book = bookDAO.get(Integer.parseInt(id));
        Condition condition = new Condition(Conditions.valueOf("Borrowed"));
        condition = conditionDAO.saveIfNotInDB(condition);
        bookDAO.changeCondition(book, condition);
        return "success";
    }

}