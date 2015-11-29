package com.controllers;

import com.LibraryConfiguration.Conf;
import com.models.Book;
import com.models.Section;
import com.models.Session;
import com.models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by piotrek on 21.11.15.
 */
@RestController
public class RestfulLibraryController extends BaseController {

    /**
     * Dodanie działu. Tylko admin
     * @param section dzial
     * @param token token
     * @return httpStatus
     */

    @RequestMapping(value = "/rest/addSection/", method = RequestMethod.POST)
    public ResponseEntity<Void> addSection(@RequestBody Section section,
                                           @RequestHeader("token") String token) {

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);

        if (sectionDAO.isExist(section)) {
            System.out.println("A section " + section.getName() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        sectionDAO.save(section);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    /**
     * Konfiguracja bibioteki. Tylko admin
     * @param days maksymalna liczba dni dla wypozyczenia ksiazki
     * @param interests kara za przetrzymanie, naliczana codziennie
     * @param token
     * @return HttpStatus
     */

    @RequestMapping(value = "/rest/configureLibrary/{borrowedDays}/{interests}", method = RequestMethod.PUT)
    public ResponseEntity<Void> configureLibrary(@RequestParam("days") int days,
                                                 @RequestParam("interests") double interests,
                                                 @RequestParam("maxReservedBooks") int reserved,
                                                 @RequestParam("expirationTime") int expirationTime,
                                                 @RequestParam("maxBorrowedBooks") int borrowed,
                                                 @RequestHeader("token") String token) {

        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);

        Conf.setBorrowedDays(days);
        Conf.setInterests(interests);
        Conf.setBorrowedDays(borrowed);
        Conf.setMaxReservedBooks(reserved);
        Conf.setExpirationSessionMinutes(expirationTime);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}