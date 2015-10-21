package controllers.SubLogic;

import controllers.LogicFactory;
import models.User;

/**
 * Created by Szymon on 2015-10-13.
 */

public class UserSubLogic extends DataPullSubLogic
{
    public UserSubLogic(LogicFactory lgF)
    {
        super(lgF);
    }

    public boolean ChangeUserPenalty(User user, long penaltyToAdd)
    {
        user.debt += penaltyToAdd;
        return SaveUser(user);
    }

    public boolean ChangeUserPenalty(String userCode, double penaltyToAdd) throws Exception {
        User user = GetUser(userCode);
        if (user == null)
            throw new Exception("Brak uzytkownika o danym kodzie!");

        user.debt += penaltyToAdd;
        return SaveUser(user);
    }

    public boolean SaveUser(User user)
    {
        return true;
    }

    public boolean RegisterUser(User user)
    {
        return true;
    }
}
