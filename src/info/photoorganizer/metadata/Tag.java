package info.photoorganizer.metadata;

public abstract class Tag<T extends TagDefinition>
{
    public void setDefinition(T definition)
    {
        _definition = definition;
    }

    private T _definition = null;
    
    private ImageRegion _region = null;

    public Tag(T definition)
    {
        super();
        _definition = definition;
    }

    public T getDefinition()
    {
        return _definition;
    }

    public ImageRegion getRegion()
    {
        return _region;
    }

    public void setRegion(ImageRegion region)
    {
        _region = region;
    }

    @Override
    public String toString()
    {
        return _definition.getName() + " (" + getClass().getSimpleName() + ")";
    }
}
