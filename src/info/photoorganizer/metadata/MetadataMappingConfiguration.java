package info.photoorganizer.metadata;

import info.photoorganizer.util.transform.TextTransformer;

import java.util.ArrayList;
import java.util.List;

public class MetadataMappingConfiguration
{
    private TagDefinition _target = null;
    private ImageFileMetadataTag _source = null;
    private List<TextTransformer> _sourceTextTransformers = null;

    public MetadataMappingConfiguration(ImageFileMetadataTag source, TagDefinition target)
    {
        super();
        _target = target;
        _source = source;
    }
    
    public MetadataMappingConfiguration()
    {
        this(null, null);
    }

    public TagDefinition getTarget()
    {
        return _target;
    }

    public void setTarget(TagDefinition target)
    {
        _target = target;
    }

    public ImageFileMetadataTag getSource()
    {
        return _source;
    }

    public void setSource(ImageFileMetadataTag source)
    {
        _source = source;
    }

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

    public String applySourceTextTransformations(String input)
    {
        for (TextTransformer transformer : _sourceTextTransformers)
        {
            input = transformer.transform(input);
        }
        return input;
    }
}
