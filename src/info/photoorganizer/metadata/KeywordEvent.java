package info.photoorganizer.metadata;


public class KeywordEvent extends TagEvent
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private int[] targetIndices = null;

    public int[] getTargetIndices()
    {
        return targetIndices;
    }

    public KeywordEvent(Object source)
    {
        super(source);
    }
    
    public KeywordEvent(Object source, KeywordTagDefinition target)
    {
        super(source, target);
        targetIndices = new int[] { getSource().getIndexOfChild(target) };
    }
    
    @Override
    public KeywordTagDefinition getSource()
    {
        if (source instanceof KeywordTagDefinition)
        {
            return (KeywordTagDefinition) source;
        }
        return null;
    }
    
}
