package com.controllers;

import com.dao.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pietrek on 10.11.15.
 */
abstract class BaseController {
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
}
