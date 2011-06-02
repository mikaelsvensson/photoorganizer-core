package info.photoorganizer.tool;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseManager;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.metadata.DefaultTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.config.ConfigurationProperty;

public class InitDefaultTagDefinitions
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            for (DefaultTagDefinition def : DefaultTagDefinition.values())
            {
                TagDefinition definition = database.getTagDefinition(def.getId());
                if (null == definition)
                {
                    TagDefinition tagDefinition = database.createTagDefinition(def.getDefinitionClass(), def.toString());
                    tagDefinition.setId(def.getId());
                    tagDefinition.store();
                }
            }
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

}
