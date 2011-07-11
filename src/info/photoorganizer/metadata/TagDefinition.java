package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.util.Event;
import info.photoorganizer.util.Event.EventExecuter;

import java.util.UUID;

public abstract class TagDefinition extends DatabaseObject 
{
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -6122806956666432455L;
    
    transient private Event<TagDefinitionEventListener, TagDefinitionEvent> _tagChangedEvent = new Event<TagDefinitionEventListener, TagDefinitionEvent>(
            new EventExecuter<TagDefinitionEventListener, TagDefinitionEvent>()
            {
                public void fire(TagDefinitionEventListener listener, TagDefinitionEvent event)
                {
                    listener.tagChanged(event);
                }
            });
    transient private Event<TagDefinitionEventListener, TagDefinitionEvent> _keywordDeletedEvent = new Event<TagDefinitionEventListener, TagDefinitionEvent>(
            new EventExecuter<TagDefinitionEventListener, TagDefinitionEvent>()
            {
                public void fire(TagDefinitionEventListener listener, TagDefinitionEvent event)
                {
                    listener.tagDeleted(event);
                }
            });
    
    private boolean applicableToPhotoRegion = false;
    private String name = null;
    private boolean userAllowedToEditTags = false;
    private boolean userAllowedToCreateTags = false;

    public boolean isUserAllowedToEditTags()
    {
        return userAllowedToEditTags;
    }

    public void setUserAllowedToEditTags(boolean userAllowedToEditTags)
    {
        this.userAllowedToEditTags = userAllowedToEditTags;
    }

    public boolean isUserAllowedToCreateTags()
    {
        return userAllowedToCreateTags;
    }

    public void setUserAllowedToCreateTags(boolean userAllowedToCreateTags)
    {
        this.userAllowedToCreateTags = userAllowedToCreateTags;
    }

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
        _keywordDeletedEvent.addListener(listener);
    }
    
    protected void fireDeletedEvent(TagDefinitionEvent event)
    {
        _keywordDeletedEvent.fire(event);
    }
    
    protected void fireDeletedEvent()
    {
        _keywordDeletedEvent.fire(new TagDefinitionEvent(this));
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
    
    public boolean isApplicableToPhotoRegion()
    {
        return applicableToPhotoRegion;
    }

    public void removeTagEventListener(TagDefinitionEventListener listener)
    {
        _tagChangedEvent.removeListener(listener);
        _keywordDeletedEvent.removeListener(listener);
    }

    public void setApplicableToPhotoRegion(boolean applicableToImageRegion)
    {
        if (equals(this.applicableToPhotoRegion, applicableToImageRegion)) return;
        this.applicableToPhotoRegion = applicableToImageRegion;
        fireChangedEvent();
    }
    
    public void setName(String name)
    {
        if (equals(this.name, name)) return;
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
    
    public void store() throws DatabaseStorageException
    {
        getStorageStrategy().storeTagDefinition(this);
    }
    
}
