package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Image extends DatabaseObject
{
    
    private List<Tag<? extends TagDefinition>> _tags = new LinkedList<Tag<? extends TagDefinition>>();
    private URL _url = null;

    public Image(DatabaseStorageStrategy storageContext)
    {
        super(null, storageContext);
    }
    
    public Image(UUID id, DatabaseStorageStrategy storageContext)
    {
        super(id, storageContext);
    }

    public Iterator<Tag<? extends TagDefinition>> getTags()
    {
        return _tags.iterator();
    }
    
    public void addTag(Tag<? extends TagDefinition> tag)
    {
        _tags.add(tag);
    }
    
    public void removeTag(Tag<? extends TagDefinition> tag)
    {
        _tags.remove(tag);
    }

    public URL getUrl()
    {
        return _url;
    }

    public void setUrl(URL url)
    {
        _url = url;
    }

    public void store() throws DatabaseStorageException
    {
        getStorageStrategy().storeImage(this);
    }
    
    public void remove() throws DatabaseStorageException
    {
        getStorageStrategy().removeImage(this);
    }
    
    public boolean hasTag(TagDefinition tagDefinition)
    {
        for (Tag<? extends TagDefinition> tag : _tags)
        {
            if (tag.getDefinition().equals(tagDefinition))
            {
                return true;
            }
        }
        return false;
    }
}
