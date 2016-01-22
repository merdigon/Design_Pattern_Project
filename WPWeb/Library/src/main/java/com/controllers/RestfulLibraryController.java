package com.controllers;

import com.LibraryConfiguration.LibraryConfiguration;
import com.models.Section;
import com.models.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by piotrek on 21.11.15.
 */
@RestController
public class RestfulLibraryController extends BaseController {

    /**
     * Dodanie działu. Tylko admin
     * @param request"token", "section"
     * @return httpStatus
     */

    @RequestMapping(value = "/rest/addSection/", method = RequestMethod.POST)
    public ResponseEntity<String> addSection(HttpServletRequest request) {

        String token = request.getParameter("token");
        Session session = sessionManager.getAndUpdateSession(token);

        Section section = new Section(request.getParameter("section"));
        if (session == null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure no session\"}", HttpStatus.UNAUTHORIZED);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<String>("{\"Status\" : \"Failure no permission\"}", HttpStatus.FORBIDDEN);

        if (sectionDAO.isExist(section)) {
            System.out.println("{\"Status\" : \"A section " + section.getName() + " already exist\"}");
            return new ResponseEntity<String>("Alredy exist",HttpStatus.CONFLICT);
        }

        sectionDAO.save(section);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>("{\"Status\" : \"Success\"}", HttpStatus.CREATED);
    }

    /**
     * Konfiguracja bibioteki. Tylko admin
     * @param request "token", "days" - ilosc dni wypozyczenia, "interests" - odsedki od przetrzymania ksiazki
     *                "borrowed" - maksymalna ilosc wypozyczenia ksiezek dla uzytkownika, "reserved" - maksymalna
     *                ilosc zarezerwowanych ksiazek przez uzytkownika, "expirationTime" - czas po ktorym sesja sie konczy
     * @return HttpStatus
     */

    @RequestMapping(value = "/rest/configureLibrary/", method = RequestMethod.POST)
    public ResponseEntity<String> configureLibrary(HttpServletRequest request) {

        String token = request.getParameter("token");
        String days= request.getParameter("days");
        String interests = request.getParameter("interests");
        String borrowed = request.getParameter("borrowed");
        String reserved = request.getParameter("reserved");
        String expirationTime = request.getParameter("expirationTime");
        Session session = sessionManager.getAndUpdateSession(token);

        if (session == null)
            return new ResponseEntity<String>("{\"Status\" : \"Failure bad token\"}",HttpStatus.UNAUTHORIZED);

        if(!userModelDAO.getByLogin(session.getLogin()).getUserRole().getType().equals("ADMIN"))
            return new ResponseEntity<String>("{\"Status\" : \"Failure no permission\"}",HttpStatus.FORBIDDEN);

        LibraryConfiguration.getInstance().setBorrowedDays(Integer.parseInt(days));
        LibraryConfiguration.getInstance().setInterests(Integer.parseInt(interests));
        LibraryConfiguration.getInstance().setMaxBorrowedBooks(Integer.parseInt(borrowed));
        LibraryConfiguration.getInstance().setMaxReservedBooks(Integer.parseInt(reserved));
        LibraryConfiguration.getInstance().setExpirationSessionMinutes(Integer.parseInt(expirationTime));
        return new ResponseEntity<String>("{\"Status\" : \"Success\"}",HttpStatus.OK);
    }


}