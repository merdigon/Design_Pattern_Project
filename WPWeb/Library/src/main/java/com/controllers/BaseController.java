package com.controllers;

import com.dao.*;
import com.models.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class BaseController {
    @Autowired
    BookDAO bookDAO;

    @Autowired
    AuthorDAO authorDAO;

    @Autowired
    ConditionDAO conditionDAO;

    @Autowired
    SectionDAO sectionDAO;

    @Autowired
    TypeOfBookDAO typeOfBookDAO;

    @Autowired
    UserModelDAO userModelDAO;

    @Autowired
    UserRoleDAO userRoleDAO;

    @Autowired
    DateDAO dateDAO;

    @Autowired
    TestDAO testDAO;

    @Autowired
    SessionManager sessionManager;
}
