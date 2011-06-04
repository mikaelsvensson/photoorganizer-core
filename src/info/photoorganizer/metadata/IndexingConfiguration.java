package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.util.transform.TextTransformer;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IndexingConfiguration extends DatabaseObject
{
    private FileFilter _fileFilter = null;

    private List<MetadataMappingConfiguration> _metadataMappers = null;

    public IndexingConfiguration(DatabaseStorageStrategy storageStrategy)
    {
        super(null, storageStrategy);
    }

    public IndexingConfiguration(UUID id,
            DatabaseStorageStrategy storageStrategy)
    {
        super(id, storageStrategy);
    }

    public FileFilter getFileFilter()
    {
        return _fileFilter;
    }

    public List<MetadataMappingConfiguration> getMetadataMappers()
    {
        if (null == _metadataMappers)
        {
            _metadataMappers = new ArrayList<MetadataMappingConfiguration>();
        }
        return _metadataMappers;
    }

    public void remove() throws DatabaseStorageException
    {
        getStorageStrategy().removeIndexingConfiguration(this);
    }

    public void setFileFilter(FileFilter fileFilter)
    {
        _fileFilter = fileFilter;
    }

    public void store() throws DatabaseStorageException
    {
        getStorageStrategy().storeIndexingConfiguration(this);
    }

}
