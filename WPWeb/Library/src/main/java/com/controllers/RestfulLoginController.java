package com.controllers;


import com.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by piotrek on 28.11.15.
 */
@RestController
public class RestfulLoginController extends BaseController {

    /**
     * Rejestracja nowego użytkownika. Wszyscy maja dostep
     *
     * @param user json reprezentujacy uzytkownika
     * @return "Success" w przypadku powodzenia, w przeciwnym razie komunikat o bledzie
     */

    @RequestMapping(value = "/rest/registration", method = RequestMethod.POST)
    public ResponseEntity<String> registrationUser(@RequestBody UserModel user) {

        if (userModelDAO.isLogin(user.getLogin()))
            return new ResponseEntity<String>("Failure: login is used", HttpStatus.IM_USED);

        if (userModelDAO.isMail(user.getMail())) {
            return new ResponseEntity<String>("Failure: mail is used", HttpStatus.IM_USED);
        }
        if(!user.getUserRole().getType().equals("USER")){
            return new ResponseEntity<String>("Failure: wrong user role", HttpStatus.IM_USED);
        }
        UserRole userRole = new UserRole();
        userRole.setType(user.getUserRole().getType());

        userRole = userRoleDAO.saveIfNotInDB(userRole);
        user.setUserRole(userRole);
        userModelDAO.save(user);
        return new ResponseEntity<String>("Success", HttpStatus.CREATED);

    }

    /**
     * Rejestracja nowego admina. Tylko admin ma dostęp
     * @param user json reprezentujacy admina
     * @param token token przekazywany w naglowku
     * @return "Success" w  przypadku powodzenia, w przeciwnym razie komunikat o bledzie
     */

    @RequestMapping(value = "/rest/registrationAdmin", method = RequestMethod.POST)
    public ResponseEntity<String> registrationAdmin(@RequestBody UserModel user, @RequestHeader("token") String token) {

        Session session = sessionManager.getAndUpdateSession(token);

        if(session==null)
            return new ResponseEntity<String>("No session available with given token", HttpStatus.NOT_FOUND);

        String login = session.getLogin();

        UserModel user1 = userModelDAO.getByLogin(login);

        if (user1.getUserRole().getType().equals("ADMIN")) {
            if (userModelDAO.isLogin(user.getLogin()))
                return new ResponseEntity<String>("Failure: login is used", HttpStatus.IM_USED);

            if (userModelDAO.isMail(user.getMail())) {
                return new ResponseEntity<String>("Failure: mail is used", HttpStatus.IM_USED);
            }
            if(!(user.getUserRole().getType().equals("USER") || user.getUserRole().getType().equals("ADMIN"))){
                return new ResponseEntity<String>("Failure: wrong user role", HttpStatus.IM_USED);
            }
            UserRole userRole = new UserRole();
            userRole.setType(user.getUserRole().getType());

            userRole = userRoleDAO.saveIfNotInDB(userRole);
            user.setUserRole(userRole);
            userModelDAO.save(user);
            return new ResponseEntity<String>("Success", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Failure: no user role", HttpStatus.FORBIDDEN);
        }

    }

    /**
     * Logowanie. Wszyscy mają dostep
     * @param login
     * @param password
     * @return token, ktory trzeba przekazywac w naglowkach
     */

    @RequestMapping(value = "/rest/login/{user}/{password}", method = RequestMethod.POST)
    public ResponseEntity<String> logIn(@PathVariable("user") String login,
                                        @PathVariable("password") String password) {

        if (userModelDAO.isValidUser(login, password)) {

            String token = sessionManager.createUserSession(login);
            return new ResponseEntity<String>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Failure: wrong user or password", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Wylogowanie. Wszyscy zalogowani uzytkownicy maja dostep
     * @param token przekazany w naglowku
     * @return "Success" or "Failure"
     */

    @RequestMapping(value = "/rest/logout", method = RequestMethod.POST)
    public ResponseEntity<String> logOut(@RequestHeader("token") String token) {
        if (sessionManager.closeSession(token))
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        else
            return new ResponseEntity<String>("Failure", HttpStatus.NOT_FOUND);
    }

    /**
     * Usuwanie uzytkownika. Tylko admin ma dostep;
     * @param token przekazany w naglowku
     * @param uuid id uzytkownika
     * @return "Success" w przypadku powodzenia, w przeciwnym razie komunikat o blędzie
     */


    @RequestMapping(value = "/rest/deleteUser/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@RequestHeader("token") String token,
                                             @PathVariable("uuid") String uuid) {
        Session session = sessionManager.getAndUpdateSession(token);

        if(session==null)
            return new ResponseEntity<String>("No session available with given token", HttpStatus.NOT_FOUND);

        String login = session.getLogin();



        UserModel user = userModelDAO.getByLogin(login);
        if(user.getUserRole().getType().equals("ADMIN")){
            if(userModelDAO.get(uuid) == null){
                return new ResponseEntity<String>("Failure: no user with this uuid", HttpStatus.OK);
            }
            userModelDAO.delete(uuid);
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        }else
            return new ResponseEntity<String>("Failure: no admin", HttpStatus.OK);
    }



}
