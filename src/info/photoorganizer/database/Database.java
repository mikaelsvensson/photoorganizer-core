package info.photoorganizer.database;

import info.photoorganizer.metadata.Keyword;

import java.io.IOException;

public class Database implements DatabaseItem
{
    private Keyword rootKeyword = new Keyword(null);
    
    private DatabaseStorageStrategy storageStrategy = null;
    
    public Database(DatabaseStorageStrategy storageStrategy, String name)
    {
        super();
        this.storageStrategy = storageStrategy;
        this.name = name;
    }
    
    public Database(DatabaseStorageStrategy storageStrategy)
    {
        this(storageStrategy, null);
    }

    private String name = null;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Keyword getRootKeyword()
    {
        return rootKeyword;
    }
    
    public void store() throws IOException
    {
        storageStrategy.store(this);
    }

 }
