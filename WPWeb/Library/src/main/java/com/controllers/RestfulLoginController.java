package com.controllers;


import com.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by piotrek on 28.11.15.
 */
@RestController
public class RestfulLoginController extends BaseController {

    /**
     * Rejestracja nowego użytkownika. Wszyscy maja dostep
     *@param request "login", "password", "mail", "name", "surname"
     * @return "Success" w przypadku powodzenia, w przeciwnym razie komunikat o bledzie
     */

    @RequestMapping(value = "/rest/registration/", method = RequestMethod.POST)
    public ResponseEntity<String> registrationUser(HttpServletRequest request) {
        UserModel user = new UserModel();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String mail= request.getParameter("mail");
        String name= request.getParameter("name");
        String surname = request.getParameter("surname");
        if (userModelDAO.isLogin(login))
            return new ResponseEntity<String>("{\"Status\" : \"Failure login is used\"}", HttpStatus.IM_USED);

        if (userModelDAO.isMail(mail)) {
            return new ResponseEntity<String>("{\"Status\" : \"Failure mail is used\"}", HttpStatus.IM_USED);
        }

        UserRole userRole = new UserRole();
        userRole.setType("USER");
        user.setLogin(login);
        user.setPassword(password);
        user.setMail(mail);
        user.setName(name);
        user.setSurname(surname);
        userRole = userRoleDAO.saveIfNotInDB(userRole);
        user.setUserRole(userRole);
        userModelDAO.save(user);
        return new ResponseEntity<String>("{\"Status\" : \"Success\"}", HttpStatus.CREATED);

    }

    /**
     * Rejestracja nowego admina. Tylko admin ma dostęp
     * @param request "login", "password", "mail", "surname", "token"
     * @return "Success" w  przypadku powodzenia, w przeciwnym razie komunikat o bledzie
     */

    @RequestMapping(value = "/rest/registrationAdmin/", method = RequestMethod.POST)
    public ResponseEntity<String> registrationAdmin(HttpServletRequest request) {
        UserModel user = new UserModel();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String mail= request.getParameter("mail");
        String name= request.getParameter("name");
        String surname = request.getParameter("surname");
        String token = request.getParameter("token");

        Session session = sessionManager.getAndUpdateSession(token);

        if(session==null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure no session available with given token\"}", HttpStatus.UNAUTHORIZED);

        UserModel admin = userModelDAO.getByLogin(session.getLogin());

        if (admin.getUserRole().getType().equals("ADMIN")) {
            if (userModelDAO.isLogin(login))
                return new ResponseEntity<String>("{\"Status\" : \"Failure login is used\"}", HttpStatus.IM_USED);

            if (userModelDAO.isMail(mail)) {
                return new ResponseEntity<String>("{\"Status\" : \"Failure login is used\"}", HttpStatus.IM_USED);
            }
            UserRole userRole = new UserRole();
            userRole.setType("ADMIN");

            userRole = userRoleDAO.saveIfNotInDB(userRole);
            user.setLogin(login);
            user.setPassword(password);
            user.setName(name);
            user.setSurname(surname);
            user.setMail(mail);
            user.setUserRole(userRole);
            userModelDAO.save(user);
            return new ResponseEntity<String>("{\"Status\" : \"Success\"}", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("{\"Status\" : \"Failure: no user role\"}", HttpStatus.FORBIDDEN);
        }

    }

    /**
     * Logowanie. Wszyscy mają dostep
     * @param request "login", "password"
     * @return token, ktory trzeba przekazywac w naglowkach
     */

    @RequestMapping(value = "/rest/login/", method = RequestMethod.POST)
    public ResponseEntity<String> logIn(HttpServletRequest request) {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (userModelDAO.isValidUser(login, password)) {

            String token = sessionManager.createUserSession(login);
            String response = "{ \"token\" : \"" + token + "\", \"role\" : \"" + userModelDAO.getByLogin(login).getUserRole().getType() + "\"}";
            return new ResponseEntity<String>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"Status\" : \"Failure: wrong user or password\"}", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Wylogowanie. Wszyscy zalogowani uzytkownicy maja dostep
     * @param request "token"
     * @return "Success" or "Failure"
     */

    @RequestMapping(value = "/rest/logout", method = RequestMethod.POST)
    public ResponseEntity<String> logOut(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (sessionManager.closeSession(token))
            return new ResponseEntity<String>("{\"Status\" : \"Success\"}", HttpStatus.OK);
        else
            return new ResponseEntity<String>("{\"Status\" : \"Failure\"}", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Usuwanie uzytkownika. Tylko admin ma dostep;
     * @param request "token", "idNumber"
     * @return "Success" w przypadku powodzenia, w przeciwnym razie komunikat o blędzie
     */


    @RequestMapping(value = "/rest/deleteUser/", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        String token = request.getParameter("token");
        String idNumber = request.getParameter("idNumber");
        Session session = sessionManager.getAndUpdateSession(token);

        if(session==null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure no session available with given token\"}", HttpStatus.UNAUTHORIZED);

        String login = session.getLogin();
        UserModel userToDelete = userModelDAO.getByIdNumber(Integer.parseInt(idNumber));
        UserModel user = userModelDAO.getByLogin(login);
        if(user.getUserRole().getType().equals("ADMIN")){
            if( userToDelete== null){
                return new ResponseEntity<String>("{\"Status\" : \"Failure: no user with this idNumber\"}", HttpStatus.NOT_FOUND);
            }
            userModelDAO.delete(userToDelete.getUuid());
            return new ResponseEntity<String>("{\"Status\" : \"Success\"}", HttpStatus.OK);
        }else
            return new ResponseEntity<String>("{\"Status\" : \"Failure you are no admin\"}", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/rest/addIdNumber/", method = RequestMethod.DELETE)
    public ResponseEntity<String> addIdNumber(HttpServletRequest request) {
        String token = request.getParameter("token");
        String uuid = request.getParameter("uuid");
        int idNumber = Integer.parseInt(request.getParameter("idNumber"));
        Session session = sessionManager.getAndUpdateSession(token);

        if(session==null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure no session available with given token\"}", HttpStatus.UNAUTHORIZED);

        String login = session.getLogin();
        UserModel userToEdit = userModelDAO.get(uuid);
        UserModel user = userModelDAO.getByLogin(login);
        if(user.getUserRole().getType().equals("ADMIN")){
            if( userToEdit== null){
                return new ResponseEntity<String>("{\"Status\" : \"Failure: no user with this uuid\"}", HttpStatus.NOT_FOUND);
            }
            if(userToEdit.getIdNumber()!=0)
                return new ResponseEntity<String>("{\"Status\" : \"Failure: user has idNumber\"}", HttpStatus.NOT_FOUND);

            userToEdit.setIdNumber(idNumber);
            userModelDAO.update(userToEdit);
            return new ResponseEntity<String>("{\"Status\" : \"Success\"}", HttpStatus.OK);
        }else
            return new ResponseEntity<String>("{\"Status\" : \"Failure you are no admin\"}", HttpStatus.UNAUTHORIZED);
    }
}