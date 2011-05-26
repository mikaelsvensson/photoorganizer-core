package info.photoorganizer.database;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;

import java.net.URL;

public class DatabaseStoragePolicy
{
    public static DatabaseStorageStrategy getStrategy(URL databaseUrl)
    {
        if (XMLDatabaseStorageStrategy.handles(databaseUrl))
        {
            return new XMLDatabaseStorageStrategy(databaseUrl);
        }
        else
        {
            return null;
        }
    }
}
