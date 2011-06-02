package info.photoorganizer.metadata;

import java.util.EventObject;

public class DatabaseObjectEvent extends EventObject
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public DatabaseObjectEvent(Object source)
    {
        super(source);
    }
    
    @Override
    public DatabaseObject getSource()
    {
        if (source instanceof DatabaseObject)
        {
            return (DatabaseObject) source;
        }
        return null;
    }
    
}
