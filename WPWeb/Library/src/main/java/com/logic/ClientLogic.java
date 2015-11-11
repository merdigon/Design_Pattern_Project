//package com.logic;
//
//import com.models.CopyStatus;
//import com.models.UserModel;
//
///**
// * Created by Szymon on 2015-10-13.
// */
//
//
//public class ClientLogic implements IClientLogic
//{
//        LogicFactory logicFactory;
//
//        public ClientLogic()
//        {
//                logicFactory = new LogicFactory();
//        }
//
//        public CopyStatus RentCopy(String copyCode, String userCode) throws Exception
//        {
//                return logicFactory.GetBookIOLogic().rentCopy(copyCode, userCode);
//        }
//
//        public CopyStatus ReturnCopy(String copyCode) throws Exception
//        {
//                return logicFactory.GetBookIOLogic().Return(copyCode);
//        }
//
//        public boolean RegisterUser(UserModel user)
//        {
//                return logicFactory.GetUsersLogic().RegisterUser(user);
//        }
//
//
//}