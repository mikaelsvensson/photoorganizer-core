package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.util.Event;
import info.photoorganizer.util.Event.EventExecuter;
import info.photoorganizer.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class KeywordTagDefinition extends TagDefinition
{

//    public static final KeywordTagDefinition ROOT_KEYWORD = new KeywordTagDefinition("root keyword", UUID.fromString("f4840997-b116-43e6-8256-6fbaefb63b3d"));
//  public static final KeywordTagDefinition EVENTS = new KeywordTagDefinition("events", UUID.fromString("8a432af2-6601-4810-bc83-caf82e2930e1"));
//  public static final KeywordTagDefinition LOCATIONS = new KeywordTagDefinition("locations", UUID.fromString("e3fa5f30-673c-435c-b5fa-a9a67f27abd1"));
//  public static final KeywordTagDefinition OBJECTS = new KeywordTagDefinition("objects", UUID.fromString("7203b041-6a98-4e35-ae47-9f2723517c01"));
//  public static final KeywordTagDefinition PEOPLE = new KeywordTagDefinition("people", UUID.fromString("750a8d79-e975-4c1b-af6e-cdebd23e9db7"));

    public static final char DEFAULT_KEYWORD_QUOTATION_MARK = '"';
    
    public static final char DEFAULT_KEYWORD_SEPARATOR = ' ';
    
    public KeywordTagDefinition(String name, UUID id, KeywordTagDefinition parent, DatabaseStorageStrategy storageContext)
    {
        super(name, id, storageContext);
        if (null != parent)
        {
            parent.addChild(this);
        }
    }

    public KeywordTagDefinition(String name, UUID id, DatabaseStorageStrategy storageContext)
    {
        super(name, id, storageContext);
    }

    public KeywordTagDefinition(String name, DatabaseStorageStrategy storageContext)
    {
        super(name, storageContext);
    }
    
    public KeywordTagDefinition(DatabaseStorageStrategy storageContext)
    {
        super(storageContext);
    }

    private final KeywordEventListener _childListener = new KeywordEventListener()
    {
        
        @Override
        public void tagChanged(TagDefinitionEvent event)
        {
            fireChangedEvent(event);
        }
        
        @Override
        public void keywordDeleted(KeywordEvent event)
        {
            fireDeletedEvent(event);
        }
        
        @Override
        public void keywordInserted(KeywordEvent event)
        {
            fireInsertedEvent(event);
        }
        
        @Override
        public void keywordStructureChanged(KeywordEvent event)
        {
            fireStructureChangedEvent(event);
        }
    };
    
    private Event<KeywordEventListener, KeywordEvent> _keywordDeletedEvent = new Event<KeywordEventListener, KeywordEvent>(
            new EventExecuter<KeywordEventListener, KeywordEvent>()
            {
                public void fire(KeywordEventListener listener, KeywordEvent event)
                {
                    listener.keywordDeleted(event);
                }
            });
    private Event<KeywordEventListener, KeywordEvent> _keywordInsertedEvent = new Event<KeywordEventListener, KeywordEvent>(
            new EventExecuter<KeywordEventListener, KeywordEvent>()
            {
                public void fire(KeywordEventListener listener, KeywordEvent event)
                {
                    listener.keywordInserted(event);
                }
            });
    
    private Event<KeywordEventListener, KeywordEvent> _keywordStructureChangedEvent = new Event<KeywordEventListener, KeywordEvent>(
            new EventExecuter<KeywordEventListener, KeywordEvent>()
            {
                public void fire(KeywordEventListener listener, KeywordEvent event)
                {
                    listener.keywordStructureChanged(event);
                }
            });

    private List<KeywordTagDefinition> children = new ArrayList<KeywordTagDefinition>(); 
    private KeywordTagDefinition parent = null;
    private Set<UUID> _synonyms = new HashSet<UUID>();
    private Location location = null;

    public void addChild(KeywordTagDefinition tag)
    {
        if (tag.getParent() != null)
        {
            tag.moveTo(this);
        }
        else
        {
            children.add(tag);
            tag.parent = this;
        }
        fireInsertedEvent(new KeywordEvent(this, tag));
        tag.addKeywordEventListener(_childListener);
    }
    
    public void addChildren(Iterable<KeywordTagDefinition> tags)
    {
        for (KeywordTagDefinition t : tags)
        {
            addChild(t);
        }
    }
    
    public void addChildren(KeywordTagDefinition... tags)
    {
        for (KeywordTagDefinition t : tags)
        {
            addChild(t);
        }
    }

    public void addKeywordEventListener(KeywordEventListener listener)
    {
        addTagEventListener(listener);
        _keywordDeletedEvent.addListener(listener);
        _keywordInsertedEvent.addListener(listener);
        _keywordStructureChangedEvent.addListener(listener);
    }
    
    
    private void fireDeletedEvent(KeywordEvent event)
    {
        _keywordDeletedEvent.fire(event);
    }
    
    private void fireInsertedEvent(KeywordEvent event)
    {
        _keywordInsertedEvent.fire(event);
    }
    
    private void fireStructureChangedEvent(KeywordEvent event)
    {
        _keywordStructureChangedEvent.fire(event);
    }
    
    public KeywordTagDefinition getChild(int index)
    {
        return index >= 0 && index < children.size() ? children.get(index) : null;
    }
    
    public KeywordTagDefinition getChildById(UUID id)
    {
        return getChildById(id, false);
    }
    
    public KeywordTagDefinition getChildById(UUID id, boolean recursive)
    {
        if (null != id)
        {
            for (KeywordTagDefinition k : children)
            {
                if (id.equals(k.getId()))
                {
                    return k;
                }
                else if (recursive)
                {
                    KeywordTagDefinition recursiveMatch = k.getChildById(id, recursive);
                    if (null != recursiveMatch)
                    {
                        return recursiveMatch;
                    }
                }
        }
        }
        return null;
    }
    
    public KeywordTagDefinition getChildByName(String name)
    {
        return getChildByName(name, false);
    }
    
    public KeywordTagDefinition getChildByName(String name, boolean recursive)
    {
        for (KeywordTagDefinition k : children)
        {
            if (k.getName().equals(name))
            {
                return k;
            }
            else if (recursive)
            {
                KeywordTagDefinition recursiveMatch = k.getChildByName(name, recursive);
                if (null != recursiveMatch)
                {
                    return recursiveMatch;
                }
            }
        }
        return null;
    }
    
    public int getChildCount()
    {
        return getChildCount(false);
    }
    
    public int getChildCount(boolean recursive)
    {
        int sum = children.size();
        if (recursive)
        {
            for (KeywordTagDefinition child : children)
            {
                sum += child.getChildCount(recursive);
            }
        }
        return sum;
    }
    
    public Iterable<KeywordTagDefinition> getChildren()
    {
        return Collections.unmodifiableCollection(children);
    }
    
    public int getDepth()
    {
        int depth = 0;
        for (KeywordTagDefinition k = this; k != null; k = k.getParent(), depth++)
            ;
        return depth;
    }
    
    public KeywordTagDefinition[] getDescendants()
    {
        List<KeywordTagDefinition> descendants = new ArrayList<KeywordTagDefinition>();
        getDescendants(descendants);
        return descendants.toArray(new KeywordTagDefinition[0]);
    }
    
    private void getDescendants(List<KeywordTagDefinition> res)
    {
        for (KeywordTagDefinition c : children)
        {
            res.add(c);
            c.getDescendants(res);
        }
    }
    

    public int getIndexOfChild(KeywordTagDefinition child)
    {
        return children.indexOf(child);
    }
    
    public KeywordTagDefinition getParent()
    {
        return parent;
    }
    
    public KeywordTagDefinition[] getPath()
    {
        int depth = getDepth();
        KeywordTagDefinition[] path = new KeywordTagDefinition[depth];
        for (KeywordTagDefinition k = this; k != null; k = k.getParent(), depth--)
        {
            path[depth - 1] = k;
        }
        return path;
    }
    
    
    public void moveTo(KeywordTagDefinition newParent)
    {
        if (newParent != this && newParent != null)
        {
            parent.removeChild(this);
            newParent.addChild(this);
        }
    }
    
    public void removeChild(KeywordTagDefinition keyword)
    {
        KeywordEvent event = new KeywordEvent(this, keyword); // Before remove(...) so that the current child index can be computed.
        children.remove(keyword);
        keyword.parent = null;
        fireDeletedEvent(event);
        keyword.removeKeywordEventListener(_childListener);
    }
    
    public void removeKeywordEventListener(KeywordEventListener listener)
    {
        _keywordDeletedEvent.removeListener(listener);
        _keywordInsertedEvent.removeListener(listener);
        _keywordStructureChangedEvent.removeListener(listener);
    }
    
    public void addSynonym(UUID synonymId)
    {
        _synonyms.add(synonymId);
        
        KeywordTagDefinition synonym = (KeywordTagDefinition) getStorageStrategy().getTagDefinition(synonymId);
        synonym._synonyms.add(getId());
    }

    public Location getLocation()
    {
        return location;
    }

    public UUID[] getSynonymIds()
    {
        UUID[] ids = new UUID[_synonyms.size()];
        int i = 0;
        for (UUID s : _synonyms)
        {
            ids[i++] = s;
        }
        return ids;
    }

    public String getSynonymIds(char separatorChar, char quotationChar)
    {
        return StringUtils.join(getSynonymIds(), separatorChar, quotationChar);
    }

    public KeywordTagDefinition[] getSynonyms()
    {
        return _synonyms.toArray(new KeywordTagDefinition[0]);
    }

    public String getSynonyms(char separatorChar, char quotationChar)
    {
        return StringUtils.join(_synonyms, separatorChar, quotationChar);
    }

    public void removeAllSynonyms()
    {
        for (UUID s : _synonyms)
        {
            KeywordTagDefinition synonym = (KeywordTagDefinition) getStorageStrategy().getTagDefinition(s);
            synonym._synonyms.remove(this);
        }
        _synonyms.clear();
    }

    public void removeSynonym(KeywordTagDefinition keyword)
    {
        _synonyms.remove(keyword);
        keyword._synonyms.remove(this);
    }

    public void setLocation(Location location)
    {
        this.location = location;
        fireChangedEvent(new TagDefinitionEvent(this));
    }

    public void store() throws DatabaseStorageException
    {
        getStorageStrategy().storeTagDefinition(this);
    }
    
    public void remove() throws DatabaseStorageException
    {
        if (parent != null)
        {
            parent.removeChild(this);
            getStorageStrategy().removeTagDefinition(this);
        }
    }

}
