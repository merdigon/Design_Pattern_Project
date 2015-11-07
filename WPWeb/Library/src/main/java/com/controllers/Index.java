package com.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.ModelMap;

@Controller
public class Index {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {

        return "index";

    }

}
