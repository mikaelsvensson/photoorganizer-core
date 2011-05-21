package info.photoorganizer.database;

import info.photoorganizer.metadata.CoreTagDefinition;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;

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
                KeywordTagDefinition keyword = new KeywordTagDefinition("demo");
                database.getTagDefinitions().add(keyword);
                DatabaseManager.getInstance().saveDatabase();
            }
            {
                Database database = DatabaseManager.getInstance().getDatabase();
                TagDefinition keyword = database.getTagDefinitionByName("demo");
                database.removeTagDefinition(keyword);
                DatabaseManager.getInstance().saveDatabase();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }
    
    @Test
    public void addSynonyms()
    {
        try
        {
            {
                Database database = DatabaseManager.getInstance().getDatabase();
                
                KeywordTagDefinition jfk = new KeywordTagDefinition("JKF");
                KeywordTagDefinition johnFKennedy = new KeywordTagDefinition("John F Kennedy");
                johnFKennedy.addSynonym(jfk);
                
                database.getTagDefinitions().add(johnFKennedy);
                database.getTagDefinitions().add(jfk);

                DatabaseManager.getInstance().saveDatabase();
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
