package info.photoorganizer.database;

import info.photoorganizer.metadata.Keyword;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class DatabaseManagerTest
{
    @Test
    public void addRemoveKeyword()
    {
        try
        {
            {
                Database database = DatabaseManager.getInstance().getDatabase();
                Keyword keyword = new Keyword("demo");
                database.getRootKeyword().addChild(keyword);
                database.store();
            }
            {
                Database database = DatabaseManager.getInstance().getDatabase();
                Keyword root = database.getRootKeyword();
                Keyword keyword = root.getChildByName("demo");
                root.removeChild(keyword);
                database.store();
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }
}
