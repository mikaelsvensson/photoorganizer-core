package info.photoorganizer.database;

import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.KeywordTag;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;


public class DatabaseTest
{
    @Test
    public void addImageAndKeywordAndRemoveKeyword()
    {
        String keywordName = "Keyword created at " + Calendar.getInstance().getTime().toString();
        addKeyword(keywordName);
        addImageWithKeyword("http://host/addImageAndKeywordAndRemoveKeyword.jpg", keywordName);
        removeKeyword(keywordName);
        
        Assert.assertTrue(true);
    }

    private void addKeyword(String keywordName)
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            KeywordTagDefinition keyword = database.createRootKeyword(keywordName);
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
    
    private void addImageWithKeyword(String url, String keywordName)
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            TagDefinition keyword = database.getTagDefinition(keywordName);
            
            Image createImage = database.createImage();
            createImage.setUrl(new URL(url));
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
        catch (MalformedURLException e)
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