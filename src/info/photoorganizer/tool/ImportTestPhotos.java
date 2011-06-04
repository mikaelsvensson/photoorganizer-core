package info.photoorganizer.tool;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseManager;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.metadata.DefaultTagDefinition;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.metadata.ValueTag;
import info.photoorganizer.metadata.ValueTagDefinition;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.io.File;
import java.io.IOException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Descriptor;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

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
                
                Image image = database.getImage(f);
                if (null != image)
                {
                    System.out.println("  Stored in database. UUID=" + image.getId() + ".");
                }
                else
                {
                    System.out.println("  Not in database. Yet. Adding image.");
                    database.indexImage(f);
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
