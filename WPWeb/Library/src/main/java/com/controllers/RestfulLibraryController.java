package com.controllers;

import com.LibraryConfiguration.Conf;
import com.models.Book;
import com.models.Section;
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

    //----------------------------------Add Section--------------------------------------

    @RequestMapping(value = "/rest/addSection/", method = RequestMethod.POST)
    public ResponseEntity<Void> addSection(@RequestBody Section section, UriComponentsBuilder ucBuilder) {



        if (sectionDAO.isExist(section)) {
            System.out.println("A section " + section.getName() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        sectionDAO.save(section);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/configureLibrary/{borrowedDays}/{interests}", method = RequestMethod.PUT)
    public ResponseEntity<Void> configureLibrary(@PathVariable("borrowedDays") int days,
                                         @PathVariable("interests") int interests) {

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}