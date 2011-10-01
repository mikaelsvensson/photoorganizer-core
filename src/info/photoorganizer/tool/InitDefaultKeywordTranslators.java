package info.photoorganizer.tool;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseManager;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.autoindexing.MetadataMappingConfiguration;
import info.photoorganizer.metadata.AutoIndexTagDefinition;
import info.photoorganizer.metadata.DefaultTagDefinition;
import info.photoorganizer.metadata.IndexingConfiguration;
import info.photoorganizer.metadata.RegexpFileFilter;
import info.photoorganizer.metadata.PhotoFileMetadataTag;
import info.photoorganizer.util.config.ConfigurationProperty;
import info.photoorganizer.util.transform.ReplaceTransformer;

public class InitDefaultKeywordTranslators
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Database database = DatabaseManager.getInstance().openDatabase(ConfigurationProperty.dbPath.get());
        try
        {
            IndexingConfiguration cfg = database.createIndexingConfiguration();
            
            {
                MetadataMappingConfiguration mappingCfg = new MetadataMappingConfiguration(/*database*/);
                mappingCfg.setSource(PhotoFileMetadataTag.IPTC_SUPPLEMENTAL_CATEGORIES);
                mappingCfg.getSourceTextTransformers().add(new ReplaceTransformer(".", " "));
                mappingCfg.setTarget(database.getTagDefinition(DefaultTagDefinition.ROOT_KEYWORD.getId()));
                cfg.getMetadataMappers().add(mappingCfg);
            }
            
            for (AutoIndexTagDefinition aitd : AutoIndexTagDefinition.values())
            {
                cfg.getMetadataMappers().add(
                        new MetadataMappingConfiguration(
                                aitd.getFileTag(), 
                                database.getTagDefinition(aitd.getTargetTagDefinitionId())/*,
                                database*/));
            }
            
            cfg.setFileFilter(new RegexpFileFilter("^soccer_.*"));
            
            cfg.store();
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
