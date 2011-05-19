package info.photoorganizer.database;

import info.photoorganizer.metadata.Keyword;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

public class DatabaseStorageTest
{
    @Test
    public void loadAndStore()
    {
        try
        {
            DatabaseStorageContext context = new DatabaseStorageContext(DatabaseStoragePolicy.newDefaultStrategy());
            Database database = context.load();
            context.store(database);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }
    
    @Test
    public void addKeyword()
    {
        try
        {
            DatabaseStorageContext context = new DatabaseStorageContext(DatabaseStoragePolicy.newDefaultStrategy());
            Database database = context.load();
            database.getRootKeyword().addChild(new Keyword("nyckelord som skapades "
                    + Calendar.getInstance().getTime().toLocaleString()));
            context.store(database);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }
}
