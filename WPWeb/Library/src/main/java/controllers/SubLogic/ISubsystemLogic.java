package src.main.java.controllers.subLogic;

import src.main.java.controllers.*;

/**
 * Created by Szymon on 2015-10-13.
 */
public abstract class ISubsystemLogic
{
    LogicFactory logicsFactory;

    public ISubsystemLogic(LogicFactory lgF)
    {
        logicsFactory = lgF;
    }
}
