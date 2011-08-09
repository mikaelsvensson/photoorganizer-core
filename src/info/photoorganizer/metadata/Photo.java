package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;

import java.io.File;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Photo extends DatabaseObject
{
    private List<Tag<? extends TagDefinition>> _tags = new LinkedList<Tag<? extends TagDefinition>>();
    private URI _uri = null;
    private File _file = null;

    public Photo(DatabaseStorageStrategy storageStrategy)
    {
        super(null, storageStrategy);
    }
    
    public Photo(UUID id, DatabaseStorageStrategy storageStrategy)
    {
        super(id, storageStrategy);
    }

    public Iterator<Tag<? extends TagDefinition>> getTags()
    {
        return _tags.iterator();
    }
    
    public void addTag(Tag<? extends TagDefinition> tag) throws DatabaseException
    {
        if (null != tag)
        {
            if (!hasTag(tag.getDefinition()))
            {
                _tags.add(tag);
                fireChangedEvent();
            }
            else
            {
                throw new DatabaseException("Each image may only have one tag of each type. This image already has a tag of type " + tag.getDefinition() + ".");
            }
        }
    }
    
    public void removeTag(Tag<? extends TagDefinition> tag)
    {
        if (_tags.remove(tag))
        {
            fireChangedEvent();
        }
    }
    
    public void removeKeywordTagsOfType(KeywordTagDefinition oldKeyword)
    {
        Iterator<Tag<? extends TagDefinition>> i = _tags.iterator();
        while (i.hasNext())
        {
            Tag<? extends TagDefinition> tag = i.next();
            TagDefinition def = tag.getDefinition();
            if (oldKeyword.equals(def) || oldKeyword.isAncestorTo(def))
            {
                i.remove();
            }
        }
    }

    public URI getURI()
    {
        return _uri;
    }
    
    public File getFile()
    {
        return _file;
    }

    public void setURI(URI uri)
    {
        if (equals(_uri, uri)) return;
        _uri = uri;
        try
        {
            _file = new File(_uri);
        }
        catch (IllegalArgumentException e)
        {
            _file = null;
        }
        fireChangedEvent();
    }
    
    public boolean isFile()
    {
        return null != _file;
    }

    public void store() throws DatabaseStorageException
    {
        getStorageStrategy().storePhoto(this);
    }
    
    public void remove() throws DatabaseStorageException
    {
        getStorageStrategy().removePhoto(this);
    }
    
    public boolean hasTag(TagDefinition tagDefinition)
    {
        return getTag(tagDefinition) != null;
    }
    
    public Tag<? extends TagDefinition> getTag(TagDefinition tagDefinition)
    {
        for (Tag<? extends TagDefinition> tag : _tags)
        {
            if (tag.getDefinition().equals(tagDefinition))
            {
                return tag;
            }
        }
        return null;
    }

    public void setFile(File file)
    {
        if (equals(_file, file)) return;
        _file = file;
        _uri = file.toURI();
        fireChangedEvent();
    }

}
