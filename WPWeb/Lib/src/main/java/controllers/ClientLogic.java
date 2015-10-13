package src.main.java.controllers;

/**
 * Created by Szymon on 2015-10-13.
 */
import src.main.java.models.*;
public class ClientLogic implements IClientLogic
        {
public CopyStatus RentCopy(String copyCode, String userCode)
        {
        return LogicFactory.GetLogic().RentCopy(copyCode, userCode);
        }

public CopyStatus ReturnCopy(String copyCode)
        {
        return LogicFactory.GetLogic().Return(copyCode);
        }

public boolean RegisterUser(User user)
        {
        return LogicFactory.GetLogic().RegisterUser(user);
        }


        }