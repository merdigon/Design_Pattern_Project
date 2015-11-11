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

    @RequestMapping(value = "/searchBookAjax", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Book> searchBookAjax(@RequestParam("column") String column,
                           @RequestParam("value") String value){
        List<Book> books = new ArrayList<Book>();
        if(column.equals("author")){

        }else if(column.equals("title")){
           books = bookDAO.findByColumn(column, value);
        }

        System.out.println(books.toString());
        return books;
    }

    @RequestMapping(value = {"/showBooksJsp"}, method = RequestMethod.GET)
    public String showBooksAjax(Model model) {
        model.addAttribute("books",bookDAO.getAll());
        return "showBook";

    }

}