package com.controllers;

import com.models.Book;
import com.models.UserModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by piotrek on 15.11.15.
 */

@Controller
public class UserController extends  BaseController {
    @RequestMapping(value ={"/userProfile"} , method = RequestMethod.GET)
    public String showBook() {
        return "userProfile";
    }

    @RequestMapping(value = {"/userDetails"}, method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public String userDetail() {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = userModelDAO.getByLogin(userDetails.getUsername());
        return user.toString();

    }

    @RequestMapping(value = {"/myBooks"}, method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Book> myBooks() {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = userModelDAO.getByLogin(userDetails.getUsername());
        return user.getBooks();

    }

}
