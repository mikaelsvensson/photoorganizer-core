package info.photoorganizer.metadata;

import java.util.EventObject;

public class TagDefinitionEvent extends EventObject
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

    public TagDefinitionEvent(Object source)
    {
        super(source);
    }
    
    public TagDefinitionEvent(Object source, TagDefinition target)
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
