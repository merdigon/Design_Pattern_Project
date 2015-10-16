package controllers;
import models.Book;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class BookController {

    @RequestMapping(value = "/addBook", method = RequestMethod.GET)
    public ModelAndView addBook() {
        return new ModelAndView("addBook", "command", new Book());

    }

    @RequestMapping(value = "/saveBook", method = RequestMethod.POST)
    public @ResponseBody
    String saveBook(@RequestParam("author") String author,
                   @RequestParam("title") String title,
                   @RequestParam("year") String year) {
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setYear(year);
        System.out.println(book.getAuthor() + " " + book.getTitle() + " " + book.getYear());
        HibernateOperations.save(book);
        return "zapisalo";
    }

    @RequestMapping(value = "/showBook", method = RequestMethod.GET)
    public String showBook() {

        return "showBook";
    }

    @RequestMapping(value = "/showBooksAjax", method = RequestMethod.POST)
    public @ResponseBody
    List<Book> getTime(){
       return HibernateOperations.getAll();
    }


}