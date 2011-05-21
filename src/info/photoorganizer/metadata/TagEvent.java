package info.photoorganizer.metadata;

import java.util.EventObject;

public class TagEvent extends EventObject
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private TagDefinition[] targets = null;

    public TagDefinition[] getTargets()
    {
        return targets;
    }

    public TagEvent(Object source)
    {
        super(source);
    }
    
    public TagEvent(Object source, TagDefinition target)
    {
        super(source);
        targets = new TagDefinition[] { target };
    }
    
    @Override
    public TagDefinition getSource()
    {
        if (source instanceof TagDefinition)
        {
            return (TagDefinition) source;
        }
        return null;
    }
    
}
