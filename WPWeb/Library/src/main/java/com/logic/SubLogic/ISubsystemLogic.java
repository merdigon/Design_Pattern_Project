package com.logic.SubLogic;


import com.logic.LogicFactory;

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
