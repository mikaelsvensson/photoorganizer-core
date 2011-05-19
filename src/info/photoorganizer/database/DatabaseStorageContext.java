package info.photoorganizer.database;

import java.io.IOException;

public class DatabaseStorageContext
{
    private DatabaseStorageStrategy strategy = null;

    public DatabaseStorageContext(DatabaseStorageStrategy strategy)
    {
        super();
        this.strategy = strategy;
    }
    
    public void store(Database db) throws IOException
    {
        strategy.store(db);
    }
    
    public Database load()
    {
        return strategy.load();
    }
}
