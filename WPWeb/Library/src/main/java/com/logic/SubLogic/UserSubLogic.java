//package com.logic.SubLogic;
//
//import com.logic.LogicFactory;
//import com.models.UserModel;
//
///**
// * Created by Szymon on 2015-10-13.
// */
//
//public class UserSubLogic extends DataPullSubLogic
//{
//    public UserSubLogic(LogicFactory lgF)
//    {
//        super(lgF);
//    }
//
//    public boolean ChangeUserPenalty(UserModel user, long penaltyToAdd)
//    {
//        user.debt += penaltyToAdd;
//        return SaveUser(user);
//    }
//
//    public boolean ChangeUserPenalty(String userCode, double penaltyToAdd) throws Exception {
//        UserModel user = GetUser(userCode);
//        if (user == null)
//            throw new Exception("Brak uzytkownika o danym kodzie!");
//
//        user.debt += penaltyToAdd;
//        return SaveUser(user);
//    }
//
//    public boolean SaveUser(UserModel user)
//    {
//        return true;
//    }
//
//    public boolean RegisterUser(UserModel user)
//    {
//        return true;
//    }
//}
