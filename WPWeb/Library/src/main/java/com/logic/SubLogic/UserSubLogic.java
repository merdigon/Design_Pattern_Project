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
//    public boolean addUserPenalty(UserModel user, double penaltyToAdd)
//    {
//        user.setDebt(user.getDebt()+penaltyToAdd);
//        return saveUser(user);
//    }
//
//    public boolean addUserPenalty(String userCode, double penaltyToAdd) throws Exception {
//        UserModel user = getUser(userCode);
//        if (user == null)
//            throw new Exception("Brak uzytkownika o danym kodzie!");
//
//        user.setDebt(user.getDebt()+penaltyToAdd);
//        return saveUser(user);
//    }
//
//    public boolean saveUser(UserModel user)
//    {
//
//        return true;
//    }
//
//    public boolean registerUser(UserModel user)
//    {
//        return true;
//    }
//}
