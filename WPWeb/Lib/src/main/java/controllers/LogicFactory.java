package src.main.java.controllers;

import src.main.java.models.Book;

/**
 * Created by Szymon on 2015-10-13.
 */
public class LogicFactory
{
    public static Book GetLogic()
    {
        return new Book();//new T();
    }
}
