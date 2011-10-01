package info.photoorganizer.database.autoindexing;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseManager;
import info.photoorganizer.metadata.PhotoFileMetadataTag;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.transform.TextTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MetadataMappingConfiguration implements MetadataMappingConfigurationInterface
{
    @Override
    public String toString()
    {
        return _source + ", " + _targetId + " (" + _sourceTextTransformers + ")";
    }

    @Override
    public MetadataMappingConfigurationInterface cloneDeep()
    {
        MetadataMappingConfiguration clone = new MetadataMappingConfiguration(/*_database*/);
        clone._targetId = _targetId;
        clone._source = _source;
        if (_sourceTextTransformers != null)
        {
            clone._sourceTextTransformers = new ArrayList<TextTransformer>();
            for (TextTransformer sourceTextTransformer : _sourceTextTransformers)
            {
                clone._sourceTextTransformers.add(sourceTextTransformer.cloneDeep());
            }
        }
        return clone;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
//    private Database _database = null;
//    private TagDefinition _target = null;
    private UUID _targetId = null;
    private PhotoFileMetadataTag _source = null;
//    private UUID _sourceId = null;
    private List<TextTransformer> _sourceTextTransformers = null;

    public MetadataMappingConfiguration(PhotoFileMetadataTag source, TagDefinition target/*, Database database*/)
    {
        super();
        setTarget(target);
//        _target = target;
        _source = source;
//        _database = database;
    }
    
//    public MetadataMappingConfiguration()
//    {
//    }

    public MetadataMappingConfiguration(/*Database database*/)
    {
        this(null, null/*, database*/);
    }

    @Override
//    @Deprecated
    public TagDefinition getTarget(Database database)
    {
        if (database != null)
        {
            return database.getTagDefinition(_targetId);
        }
        else
        {
            return null;
        }
//        return _target;
    }

    @Override
    public void setTarget(TagDefinition target)
    {
        _targetId = target != null ? target.getId() : null;
//        _target = target;
    }

    @Override
//    @Deprecated
    public PhotoFileMetadataTag getSource()
    {
        return _source;
    }

    @Override
    public void setSource(PhotoFileMetadataTag source)
    {
        _source = source;
    }

    @Override
    public List<TextTransformer> getSourceTextTransformers()
    {
        if (null == _sourceTextTransformers)
        {
            _sourceTextTransformers = new ArrayList<TextTransformer>();
        }
        return _sourceTextTransformers;
    }

    public void setSourceTextTransformers(List<TextTransformer> sourceTextTransformers)
    {
        _sourceTextTransformers = sourceTextTransformers;
    }

    @Override
    public String applySourceTextTransformations(String input)
    {
        for (TextTransformer transformer : _sourceTextTransformers)
        {
            input = transformer.transform(input);
        }
        return input;
    }

//    @Override
//    public UUID getSourceId()
//    {
//        return _sourceId;
//    }
//
//    @Override
//    public UUID getTargetId()
//    {
//        return _targetId;
//    }
//
//    @Override
//    public void setSourceId(UUID id)
//    {
//        _sourceId = id;
//    }
//
//    @Override
//    public void setTargetId(UUID id)
//    {
//        _targetId = id;
//    }
}
