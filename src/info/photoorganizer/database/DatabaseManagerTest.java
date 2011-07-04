package info.photoorganizer.database;

import info.photoorganizer.metadata.DatabaseException;
import info.photoorganizer.metadata.DefaultTagDefinition;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.KeywordTag;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.metadata.TextTag;
import info.photoorganizer.metadata.TextTagDefinition;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.net.URI;
import java.net.URISyntaxException;

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
            KeywordTagDefinition keyword = database.addRootKeyword(KEYWORD_NAME);
            keyword.store();
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                database.close();
            }
            catch (DatabaseStorageException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
            try
            {
                database.close();
            }
            catch (DatabaseStorageException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    @Test
    public void addSynonyms()
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            
            KeywordTagDefinition jfk = database.addRootKeyword("JKF");
            KeywordTagDefinition johnFKennedy = database.addRootKeyword("John F Kennedy");
            
            KeywordTagDefinition.addSynonym(jfk, johnFKennedy, false);
            
            jfk.store();
            johnFKennedy.store();

//                DatabaseManager.getInstance().saveDatabase();
            
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                database.close();
            }
            catch (DatabaseStorageException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Assert.assertTrue(true);
    }
    
    @Test
    public void addImage()
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            Image img = database.createImage();
            img.setURI(new URI("http://demo/test.jpg"));
            
            TagDefinition tagDefinition = database.getTagDefinition(DatabaseManager.KEYWORD_OBJECTS);
            if (tagDefinition instanceof KeywordTagDefinition)
            {
                img.addTag(new KeywordTag((KeywordTagDefinition) tagDefinition));
            }
            
            TagDefinition commentTagDefinition = database.getTagDefinition(DefaultTagDefinition.COMMENT.getId());
            if (commentTagDefinition instanceof TextTagDefinition)
            {
                TextTag commentTag = new TextTag((TextTagDefinition) commentTagDefinition);
                commentTag.setValue("a flower");
                
                img.addTag(commentTag);
            }
            
            img.store();
            
//                DatabaseManager.getInstance().saveDatabase();
            
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (DatabaseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                database.close();
            }
            catch (DatabaseStorageException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Assert.assertTrue(true);
    }
}
