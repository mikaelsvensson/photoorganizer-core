package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseItem;
import info.photoorganizer.util.Event;
import info.photoorganizer.util.StringUtils;
import info.photoorganizer.util.UUIDUtilities;
import info.photoorganizer.util.Event.EventExecuter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.omg.CORBA._IDLTypeStub;

public class Keyword implements DatabaseItem
{
    public static final char DEFAULT_KEYWORD_QUOTATION_MARK = '"';
    
    public static final char DEFAULT_KEYWORD_SEPARATOR = ' ';
    
    private final KeywordEventListener _childListener = new KeywordEventListener()
    {
        
        @Override
        public void keywordChanged(KeywordEvent event)
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
    
    private Event<KeywordEventListener, KeywordEvent> _keywordChangedEvent = new Event<KeywordEventListener, KeywordEvent>(
            new EventExecuter<KeywordEventListener, KeywordEvent>()
            {
                public void fire(KeywordEventListener listener, KeywordEvent event)
                {
                    listener.keywordChanged(event);
                }
            });
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
    
    private Set<Keyword> _synonyms = new HashSet<Keyword>();
    private List<Keyword> children = new ArrayList<Keyword>(); 
    private UUID id = null;
    private Location location = null;
    private String name = null;
    
    private Keyword parent = null;

    public Keyword(String name)
    {
        this(name, null, null);
    }

    public Keyword(String name, UUID id)
    {
        this(name, id, null);
    }

    public Keyword(String name, UUID id, Keyword parent)
    {
        super();
        this.name = name;
        this.id = (id != null ? id : UUIDUtilities.generateUuid());
        if (null != parent)
        {
            parent.addChild(this);
        }
    }

    public void addChild(Keyword keyword)
    {
        if (keyword.getParent() != null)
        {
            keyword.moveTo(this);
        }
        else
        {
            children.add(keyword);
            keyword.parent = this;
        }
        fireInsertedEvent(new KeywordEvent(this, keyword));
        keyword.addKeywordEventListener(_childListener);
    }

//    public void setId(String id)
//    {
//        this.id = id;
//    }

    public void addKeywordEventListener(KeywordEventListener listener)
    {
        _keywordChangedEvent.addListener(listener);
        _keywordDeletedEvent.addListener(listener);
        _keywordInsertedEvent.addListener(listener);
        _keywordStructureChangedEvent.addListener(listener);
    }
    public void addSynonym(Keyword keyword)
    {
        _synonyms.add(keyword);
        keyword._synonyms.add(this);
    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Keyword other = (Keyword) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
    }
    
    public Keyword getChild(int index)
    {
        return index >= 0 && index < children.size() ? children.get(index) : null;
    }
    
    public Keyword getChildById(UUID id)
    {
        return getChildById(id, false);
    }
    
    public Keyword getChildById(UUID id, boolean recursive)
    {
        if (null != id)
        {
            for (Keyword k : children)
            {
                if (id.equals(k.getId()))
                {
                    return k;
                }
                else if (recursive)
                {
                    Keyword recursiveMatch = k.getChildById(id, recursive);
                    if (null != recursiveMatch)
                    {
                        return recursiveMatch;
                    }
                }
        }
        }
        return null;
    }
    
    public Keyword getChildByName(String name)
    {
        return getChildByName(name, false);
    }
    
    public Keyword getChildByName(String name, boolean recursive)
    {
        for (Keyword k : children)
        {
            if (k.getName().equals(name))
            {
                return k;
            }
            else if (recursive)
            {
                Keyword recursiveMatch = k.getChildByName(name, recursive);
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
            for (Keyword child : children)
            {
                sum += child.getChildCount(recursive);
            }
        }
        return sum;
    }
    
    public Keyword[] getChildren()
    {
        return children.toArray(new Keyword[0]);
    }
    
    public int getDepth()
    {
        int depth = 0;
        for (Keyword k = this; k != null; k = k.getParent(), depth++)
            ;
        return depth;
    }
    
    public Keyword[] getDescendants()
    {
        List<Keyword> descendants = new ArrayList<Keyword>();
        getDescendants(descendants);
        return descendants.toArray(new Keyword[0]);
    }
    
    public UUID getId()
    {
        return id;
    }
    
    public int getIndexOfChild(Keyword child)
    {
        return children.indexOf(child);
    }
    
    public Location getLocation()
    {
        return location;
    }
    
    public String getName()
    {
        return name;
    }
    
    public Keyword getParent()
    {
        return parent;
    }
    
    public Keyword[] getPath()
    {
        int depth = getDepth();
        Keyword[] path = new Keyword[depth];
        for (Keyword k = this; k != null; k = k.getParent(), depth--)
        {
            path[depth - 1] = k;
        }
        return path;
    }
    
    public UUID[] getSynonymIds()
    {
        UUID[] ids = new UUID[_synonyms.size()];
        int i = 0;
        for (Keyword s : _synonyms)
        {
            ids[i++] = s.getId();
        }
        return ids;
    }
    
    public String getSynonymIds(char separatorChar, char quotationChar)
    {
        return StringUtils.join(getSynonymIds(), separatorChar, quotationChar);
    }
    
    public Keyword[] getSynonyms()
    {
        return _synonyms.toArray(new Keyword[0]);
    }
    
    public String getSynonyms(char separatorChar, char quotationChar)
    {
        return StringUtils.join(_synonyms, separatorChar, quotationChar);
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    
    public void moveTo(Keyword newParent)
    {
        if (newParent != this && newParent != null)
        {
            parent.removeChild(this);
            newParent.addChild(this);
        }
    }
    
    public void remove()
    {
        if (parent != null)
        {
            parent.removeChild(this);
        }
    }
    
    public void removeAllSynonyms()
    {
        for (Keyword s : _synonyms)
        {
            s._synonyms.remove(this);
        }
        _synonyms.clear();
    }
    
    public void removeChild(Keyword keyword)
    {
        KeywordEvent event = new KeywordEvent(this, keyword); // Before remove(...) so that the current child index can be computed.
        children.remove(keyword);
        keyword.parent = null;
        fireDeletedEvent(event);
        keyword.removeKeywordEventListener(_childListener);
    }
    
    public void removeKeywordEventListener(KeywordEventListener listener)
    {
        _keywordChangedEvent.removeListener(listener);
        _keywordDeletedEvent.removeListener(listener);
        _keywordInsertedEvent.removeListener(listener);
        _keywordStructureChangedEvent.removeListener(listener);
    }
    
    public void removeSynonym(Keyword keyword)
    {
        _synonyms.remove(keyword);
        keyword._synonyms.remove(this);
    }
    
    public void setLocation(Location location)
    {
        this.location = location;
        fireChangedEvent(new KeywordEvent(this));
    }
    
    public void setName(String name)
    {
        this.name = name;
        fireChangedEvent(new KeywordEvent(this));
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    private void fireChangedEvent(KeywordEvent event)
    {
        _keywordChangedEvent.fire(event);
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

    private void getDescendants(List<Keyword> res)
    {
        for (Keyword c : children)
        {
            res.add(c);
            c.getDescendants(res);
        }
    }
    
}
