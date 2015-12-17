package com.controllers;

import com.configuration.IdNumberGenerator;
import com.models.UserModel;
import com.models.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by piotrek on 08.11.15.
 */


@Controller
public class LoginController extends BaseController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "index";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String userPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "index";
    }


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String searchBook() {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public String addUser(HttpServletRequest request) {
        String userRole = request.getParameter("role");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String mail = request.getParameter("mail");
        int idNumber = new IdNumberGenerator().getRandomNumberInRange(100000, 999990);
        UserModel user = new UserModel();

        if(login.length()<6)
            return "Failure: login is too short";
        if(password.length()<6)
            return "Failure: password is too short";

        if(userModelDAO.isLogin(login))
            return "Failure: login is used";

        if(userModelDAO.isMail(mail)){
            return "failure: mail is used";
        }

        user.setLogin(login);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setMail(mail);
        user.setIdNumber(idNumber);
        UserRole role = new UserRole();
        role.setType("USER");
        role = userRoleDAO.saveIfNotInDB(role);
        user.setUserRole(role);
        userModelDAO.save(user);
        return "Success";
    }

    //    uruchomic tylko raz, dodaje konto admina
    @RequestMapping(value = "/addAdmin", method = RequestMethod.GET)
    @ResponseBody
    public String addAdmin() {
        UserModel user = new UserModel();
        user.setLogin("admin");
        user.setPassword("password");
        user.setName("adminName");
        user.setSurname("adminSurname");
        user.setMail("adminMail");
        user.setIdNumber(100000);
        UserRole role = new UserRole();
        role.setType("ADMIN");
        userRoleDAO.save(role);
        user.setUserRole(role);
        userModelDAO.save(user);
        return "Success";
    }

    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}