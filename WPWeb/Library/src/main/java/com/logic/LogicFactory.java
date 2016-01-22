//package com.logic;
//
//
//import com.logic.SubLogic.BookIOSubLogic;
//import com.logic.SubLogic.DataPullSubLogic;
//import com.logic.SubLogic.UserSubLogic;
//
//public class LogicFactory
//{
//    private BookIOSubLogic bookIOLogic;
//    private DataPullSubLogic dataPullSubLogic;
//    private UserSubLogic userLogic;
//
//    public LogicFactory()
//    {
//        bookIOLogic = new BookIOSubLogic(this);
//        dataPullSubLogic = new DataPullSubLogic(this);
//        userLogic = new UserSubLogic(this);
//    }
//
//    public BookIOSubLogic getBookIOLogic()
//    {
//        return bookIOLogic;
//    }
//
//    public DataPullSubLogic getDataPull()
//    {
//        return dataPullSubLogic;
//    }
//
//    public UserSubLogic getUsersLogic()
//    {
//        return userLogic;
//    }
//}
