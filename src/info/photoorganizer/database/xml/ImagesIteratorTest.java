package info.photoorganizer.database.xml;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseManager;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.util.StringUtils;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.util.Iterator;

import org.junit.Test;

public class ImagesIteratorTest
{
    @Test
    public void printAll()
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            Iterator<Image> images = database.getImages();
            while (images.hasNext())
            {
                Image image = images.next();
                System.out.format("Information about image %s:\n", image.getId().toString());
                System.out.format("   URL: %s\n", image.getUrl());
                System.out.format("   Tags: %s\n", StringUtils.join(image.getTags(), ", "));
            }
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
