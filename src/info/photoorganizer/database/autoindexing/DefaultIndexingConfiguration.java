package info.photoorganizer.database.autoindexing;

import java.util.ArrayList;
import java.util.List;

public class DefaultIndexingConfiguration implements IndexingConfigurationInterface
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public IndexingConfigurationInterface cloneDeep()
    {
        DefaultIndexingConfiguration clone = new DefaultIndexingConfiguration();
        clone._fileFilter = _fileFilter.cloneDeep();
        if (_metadataMappers != null)
        {
            clone._metadataMappers = new ArrayList<MetadataMappingConfigurationInterface>();
            for (MetadataMappingConfigurationInterface metadataMapper : _metadataMappers)
            {
                if (null != metadataMapper)
                {
                    clone._metadataMappers.add(metadataMapper.cloneDeep());
                }
            }
        }
        return clone;
    }

    private POFileFilter _fileFilter = null;

    private List<MetadataMappingConfigurationInterface> _metadataMappers = null;
    
    @Override
    public POFileFilter getFileFilter()
    {
        return _fileFilter;
    }

    @Override
    public List<MetadataMappingConfigurationInterface> getMetadataMappers()
    {
        if (null == _metadataMappers)
        {
            _metadataMappers = new ArrayList<MetadataMappingConfigurationInterface>();
        }
        return _metadataMappers;
    }

    @Override
    public synchronized void setFileFilter(POFileFilter fileFilter)
    {
        _fileFilter = fileFilter;
    }

    public synchronized void setMetadataMappers(List<MetadataMappingConfigurationInterface> metadataMappers)
    {
        _metadataMappers = metadataMappers;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(_fileFilter).append('\n');
        for (MetadataMappingConfigurationInterface mapper : getMetadataMappers())
        {
            sb.append(mapper).append('\n');
        }
        return sb.substring(0, 100).toString();
    }
    
}
