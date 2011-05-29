package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.util.Event;
import info.photoorganizer.util.Event.EventExecuter;
import info.photoorganizer.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class KeywordTagDefinition extends TagDefinition
{

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
    
    protected void addSynonymId(UUID synonymId)
    {
        _synonyms.add(synonymId);
        
//        KeywordTagDefinition synonym = (KeywordTagDefinition) getStorageStrategy().getTagDefinition(synonymId);
//        synonym._synonyms.add(getId());
    }

    /**
     * Low-level method. Should NOT be invoked by API users, only by
     * DatabaseStorageStrategy implementations.
     * 
     * @param synonymIds
     */
    public void setSynonymIds(UUID[] synonymIds)
    {
        _synonyms.clear();
        for (UUID synonymId : synonymIds)
        {
            _synonyms.add(synonymId);
        }
    }
    
    public static void addSynonym(KeywordTagDefinition valueA, KeywordTagDefinition value1, boolean autoSave) throws DatabaseStorageException
    {
        value1.addSynonymId(valueA.getId());
        valueA.addSynonymId(value1.getId());
        if (autoSave)
        {
            valueA.store();
            value1.store();
        }
    }
    
    public static void removeSynonym(KeywordTagDefinition valueA, KeywordTagDefinition value1, boolean autoSave) throws DatabaseStorageException
    {
        value1.removeSynonymId(valueA.getId());
        valueA.removeSynonymId(value1.getId());
        if (autoSave)
        {
            valueA.store();
            value1.store();
        }
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
        KeywordTagDefinition[] synonyms = new KeywordTagDefinition[_synonyms.size()];
        int i = 0;
        for (UUID id : _synonyms)
        {
            synonyms[i++] = getStorageStrategy().getTagDefinition(id, KeywordTagDefinition.class);
        }
        return synonyms;
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

    protected void removeSynonymId(UUID synonymId)
    {
        _synonyms.remove(synonymId);
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
        }
        removeTagsForTagDefinition();
        
        getStorageStrategy().removeTagDefinition(this);
    }

    protected void removeTagsForTagDefinition() throws DatabaseStorageException
    {
        Iterator<Image> images = getStorageStrategy().getImagesWithTag(this);
        while (images.hasNext())
        {
            Image image = images.next();
            Iterator<Tag<? extends TagDefinition>> tags = image.getTags();
            while (tags.hasNext())
            {
                Tag<? extends TagDefinition> t = tags.next();
                if (t.getDefinition().equals(this))
                {
                    tags.remove();
                }
            }
            image.store();
        }
    }
    
    public boolean isDirty()
    {
        return false;
    }

}
