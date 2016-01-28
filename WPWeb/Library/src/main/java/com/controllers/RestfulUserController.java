package com.controllers;

import com.dao.UserModelDAO;
import com.models.Session;
import com.models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
public class RestfulUserController extends BaseController {


    /**
     * Pobiera liste wszystkich uzytkownikow. Tylko admin
     * @param request -"token"
     * @return lista wszystkich ksiazek
     */


    @RequestMapping(value = "/rest/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserModel>> listAllUsers(HttpServletRequest request) {

        String token = request.getParameter("token");
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<List<UserModel>>(HttpStatus.UNAUTHORIZED);

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
     * Pobiera uzytkownika na podstawie idNumber. Tylko admin
     @param request "token", "idNumber"
     * @return uzytkownik
     */

    @RequestMapping(value = "/rest/user/", method = RequestMethod.GET)
    public ResponseEntity<UserModel> getUser(HttpServletRequest request) {
        String token = request.getParameter("token");
        int uuid = Integer.parseInt(request.getParameter("idNumber"));
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<UserModel>(HttpStatus.UNAUTHORIZED);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<UserModel>(HttpStatus.FORBIDDEN);


        UserModel user = userModelDAO.getByIdNumber(uuid);
        if (user == null) {
            System.out.println("user with uuid " + uuid + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserModel>(user, HttpStatus.OK);
    }

    /**
     * Aktualizacja uzytkownika na podstawie jego idNumber. Tylko admin
     * @param request "token", "idNumber", "password", "name", "surname", "mail"
     * @return httpStatus
     */
    @RequestMapping(value = "/rest/user/", method = RequestMethod.POST)
    public ResponseEntity<UserModel> updateUserModel(HttpServletRequest request) {

        String token = request.getParameter("token");
        int idNumber = Integer.parseInt(request.getParameter("idNumber"));
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String mail = request.getParameter("mail");

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<UserModel>(HttpStatus.UNAUTHORIZED);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<UserModel>(HttpStatus.FORBIDDEN);

        UserModel currentUser = userModelDAO.getByIdNumber(idNumber);

        if (currentUser == null) {
            System.out.println("{\"Status\" : \"UserModel with idNumber " + idNumber + " not found\"}");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }
        if(userModelDAO.isMail(mail)){
            System.out.println("{\"Status\" : \"UserModel with mail " + mail + " exist\"}");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }
        currentUser.setName(name);
        currentUser.setSurname(surname);
        currentUser.setMail(mail);
        userModelDAO.update(currentUser);
        return new ResponseEntity<UserModel>(currentUser, HttpStatus.OK);
    }

    /**
     * Usuwanie uzytkownika na podstawie idNumber. tylko admin
     * @param request "token", "idNumber"
     * @return httpStatus
     */

    @RequestMapping(value = "/rest/user/", method = RequestMethod.DELETE)
    public ResponseEntity<UserModel> deleteUser(HttpServletRequest request) {

        String token = request.getParameter("token");
        int idNumber = Integer.parseInt(request.getParameter("idNumber"));
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<UserModel>(HttpStatus.UNAUTHORIZED);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<UserModel>(HttpStatus.FORBIDDEN);

        UserModel user = userModelDAO.getByIdNumber(idNumber);
        if (user == null) {
            System.out.println("{\"Status\" : \"Unable to delete. User with idNumber " + idNumber + " not found\"}");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }

        userModelDAO.delete(user.getUuid());
        return new ResponseEntity<UserModel>(HttpStatus.OK);
    }

    /**
     * Rozliczenie zaleglosci finansowej uzytkownika. Tylko admin
     @param request "idNumber", "money" - to pay debt, "token"
     * @return status
     */

    @RequestMapping(value = "/rest/payDept/", method = RequestMethod.POST)
    public ResponseEntity<String> payDept(HttpServletRequest request) {

        int idNumber = Integer.parseInt(request.getParameter("idNumber"));
        double money = Double.parseDouble(request.getParameter("money"));
        String token = request.getParameter("token");
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure bad token\"}",HttpStatus.UNAUTHORIZED);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<String>("{\"Status\" : \"Failure you are not admin\"}",HttpStatus.FORBIDDEN);

        UserModel user = userModelDAO.getByIdNumber(idNumber);
        if (user == null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure bad user idNumber\"}",HttpStatus.NOT_FOUND);

        if(user.countDebt()<=money)

        user.setDebt(user.getDebt() - money);
        userModelDAO.update(user);

        return new ResponseEntity<String>("{\"Status\" : \"Success\"}",HttpStatus.OK);
    }


}
