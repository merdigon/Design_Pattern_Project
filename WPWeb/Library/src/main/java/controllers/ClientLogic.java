package controllers;

import models.CopyStatus;
import models.User;

/**
 * Created by Szymon on 2015-10-13.
 */


public class ClientLogic implements IClientLogic
{
        LogicFactory logicFactory;

        public ClientLogic()
        {
                logicFactory = new LogicFactory();
        }

        public CopyStatus RentCopy(String copyCode, String userCode) throws Exception
        {
                return logicFactory.GetBookIOLogic().RentCopy(copyCode, userCode);
        }

        public CopyStatus ReturnCopy(String copyCode) throws Exception
        {
                return logicFactory.GetBookIOLogic().Return(copyCode);
        }

        public boolean RegisterUser(User user)
        {
                return logicFactory.GetUsersLogic().RegisterUser(user);
        }


}