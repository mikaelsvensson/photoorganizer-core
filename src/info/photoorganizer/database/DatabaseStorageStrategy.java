package info.photoorganizer.database;

import java.io.IOException;

public interface DatabaseStorageStrategy
{
    public void store(Database db) throws IOException;
    public Database load();
}
