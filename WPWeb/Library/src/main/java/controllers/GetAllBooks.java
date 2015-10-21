package controllers;

import src.main.java.models.Book;
import org.json.simple.JSONArray;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetAllBooks extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        JSONArray json = new JSONArray();
        for(Book b :HibernateOperations.getAll()){
            json.add(b);
        }
        PrintWriter out = response.getWriter();
        out.print(json);
    }

}