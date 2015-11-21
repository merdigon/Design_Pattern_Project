package com.controllers;

import com.LibraryConfiguration.Conf;
import com.dao.BookDAO;
import com.models.Section;
import com.models.TypeOfBook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pietrek on 19.11.15.
 */
@Controller
public class LibraryController extends BaseController {
    @RequestMapping(value ={"/admin/addSection",} , method = RequestMethod.GET)
    public String moveToAddSection() {
        return "addSection";
    }

    @RequestMapping(value = "/admin/addSection", method = RequestMethod.POST)
    @ResponseBody
    public String addSection(@RequestParam("section") String section){
        if(sectionDAO.isContain(new Section(section)).isPresent())
            return "failure this section is already in database";

        sectionDAO.saveIfNotInDB(new Section(section));
        return "success";
    }

    @RequestMapping(value ="/admin/addType", method = RequestMethod.GET)
    public String moveToAddType() {
        return "addType";
    }

    @RequestMapping(value = "/admin/addType", method = RequestMethod.POST)
    @ResponseBody
    public String addType(@RequestParam("type") String type){
        if(typeOfBookDAO.isContain(new TypeOfBook(type)).isPresent())

            return "failure this type is already in database";
        typeOfBookDAO.saveIfNotInDB(new TypeOfBook(type));
        return "success";
    }

    @RequestMapping(value ="/admin/configureLibrary", method = RequestMethod.GET)
    public String configureLibrary(Model model) {
        model.addAttribute("borrowedDays", Conf.getBorrowedDays());
        model.addAttribute("interests", Conf.getInterests());
        return "configureLibrary";
    }

    @RequestMapping(value = "/admin/editLibraryConfiguration", method = RequestMethod.POST)
    @ResponseBody
    public String editLibraryConfiguration(@RequestParam("days") int days,
                                           @RequestParam("interests") double interests){
        Conf.setBorrowedDays(days);
        Conf.setInterests(interests);
        return "success";
    }

}
