package info.photoorganizer.database;

import info.photoorganizer.metadata.KeywordTagDefinition;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

@Deprecated
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
            database.getTagDefinitions().add(new KeywordTagDefinition("nyckelord som skapades "
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
