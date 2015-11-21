package com.controllers;

import com.models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by piotrek on 21.11.15.
 */
public class RestfulUserController  extends BaseController{


    //-------------------Retrieve All Users--------------------------------------------------------

    @RequestMapping(value = "/rest/user/", method = RequestMethod.GET)
    public ResponseEntity<List<UserModel>> listAllUsers() {
        List<UserModel> users = userModelDAO.getAll();
        if (users.isEmpty()) {
            return new ResponseEntity<List<UserModel>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<UserModel>>(users, HttpStatus.OK);
    }

    //--------------------- Retrieve single user by uuid

    @RequestMapping(value = "/rest/user/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> getUser(@PathVariable("uuid") String uuid) {
        UserModel user = userModelDAO.get(uuid);
        if (user == null) {
            System.out.println("user with uuid " + uuid + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserModel>(user, HttpStatus.OK);
    }


    //-------------------Create a User--------------------------------------------------------

    @RequestMapping(value = "/rest/user/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUserModel(@RequestBody UserModel user, UriComponentsBuilder ucBuilder) {


        user.setUserRole(userRoleDAO.saveIfNotInDB(user.getUserRole()));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/user/{uuid}").buildAndExpand(user.getUuid()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //-------------------Update a UserModel--------------------------------------------------------

    @RequestMapping(value = "/rest/user/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<UserModel> updateUserModel(@PathVariable("uuid") String uuid, @RequestBody UserModel user) {

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

    //-------------------Delete a User--------------------------------------------------------

    @RequestMapping(value = "/rest/user/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<UserModel> deleteUser(@PathVariable("uuid") String uuid) {

        UserModel user = userModelDAO.get(uuid);
        if (user == null) {
            System.out.println("Unable to delete. User with uuid " + uuid + " not found");
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);
        }

        userModelDAO.delete(uuid);
        return new ResponseEntity<UserModel>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/rest/payDept/{userUuid}/{money}", method = RequestMethod.PUT)
    public ResponseEntity<UserModel> payDept(@PathVariable("userUuid") String uuid,
                                                 @PathVariable("money") double money) {

        UserModel user = userModelDAO.get(uuid);
        if(user == null)
            return new ResponseEntity<UserModel>(HttpStatus.NOT_FOUND);

        user.setDebt(user.getDebt()- money);
        userModelDAO.update(user);

        return new ResponseEntity<UserModel>(user, HttpStatus.OK);
    }


}
