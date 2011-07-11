package info.photoorganizer.tool;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseManager;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.metadata.Photo;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.io.File;

public class ImportTestPhotos
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            for (File f : (new File("resources\\testphotos")).listFiles())
            {
                System.out.println(f.getAbsolutePath());
                
                Photo image = database.getPhoto(f);
                if (null != image)
                {
                    System.out.println("  Stored in database. UUID=" + image.getId() + ".");
                }
                else
                {
                    System.out.println("  Not in database. Yet. Adding image.");
                    database.indexPhoto(f);
                }
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
