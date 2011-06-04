package info.photoorganizer.metadata;

public class KeywordTag extends Tag<KeywordTagDefinition>
{

    public KeywordTag(KeywordTagDefinition definition)
    {
        super(definition);
    }

    @Override
    public String toString()
    {
        return "Keyword: " + getDefinition().getName();
    }

}
