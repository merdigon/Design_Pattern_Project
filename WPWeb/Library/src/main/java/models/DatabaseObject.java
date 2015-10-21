package models;

/**
 * Created by Szymon on 2015-10-13.
 */
public abstract class DatabaseObject
{
    public int id;

    public String ToString()
    {
        return String.valueOf(id);
    }
}
