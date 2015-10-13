package src.main.java.controllers.SubLogic;

import src.main.java.controllers.*;

/**
 * Created by Szymon on 2015-10-13.
 */
    public abstract class ISubsystemLogic
    {
        LogicFactory logics;

        public ISubsystemLogic()
        {
            logics = new LogicFactory();
        }
    }
