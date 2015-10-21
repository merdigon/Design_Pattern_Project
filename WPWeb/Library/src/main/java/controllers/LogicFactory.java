package controllers;

import SubLogic.*;

/**
 * Created by Szymon on 2015-10-13.
 */
public class LogicFactory
{
    private BookIOSubLogic bookIOLogic;
    private DataPullSubLogic dataPullSubLogic;
    private UserSubLogic userLogic;

    public LogicFactory()
    {
        bookIOLogic = new BookIOSubLogic(this);
        dataPullSubLogic = new DataPullSubLogic(this);
        userLogic = new UserSubLogic(this);
    }

    public BookIOSubLogic GetBookIOLogic()
    {
        return bookIOLogic;
    }

    public DataPullSubLogic GetDataPull()
    {
        return dataPullSubLogic;
    }

    public UserSubLogic GetUsersLogic()
    {
        return userLogic;
    }
}
