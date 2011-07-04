package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.util.Event;
import info.photoorganizer.util.Event.EventExecuter;
import info.photoorganizer.util.StringUtils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class KeywordTagDefinition extends TagDefinition implements Transferable
{

    /**
     * 
     */
    private static final long serialVersionUID = -8096281677404108954L;

    public static final char DEFAULT_KEYWORD_QUOTATION_MARK = '"';
    
    public static final char DEFAULT_KEYWORD_SEPARATOR = ' ';
    
    public KeywordTagDefinition(String name, UUID id, KeywordTagDefinition parent, DatabaseStorageStrategy storageContext)
    {
        super(name, id, storageContext);
        if (null != parent)
        {
            parent.addChild(this, true);
        }
        setUserAllowedToCreateTags(true);
        setUserAllowedToEditTags(true);
    }
    
    public KeywordTagDefinition(String name, KeywordTagDefinition parent, DatabaseStorageStrategy storageContext)
    {
        this(name, null, parent, storageContext);
    }

    public KeywordTagDefinition(String name, UUID id, DatabaseStorageStrategy storageContext)
    {
        this(name, id, null, storageContext);
    }

    public KeywordTagDefinition(String name, DatabaseStorageStrategy storageContext)
    {
        this(name, null, null, storageContext);
    }
    
    public KeywordTagDefinition(DatabaseStorageStrategy storageContext)
    {
        this(null, null, null, storageContext);
    }

    transient private final KeywordTagDefinitionEventListener _childListener = new KeywordTagDefinitionEventListener()
    {
        
        @Override
        public void tagChanged(TagDefinitionEvent event)
        {
            fireChangedEvent(event);
        }
        
        @Override
        public void tagDeleted(TagDefinitionEvent event)
        {
            fireDeletedEvent(event);
        }
        
        @Override
        public void keywordInserted(KeywordTagDefinitionEvent event)
        {
            fireInsertedEvent(event);
        }
        
        @Override
        public void keywordStructureChanged(KeywordTagDefinitionEvent event)
        {
            fireStructureChangedEvent(event);
        }
    };
    
    transient private Event<KeywordTagDefinitionEventListener, KeywordTagDefinitionEvent> _keywordInsertedEvent = new Event<KeywordTagDefinitionEventListener, KeywordTagDefinitionEvent>(
            new EventExecuter<KeywordTagDefinitionEventListener, KeywordTagDefinitionEvent>()
            {
                public void fire(KeywordTagDefinitionEventListener listener, KeywordTagDefinitionEvent event)
                {
                    listener.keywordInserted(event);
                }
            });
    
    transient private Event<KeywordTagDefinitionEventListener, KeywordTagDefinitionEvent> _keywordStructureChangedEvent = new Event<KeywordTagDefinitionEventListener, KeywordTagDefinitionEvent>(
            new EventExecuter<KeywordTagDefinitionEventListener, KeywordTagDefinitionEvent>()
            {
                public void fire(KeywordTagDefinitionEventListener listener, KeywordTagDefinitionEvent event)
                {
                    listener.keywordStructureChanged(event);
                }
            });

    private List<KeywordTagDefinition> children = new ArrayList<KeywordTagDefinition>(); 
    private KeywordTagDefinition parent = null;
    private Set<UUID> _synonyms = new HashSet<UUID>();
    private Location location = null;
    
    public boolean isDescendantOf(TagDefinition possibleAncestor)
    {
        for (KeywordTagDefinition current = this.parent; current != null; current = current.parent)
        {
            if (current.equals(possibleAncestor))
            {
                return true;
            }
        }
        return false;
    }
    public boolean isDescendantOf(UUID possibleAncestorId)
    {
        for (KeywordTagDefinition current = this.parent; current != null; current = current.parent)
        {
            if (current.getId().equals(possibleAncestorId))
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean isAncestorTo(TagDefinition possibleDescendant)
    {
        return possibleDescendant instanceof KeywordTagDefinition && ((KeywordTagDefinition)possibleDescendant).isDescendantOf(this);
    }
    
    public boolean isChildOf(KeywordTagDefinition possibleParent)
    {
        return parent.equals(possibleParent);
    }
    
    public boolean isParentOf(KeywordTagDefinition possibleChild)
    {
        return children.contains(possibleChild);
    }

    public KeywordTagDefinition addChild(KeywordTagDefinition tag, boolean notify)
    {
        if (tag.getParent() != null)
        {
            tag.moveTo(this);
        }
        else
        {
            children.add(tag);
            tag.parent = this;
            if (notify)
            { 
                fireInsertedEvent(new KeywordTagDefinitionEvent(this, tag));
            }
        }
        tag.addKeywordEventListener(_childListener);
        return tag;
    }
    
    public void addChildren(Iterable<KeywordTagDefinition> tags)
    {
        for (KeywordTagDefinition t : tags)
        {
            addChild(t, true);
        }
    }
    
    public void addChildren(KeywordTagDefinition... tags)
    {
        for (KeywordTagDefinition t : tags)
        {
            addChild(t, true);
        }
    }

    public void addKeywordEventListener(KeywordTagDefinitionEventListener listener)
    {
        addTagEventListener(listener);
        _keywordInsertedEvent.addListener(listener);
        _keywordStructureChangedEvent.addListener(listener);
    }
    
    
    private void fireInsertedEvent(KeywordTagDefinitionEvent event)
    {
        _keywordInsertedEvent.fire(event);
    }
    
    private void fireStructureChangedEvent(KeywordTagDefinitionEvent event)
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
            KeywordTagDefinitionEvent event = new KeywordTagDefinitionEvent(parent.getSharedAncestor(newParent));
            try
            {
                parent.removeChild(this, false);
                
                newParent.addChild(this, false);
                
                fireStructureChangedEvent(event);
            }
            catch (Throwable e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }
    
    public KeywordTagDefinition getSharedAncestor(KeywordTagDefinition newParent)
    {
        for (KeywordTagDefinition current = this; current != null; current = current.parent)
        {
            if (current.equals(newParent) || current.isAncestorTo(newParent))
            {
                return current;
            }
        }
        return null;
    }

    public void removeChild(KeywordTagDefinition keyword, boolean notify)
    {
        KeywordTagDefinitionEvent event = new KeywordTagDefinitionEvent(this, keyword); // Before remove(...) so that the current child index can be computed.
        children.remove(keyword);
        keyword.parent = null;
        if (notify)
        {
            fireDeletedEvent(event);
        }
        keyword.removeKeywordEventListener(_childListener);
    }
    
    public void removeKeywordEventListener(KeywordTagDefinitionEventListener listener)
    {
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
        fireChangedEvent();
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
        if (equals(this.location, location)) return;
        this.location = location;
        fireChangedEvent();
    }

    public void store() throws DatabaseStorageException
    {
        getStorageStrategy().storeTagDefinition(this);
    }
    
    public void remove() throws DatabaseStorageException
    {
        if (parent != null)
        {
            parent.removeChild(this, false);
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
    
    public KeywordTagDefinition addChild(String name)
    {
        return addChild(new KeywordTagDefinition(name, getStorageStrategy()), true);
    }
    
    public static final DataFlavor KEYWORD_FLAVOR = new DataFlavor(KeywordTagDefinition.class, "Dragable Keyword");
    
    private static DataFlavor[] FLAVORS = { KEYWORD_FLAVOR };

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (isDataFlavorSupported(flavor))
        {
            return this;
        }
        throw new UnsupportedFlavorException(flavor);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        for (DataFlavor f : getTransferDataFlavors())
        {
            if (f.equals(flavor))
            {
                return true;
            }
        }
        return false;
    }

}
