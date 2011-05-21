package info.photoorganizer.database;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;

public class DatabaseStoragePolicy
{
    public static DatabaseStorageStrategy newDefaultStrategy()
    {
        return new XMLDatabaseStorageStrategy();
    }
}
