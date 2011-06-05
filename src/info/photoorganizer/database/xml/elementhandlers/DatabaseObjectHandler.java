package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.DatabaseObject;

import java.util.UUID;

import org.w3c.dom.Element;

public abstract class DatabaseObjectHandler<T extends DatabaseObject> extends ElementHandler<T>
{
    public static final String ATTRIBUTENAME_ID = "id";
    
    public DatabaseObjectHandler(Class<T> objectClass, XMLDatabaseStorageStrategy storageStrategy)
    {
        super(objectClass, storageStrategy);
    }
    
    @Override
    public void readElement(T o, Element el)
    {
        UUID uuid = _storageStrategy.getUUIDAttribute(el, ATTRIBUTENAME_ID);
        o.setId(uuid);
        
        //_processedObjects.put(uuid, o);
        
        super.readElement(o, el);
    }

    @Override
    public void writeElement(T o, Element el)
    {
        _storageStrategy.setUUIDAttribute(el, ATTRIBUTENAME_ID, o.getId());
        el.setIdAttribute(ATTRIBUTENAME_ID, true);
        
        super.writeElement(o, el);
    }
    
}
