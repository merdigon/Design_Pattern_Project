package models;

/**
 * Created by Szymon on 2015-10-13.
 */
public class Condition extends DatabaseObject
{
    Conditions condition;

    public String ToString()
    {
        return String.valueOf(condition);
    }

    public boolean equals(Condition cond)
    {
        return condition.equals(cond.condition);
    }


    public boolean equals(Conditions conds)
    {
        return condition.equals(conds);
    }
}