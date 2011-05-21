package info.photoorganizer.metadata;

import info.photoorganizer.util.Event;
import info.photoorganizer.util.Event.EventExecuter;
import info.photoorganizer.util.StringUtils;
import info.photoorganizer.util.UUIDUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TagDefinition extends DatabaseObject 
{
    
    
    private Event<TagEventListener, TagEvent> _tagChangedEvent = new Event<TagEventListener, TagEvent>(
            new EventExecuter<TagEventListener, TagEvent>()
            {
                public void fire(TagEventListener listener, TagEvent event)
                {
                    listener.tagChanged(event);
                }
            });
    
    private boolean applicableToImageRegion = false;
    private String name = null;

    public TagDefinition()
    {
        this(null, null);
    }

    public TagDefinition(String name)
    {
        this(name, null);
    }

    public TagDefinition(String name, UUID id)
    {
        super(id);
        this.name = name;
    }
    
    public void addTagEventListener(TagEventListener listener)
    {
        _tagChangedEvent.addListener(listener);
    }
    
    protected void fireChangedEvent(TagEvent event)
    {
        _tagChangedEvent.fire(event);
    }

    protected void fireChangedEvent()
    {
        fireChangedEvent(new TagEvent(this));
    }

    public String getName()
    {
        return name;
    }
    
    public boolean isApplicableToImageRegion()
    {
        return applicableToImageRegion;
    }

    public void removeTagEventListener(TagEventListener listener)
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
}
