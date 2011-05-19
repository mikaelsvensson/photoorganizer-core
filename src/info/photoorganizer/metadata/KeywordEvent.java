package info.photoorganizer.metadata;

import java.util.EventObject;

public class KeywordEvent extends EventObject
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Keyword[] targets = null;
    private int[] targetIndices = null;

    public Keyword[] getTargets()
    {
        return targets;
    }

    public int[] getTargetIndices()
    {
        return targetIndices;
    }

    public KeywordEvent(Object source)
    {
        super(source);
    }
    
    public KeywordEvent(Object source, Keyword target)
    {
        super(source);
        targets = new Keyword[] { target };
        targetIndices = new int[] { getSource().getIndexOfChild(target) };
    }
    
    @Override
    public Keyword getSource()
    {
        if (source instanceof Keyword)
        {
            return (Keyword) source;
        }
        return null;
    }
    
}
