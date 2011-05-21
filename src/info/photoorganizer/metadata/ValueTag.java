package info.photoorganizer.metadata;


public class ValueTag<V extends Object, T extends ValueTagDefinition> extends Tag<T>
{
    
    public ValueTag(T definition)
    {
        super(definition);
    }

    private V _value = null;

    public V getValue()
    {
        return _value;
    }

    public void setValue(V value)
    {
        _value = value;
    }
    
}
