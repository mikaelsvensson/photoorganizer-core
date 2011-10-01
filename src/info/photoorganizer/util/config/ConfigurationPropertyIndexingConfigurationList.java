package info.photoorganizer.util.config;

import info.photoorganizer.database.autoindexing.IndexingConfigurationList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

public class ConfigurationPropertyIndexingConfigurationList extends ConfigurationProperty<IndexingConfigurationList>
{

    protected ConfigurationPropertyIndexingConfigurationList(/*final Database database*/)
    {
        super(new DefaultValueReader<IndexingConfigurationList>()
        {
            @Override
            public IndexingConfigurationList get()
            {
                IndexingConfigurationList list = new IndexingConfigurationList();
                /*
                DefaultIndexingConfiguration configuration = new DefaultIndexingConfiguration();
                configuration.setFileFilter(new FileFilter()
                {
                    private HashSet<String> rejectedFileTypes = null;
                    
                    private HashSet<String> getRejectedFileTypes()
                    {
                        if (null == rejectedFileTypes)
                        {
                            rejectedFileTypes = new HashSet<String>();
                            rejectedFileTypes.addAll(Arrays.asList("info", "thumbs", "db", "txt"));
                        }
                        return rejectedFileTypes;
                    }
                    
                    @Override
                    public boolean accept(File f)
                    {
                        String fileType = f.getName().substring(f.getName().lastIndexOf('.')+1);
                        return !getRejectedFileTypes().contains(fileType);
                    }
                });
                List<MetadataMappingConfigurationInterface> metadataMappers = configuration.getMetadataMappers();
                {
                    MetadataMappingConfiguration mappingCfg = new MetadataMappingConfiguration();
                    mappingCfg.setSource(PhotoFileMetadataTag.IPTC_SUPPLEMENTAL_CATEGORIES);
                    mappingCfg.getSourceTextTransformers().add(new ReplaceTransformer(".", " "));
                    mappingCfg.setTarget(database.getTagDefinition(DefaultTagDefinition.ROOT_KEYWORD.getId()));
                    configuration.getMetadataMappers().add(mappingCfg);
                }
                
                for (AutoIndexTagDefinition aitd : AutoIndexTagDefinition.values())
                {
                    configuration.getMetadataMappers().add(
                            new MetadataMappingConfiguration(
                                    aitd.getFileTag(), 
                                    database.getTagDefinition(aitd.getTargetTagDefinitionId())));
                }
                list.add(configuration);
                 */
                return list;
            }
        });
    }

    @Override
    public IndexingConfigurationList get()
    {
        String value = getPrefs().get(name(), null);
        if (null != value)
        {
            try
            {
                ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(value));
                ObjectInputStream in = new ObjectInputStream(bais);
                return (IndexingConfigurationList) in.readObject();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return getDefaultValue();
    }

    @Override
    public void set(IndexingConfigurationList value)
    {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(value);
            getPrefs().put(name(), Base64.encodeBase64String(baos.toByteArray()));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }


}
