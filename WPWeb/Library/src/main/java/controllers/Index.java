package controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.ModelMap;

@Controller
public class Index {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {

        return "index";
    }

    @RequestMapping(value = "/redirectAddBook", method = RequestMethod.GET)
    public String redirect() {

        return "redirect:addBook";
    }


    @RequestMapping(value = "/redirectShowBook", method = RequestMethod.GET)
    public String redirectShowBook() {

        return "redirect:showBook";
    }


}
