package com.controllers;

import com.dao.*;
import com.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Controller
public class BookController {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private AuthorDAO authorDAO;

    @Autowired
    private ConditionDAO conditionDAO;

    @Autowired
    private SectionDAO sectionDAO;

    @Autowired
    private TypeOfBookDAO typeOfBookDAO;


    @RequestMapping(value = "/addBook", method = RequestMethod.GET)
    public ModelAndView addBook() {
        return new ModelAndView("addBook", "command", new Book());
    }

    @RequestMapping(value = "/saveBook", method = RequestMethod.POST)
    @ResponseBody
    public String saveBook(@RequestParam("author") String authorData,
                           @RequestParam("title") String title,
                           @RequestParam("condition") String conditionData,
                           @RequestParam("section") String sectionData,
                           @RequestParam("typeOfBook") String typeData,
                           @RequestParam("year") int year) {
        System.out.println("author" + authorData);
        System.out.println("title" + title);
        System.out.println("condition" + conditionData);
        System.out.println("section" + sectionData);
        System.out.println("type" + typeData);
        System.out.println("year" + year);


        Author author =new Author(authorData.split(" ")[0], authorData.split("")[1], Integer.parseInt(authorData.split(" ")[2]));
        authorDAO.save(author);
        System.out.println("year" + authorData.split(" ")[0]+ " " + authorData.split("")[1] + " " + Integer.parseInt(authorData.split(" ")[2]));

        Condition condition = new Condition(Conditions.valueOf(conditionData));
        conditionDAO.save(condition);
        System.out.println("year" + year);

        Section section = new Section(sectionData.split(" ")[0], sectionData.split(" ")[1]);
        sectionDAO.save(section);
        System.out.println("year" + year);

        TypeOfBook typeOfBook = new TypeOfBook(typeData.split(" ")[0], typeData.split(" ")[1]);
        typeOfBookDAO.save(typeOfBook);
        System.out.println("year" + year);

        Book book = new Book(author,title,year,condition,typeOfBook,section);

        bookDAO.save(book);

        return "zapisalo";
    }

    @RequestMapping(value = "/showBook", method = RequestMethod.GET)
    public String showBooks() {

        return "showBook";
    }

    @RequestMapping(value = "/showBooksAjax", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Book> showBooksAjax() {
        System.out.println("table "+bookDAO.getAll());
        return bookDAO.getAll();
    }
}