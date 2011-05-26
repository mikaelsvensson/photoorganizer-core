package info.photoorganizer.database;

import info.photoorganizer.metadata.DefaultTagDefinition;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.KeywordTag;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.metadata.TextTag;
import info.photoorganizer.metadata.TextTagDefinition;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class DatabaseManagerTest
{
    private static final String KEYWORD_NAME = "demo";

    @Test
    public void addRemoveKeyword()
    {
        addKeyword();
        removeKeyword();
        
        Assert.assertTrue(true);
    }
    
    public void addKeyword()
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            KeywordTagDefinition keyword = database.createRootKeyword(KEYWORD_NAME);
            keyword.store();
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            database.close();
        }
    }
    
    public void removeKeyword()
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            TagDefinition keyword = database.getTagDefinition(KEYWORD_NAME);
            keyword.remove();
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            database.close();
        }
    }
    
    @Test
    public void addSynonyms()
    {
        try
        {
            {
                Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
                
                KeywordTagDefinition jfk = database.createRootKeyword("JKF");
                KeywordTagDefinition johnFKennedy = database.createRootKeyword("John F Kennedy");
                johnFKennedy.addSynonym(jfk.getId());
                
                jfk.store();
                johnFKennedy.store();

//                DatabaseManager.getInstance().saveDatabase();
                
                Assert.assertTrue(true);
            }
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void addImage()
    {
        try
        {
            {
                Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
                
                Image img = database.createImage();
                img.setUrl(new URL("http://demo/test.jpg"));
                
                TagDefinition tagDefinition = database.getTagDefinition(DatabaseManager.KEYWORD_OBJECTS);
                if (tagDefinition instanceof KeywordTagDefinition)
                {
                    img.getTags().add(new KeywordTag((KeywordTagDefinition) tagDefinition));
                }
                
                TagDefinition commentTagDefinition = database.getTagDefinition(DefaultTagDefinition.COMMENT.getId());
                if (commentTagDefinition instanceof TextTagDefinition)
                {
                    TextTag commentTag = new TextTag((TextTagDefinition) commentTagDefinition);
                    commentTag.setValue("a flower");
                    
                    img.getTags().add(commentTag);
                }
                
                img.store();
                
//                DatabaseManager.getInstance().saveDatabase();
                
                Assert.assertTrue(true);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
