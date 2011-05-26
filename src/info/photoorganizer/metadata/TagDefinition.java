package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.util.Event;
import info.photoorganizer.util.Event.EventExecuter;

import java.util.UUID;

public abstract class TagDefinition extends DatabaseObject 
{
    
    
    private Event<TagDefinitionEventListener, TagDefinitionEvent> _tagChangedEvent = new Event<TagDefinitionEventListener, TagDefinitionEvent>(
            new EventExecuter<TagDefinitionEventListener, TagDefinitionEvent>()
            {
                public void fire(TagDefinitionEventListener listener, TagDefinitionEvent event)
                {
                    listener.tagChanged(event);
                }
            });
    
    private boolean applicableToImageRegion = false;
    private String name = null;

    public TagDefinition(DatabaseStorageStrategy storageContext)
    {
        this(null, null, storageContext);
    }

    public TagDefinition(String name, DatabaseStorageStrategy storageContext)
    {
        this(name, null, storageContext);
    }

    public TagDefinition(String name, UUID id, DatabaseStorageStrategy storageContext)
    {
        super(id, storageContext);
        this.name = name;
    }
    
    public void addTagEventListener(TagDefinitionEventListener listener)
    {
        _tagChangedEvent.addListener(listener);
    }
    
    protected void fireChangedEvent(TagDefinitionEvent event)
    {
        _tagChangedEvent.fire(event);
    }

    protected void fireChangedEvent()
    {
        fireChangedEvent(new TagDefinitionEvent(this));
    }

    public String getName()
    {
        return name;
    }
    
    public boolean isApplicableToImageRegion()
    {
        return applicableToImageRegion;
    }

    public void removeTagEventListener(TagDefinitionEventListener listener)
    {
        _tagChangedEvent.removeListener(listener);
    }

    public void setApplicableToImageRegion(boolean applicableToImageRegion)
    {
        this.applicableToImageRegion = applicableToImageRegion;
        fireChangedEvent();
    }
    
    public void setName(String name)
    {
        this.name = name;
        fireChangedEvent();
    }
    
    @Override
    public String toString()
    {
        return name;
    }

    public void remove() throws DatabaseStorageException
    {
        getStorageStrategy().removeTagDefinition(this);
    }
}
