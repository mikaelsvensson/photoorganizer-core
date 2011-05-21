package info.photoorganizer.metadata;

public class Tag<T extends TagDefinition>
{
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
}
