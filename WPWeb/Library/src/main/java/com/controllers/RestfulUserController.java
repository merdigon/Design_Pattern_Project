package com.controllers;

import com.models.Session;
import com.models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by piotrek on 21.11.15.
 */
@RestController
public class RestfulUserController extends BaseController {


    /**
     * Pobiera liste wszystkich ksiazek. Tylko admin
     * @param token
     * @return lista wszystkich ksiazek
     */

    @RequestMapping(value = "/rest/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserModel>> listAllUsers(@RequestHeader("token") String token) {

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<List<UserModel>>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<List<UserModel>>(HttpStatus.FORBIDDEN);


        String login = session.getLogin();
        UserModel user = userModelDAO.getByLogin(login);

        if (!user.getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<List<UserModel>>(HttpStatus.FORBIDDEN);

        List<UserModel> users = userModelDAO.getAll();
        if (users.isEmpty()) {
            return new ResponseEntity<List<UserModel>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<UserModel>>(users, HttpStatus.OK);
    }

    /**
     * Pobiera uzytkownika na podstawie uuid. Tylko admin
     * @param uuid id uzytkownika
     * @param token
     * @return uzytkownik
     */

    @RequestMapping(value = "/rest/user/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<UserModel> getUser(@PathVariable("uuid") String uuid,
                                             @RequestHeader("token") String token) {
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<UserModel>(HttpStatus.FORBIDDEN);


        UserModel user = userModelDAO.get(uuid);
        if (user == null) {
            System.out.println("user with uuid " + uuid + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserModel>(user, HttpStatus.OK);
    }

    /**
     * Aktualizacja uzytkownika na podstawie jego uuid. Tylko admin
     * @param uuid id uzytkownika do zmiany
     * @param user zmieniony uzytkownik w postaci jsona
     * @param token
     * @return httpStatus
     */
    @RequestMapping(value = "/rest/user/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<UserModel> updateUserModel(@PathVariable("uuid") String uuid, @RequestBody UserModel user,
                                                     @RequestHeader("token") String token) {

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<UserModel>(HttpStatus.FORBIDDEN);

        UserModel currentUser = userModelDAO.get(uuid);

        if (currentUser == null) {
            System.out.println("UserModel with uuid " + uuid + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }

        currentUser.setLogin(user.getLogin());
        currentUser.setName(user.getName());
        currentUser.setSurname(user.getSurname());
        currentUser.setMail(user.getMail());
        currentUser.setUserRole(user.getUserRole());
        currentUser.setBooks(user.getBooks());
        currentUser.setDebt(user.getDebt());
        userModelDAO.update(currentUser);
        return new ResponseEntity<UserModel>(currentUser, HttpStatus.OK);
    }

    /**
     * Usuwanie uzytkownika na podstawie jego uuid. tylko admin
     * @param uuid id uzytkownika
     * @param token
     * @return httpStatus
     */

    @RequestMapping(value = "/rest/user/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<UserModel> deleteUser(@PathVariable("uuid") String uuid,
                                                @RequestHeader("token") String token) {

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<UserModel>(HttpStatus.FORBIDDEN);

        UserModel user = userModelDAO.get(uuid);
        if (user == null) {
            System.out.println("Unable to delete. User with uuid " + uuid + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }

        userModelDAO.delete(uuid);
        return new ResponseEntity<UserModel>(HttpStatus.NO_CONTENT);
    }

    /**
     * Rozliczenie zaleglosci finansowej uzytkownika. Tylko admin
     * @param uuid id uzytkownika
     * @param money ilosc zaplaconych odsetek
     * @param token
     * @return
     */

    @RequestMapping(value = "/rest/payDept/{userUuid}/{money}", method = RequestMethod.PUT)
    public ResponseEntity<Void> payDept(@PathVariable("userUuid") String uuid,
                                             @PathVariable("money") double money,
                                             @RequestHeader("token") String token) {

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);

        UserModel user = userModelDAO.get(uuid);
        if (user == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        user.setDebt(user.getDebt() - money);
        userModelDAO.update(user);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
