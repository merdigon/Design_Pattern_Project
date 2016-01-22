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
////        public boolean rentBook(String userUuid, String bookUuid) throws Exception
////        {
////                return logicFactory.getBookIOLogic().rentBook(userUuid, bookUuid);
////        }
//
////        public boolean returnBook(String copyCode) throws Exception
////        {
////                return logicFactory.getBookIOLogic().returnBook(copyCode);
////        }
//
//        @Override
//        public boolean returnBook(String copyCode) throws Exception {
//                return false;
//        }
//
//        @Override
//        public boolean rentBook(String copyCode, String userCode) throws Exception {
//                return false;
//        }
//
//        @Override
//        public boolean reserveBook(String userUuid, String bookUuid) {
//                return false;
//        }
//
//        @Override
//        public boolean cancelReservation(String userUuid, String bookUuid) {
//                return false;
//        }
//
//        public boolean registerUser(UserModel user)
//        {
//                return logicFactory.getUsersLogic().registerUser(user);
//        }
//
//
//}