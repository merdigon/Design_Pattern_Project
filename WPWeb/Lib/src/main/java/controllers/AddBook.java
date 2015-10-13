package src.main.java.controllers;

import src.main.java.models.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by pietrek on 11.10.15.
 */
public class AddBook extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        String author = request.getParameter("Author");
        String title = request.getParameter("Title");
        String year = request.getParameter("Year");
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setYear(year);
        System.out.println(book.getAuthor() + " " + book.getTitle() + " " + book.getYear());
        HibernateOperations.save(book);

        PrintWriter out = response.getWriter();
        out.println("zapisalo");
    }
}
