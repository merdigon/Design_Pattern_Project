package com.controllers;

import com.models.Book;
import com.models.BookDate;
import com.models.Session;
import com.models.UserModel;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by piotrek on 15.11.15.
 */

@Controller
public class UserController extends BaseController {
    @RequestMapping(value = {"/user/userProfile", "/admin/userProfile"}, method = RequestMethod.GET)
    public String showBook() {
        return "userProfile";
    }

    @RequestMapping(value = {"/admin/userDetails", "/user/userDetails"}, method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public UserModel userDetail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = userModelDAO.getByLogin(userDetails.getUsername());
        user.setDebt(user.countDebt());
        return user;

    }

    @RequestMapping(value = {"/admin/borrowedBooks", "/user/borrowedBooks"}, method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Book> borrowedBooks() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = userModelDAO.getByLogin(userDetails.getUsername());

        return user.getBooks();
    }


    @RequestMapping(value = {"/admin/reservedBooks", "/user/reservedBooks"}, method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public List<Book> reservedBooks() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = userModelDAO.getByLogin(userDetails.getUsername());

        return user.getReservedBooks();

    }

    @RequestMapping(value = {"/admin/searchUser"}, method = RequestMethod.GET)
    public String searchUser() {
        return "searchUser";
    }

    @RequestMapping(value = "/admin/searchUser", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public UserModel searchUser(@RequestParam("searchType") String searchType,
                                @RequestParam("userData") String userData) {
        if (searchType.equals("uuid")) {
            UserModel user = userModelDAO.get(userData);
            user.setDebt(user.countDebt());
            return user;
        } else if (searchType.equals("login")) {
            UserModel user = userModelDAO.getByLogin(userData);
            user.setDebt(user.countDebt());
            return user;
        }
        return null;
    }

    @RequestMapping(value = {"/admin/showUsers"}, method = RequestMethod.GET)
    public String moveToShowUsers(Model model) {
        List<UserModel> users = userModelDAO.getAll();
        for (UserModel user : users)
            user.setDebt(user.countDebt());
        model.addAttribute("users", users);
        return "showUsers";
    }

    @RequestMapping(value = "/admin/payDebt", method = RequestMethod.POST)
    @ResponseBody
    public String editBook(@RequestParam("uuidUser") String uuidUser,
                           @RequestParam("debt") double debt) {

        UserModel user = userModelDAO.get(uuidUser);
        if (user == null) return "Failure: no user with this uuid";
        if (user.getDebt() - debt < 0) return "Failure: you pay too much";

        user.setDebt(user.getDebt() - debt);
        userModelDAO.update(user);
        return "Success: pay debt";

    }

    @RequestMapping(value = "/admin/addIdNumber", method = RequestMethod.POST)
    @ResponseBody
    public String addIdNumber(HttpServletRequest request) {
        String uuid = request.getParameter("userUuid");
        int idNumber = Integer.parseInt(request.getParameter("idNumber"));

        UserModel userToEdit = userModelDAO.get(uuid);
        if (userToEdit == null) return "Failure: no user with this uuid";

        userToEdit.setIdNumber(idNumber);
        userModelDAO.update(userToEdit);
        return "Success";
    }

    @RequestMapping(value={"/user/editUser", "/admin/editUser"})
    @ResponseBody
    public String editUser(@RequestBody UserModel user){

        if(!((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().equals( user.getLogin()))
            return "Failure: you cannot edit this user";

        UserModel currentUser = userModelDAO.getByLogin(user.getLogin());
        if(!user.getPassword().equals("") && user.getPassword().length()<6) return "Failure: password too short";
        if(!user.getPassword().equals("")) currentUser.setPassword(user.getPassword());
        if(!user.getMail().equals(currentUser.getMail()) && userModelDAO.isMail(user.getMail()))
            return "Failure: mail is already in use";

        currentUser.setMail(user.getMail());
        currentUser.setName(user.getName());
        currentUser.setSurname(user.getSurname());
        userModelDAO.update(currentUser);
        return "Success";
    }
}
