package info.photoorganizer.database.xml.elementhandlers;

import java.util.UUID;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Element;

public abstract class DatabaseObjectHandler<T extends DatabaseObject> extends ElementHandler<T>
{
    private static final String ATTRIBUTENAME_ID = "id";
    
    public DatabaseObjectHandler(Class<T> objectClass, XMLDatabaseConverter converter)
    {
        super(objectClass, converter);
    }
    
    @Override
    public void readElement(T o, Element el)
    {
        UUID uuid = XMLUtilities.getUUIDAttribute(el, ATTRIBUTENAME_ID);
        o.setId(uuid);
        
        _processedObjects.put(uuid, o);
        
        super.readElement(o, el);
    }

    @Override
    public void writeElement(T o, Element el)
    {
        XMLUtilities.setUUIDAttribute(el, ATTRIBUTENAME_ID, o.getId());
        
        super.writeElement(o, el);
    }
    
}
