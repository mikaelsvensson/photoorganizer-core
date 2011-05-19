package info.photoorganizer.database;

public class DatabaseStoragePolicy
{
    public static DatabaseStorageStrategy newDefaultStrategy()
    {
        return new DatabaseXMLStorageStrategy();
    }
}
