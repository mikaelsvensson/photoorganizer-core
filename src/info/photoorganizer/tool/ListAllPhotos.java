package info.photoorganizer.tool;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseManager;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.metadata.Photo;
import info.photoorganizer.util.StringUtils;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.util.Iterator;

public class ListAllPhotos
{
    public static void main(String[] args) 
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            Iterator<Photo> images = database.getPhotos();
            while (images.hasNext())
            {
                Photo image = images.next();
                System.out.format("Information about image %s:\n", image.getId().toString());
                System.out.format("  URI:  %s\n", image.getURI());
                System.out.format("  Tags: %s\n", StringUtils.join(image.getTags(), "\n        "));
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
