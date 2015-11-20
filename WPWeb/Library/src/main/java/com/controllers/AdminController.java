package com.controllers;

import com.dao.BookDAO;
import com.models.Section;
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
public class AdminController extends BaseController {
    @RequestMapping(value ={"/addSection",} , method = RequestMethod.GET)
    public String moveToAddSection() {
        return "addSection";
    }

    @RequestMapping(value = "/addSection", method = RequestMethod.POST)
    @ResponseBody
    public String addSection(@RequestParam("section") String section){
        sectionDAO.saveIfNotInDB(new Section(section));
        return "success";
    }

    @RequestMapping(value ={"/showUsers",} , method = RequestMethod.GET)
    public String moveToShowUsers(Model model) {
        model.addAttribute("users", userModelDAO.getAll());
        return "showUsers";
    }

}
