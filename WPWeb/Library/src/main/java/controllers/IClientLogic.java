package controllers;

import models.*;
/**
 * Created by Szymon on 2015-10-13.
 */
public interface IClientLogic
{
    CopyStatus ReturnCopy(String copyCode) throws Exception;

    CopyStatus RentCopy(String copyCode, String userCode) throws Exception;

    boolean RegisterUser(User user);
}
