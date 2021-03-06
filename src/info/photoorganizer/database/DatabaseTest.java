package info.photoorganizer.database;

import info.photoorganizer.metadata.DatabaseException;
import info.photoorganizer.metadata.KeywordTag;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.Photo;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;


public class DatabaseTest
{
    @Test
    public void addPhotoAndKeywordAndRemoveKeyword()
    {
        String keywordName = "Keyword created at " + Calendar.getInstance().getTime().toString();
        addKeyword(keywordName);
        addPhotoWithKeyword("http://host/addImageAndKeywordAndRemoveKeyword.jpg", keywordName);
        removeKeyword(keywordName);
        
        Assert.assertTrue(true);
    }

    private void addKeyword(String keywordName)
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            KeywordTagDefinition keyword = database.addRootKeyword(keywordName);
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
    private void removeKeyword(String keywordName)
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            TagDefinition keyword = database.getTagDefinition(keywordName);
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
    
    private void addPhotoWithKeyword(String uri, String keywordName)
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            TagDefinition keyword = database.getTagDefinition(keywordName);
            
            Photo createImage = database.createPhoto();
            createImage.setURI(new URI(uri));
            if (keyword instanceof KeywordTagDefinition)
            {
                createImage.addTag(new KeywordTag((KeywordTagDefinition) keyword));
            }
            createImage.store();
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
    }
}
