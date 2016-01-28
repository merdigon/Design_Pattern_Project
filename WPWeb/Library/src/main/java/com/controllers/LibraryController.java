package com.controllers;


import com.LibraryConfiguration.LibraryConfiguration;
import com.models.Section;
import com.models.TypeOfBook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LibraryController extends BaseController {
    @RequestMapping(value ={"/admin/addSection",} , method = RequestMethod.GET)
    public String moveToAddSection() {
        return "addSection";
    }

    @RequestMapping(value = "/admin/addSection", method = RequestMethod.POST)
    @ResponseBody
    public String addSection(@RequestParam("section") String section){
        if(section.equals(""))
            return "Failure: empty section";

        if(sectionDAO.isContain(new Section(section)).isPresent())
            return "Failure: this section is already in database";

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
        if(type.equals(""))
            return "Failure: empty type";
        if(typeOfBookDAO.isContain(new TypeOfBook(type)).isPresent())

            return "Failure: this type is already in database";
        typeOfBookDAO.saveIfNotInDB(new TypeOfBook(type));
        return "success";
    }

    @RequestMapping(value ="/admin/configureLibrary", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String configureLibrary(Model model) {

        return LibraryConfiguration.getInstance().toString();
    }



    @RequestMapping(value = "/admin/editLibraryConfiguration", method = RequestMethod.POST)
    @ResponseBody
    public String editLibraryConfiguration(@RequestParam("days") int days,
                                           @RequestParam("interests") double interests,
                                           @RequestParam("maxReservedBooks") int reserved,
                                           @RequestParam("expirationTime") int expirationTime,
                                           @RequestParam("maxBorrowedBooks") int borrowed){
        LibraryConfiguration.getInstance().setBorrowedDays(days);
        LibraryConfiguration.getInstance().setInterests(interests);
        LibraryConfiguration.getInstance().setMaxBorrowedBooks(borrowed);
        LibraryConfiguration.getInstance().setMaxReservedBooks(reserved);
        LibraryConfiguration.getInstance().setExpirationSessionMinutes(expirationTime);
        return "success";
    }

    @RequestMapping(value ="/aboutUs", method = RequestMethod.GET)
    public String aboutUs() {
        return "aboutUs";
    }

    @RequestMapping(value ="/contactUs", method = RequestMethod.GET)
    public String ContactUs() {
        return "contactUs";
    }
}
