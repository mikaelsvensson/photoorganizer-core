package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.database.autoindexing.DefaultIndexingConfiguration;
import info.photoorganizer.database.autoindexing.IndexingConfigurationInterface;
import info.photoorganizer.database.autoindexing.MetadataMappingConfigurationInterface;
import info.photoorganizer.database.autoindexing.POFileFilter;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IndexingConfiguration extends DatabaseObject implements IndexingConfigurationInterface
{
    private POFileFilter _fileFilter = null;

    private List<MetadataMappingConfigurationInterface> _metadataMappers = null;

    @Override
    public IndexingConfigurationInterface cloneDeep()
    {
        IndexingConfiguration clone = new IndexingConfiguration(getStorageStrategy());
        clone._fileFilter = _fileFilter.cloneDeep();
        if (_metadataMappers != null)
        {
            clone._metadataMappers = new ArrayList<MetadataMappingConfigurationInterface>();
            for (MetadataMappingConfigurationInterface metadataMapper : _metadataMappers)
            {
                clone._metadataMappers.add(metadataMapper.cloneDeep());
            }
        }
        return clone;
    }
    
    public IndexingConfiguration(DatabaseStorageStrategy storageStrategy)
    {
        super(null, storageStrategy);
    }

    public IndexingConfiguration(UUID id, DatabaseStorageStrategy storageStrategy)
    {
        super(id, storageStrategy);
    }

    /* (non-Javadoc)
     * @see info.photoorganizer.metadata.MetadataIndexingConfiguration#getFileFilter()
     */
    @Override
    public POFileFilter getFileFilter()
    {
        return _fileFilter;
    }

    /* (non-Javadoc)
     * @see info.photoorganizer.metadata.MetadataIndexingConfiguration#getMetadataMappers()
     */
    @Override
    public List<MetadataMappingConfigurationInterface> getMetadataMappers()
    {
        if (null == _metadataMappers)
        {
            _metadataMappers = new ArrayList<MetadataMappingConfigurationInterface>();
        }
        return _metadataMappers;
    }

    public void remove() throws DatabaseStorageException
    {
        getStorageStrategy().removeIndexingConfiguration(this);
    }

    public void setFileFilter(POFileFilter fileFilter)
    {
        _fileFilter = fileFilter;
    }

    public void store() throws DatabaseStorageException
    {
        getStorageStrategy().storeIndexingConfiguration(this);
    }

}
