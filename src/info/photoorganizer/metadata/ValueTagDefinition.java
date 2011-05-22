package info.photoorganizer.metadata;

import java.util.UUID;

public abstract class ValueTagDefinition extends TagDefinition
{
    
    public ValueTagDefinition()
    {
        super();
    }

    public ValueTagDefinition(String name)
    {
        super(name);
    }

    public ValueTagDefinition(String name, UUID id)
    {
        super(name, id);
    }
    
}
