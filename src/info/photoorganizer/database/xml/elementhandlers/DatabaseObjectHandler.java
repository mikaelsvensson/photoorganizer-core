package info.photoorganizer.database.xml.elementhandlers;

import java.util.Arrays;
import java.util.Collections;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.CoreTagDefinition;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class DatabaseObjectHandler<T extends DatabaseObject>
{
    private static final String ATTRIBUTENAME_ID = "id";
    
    private Class<T> _objectClass = null;
    protected XMLDatabaseConverter _converter = null;
    
    public DatabaseObjectHandler(Class<T> objectClass, XMLDatabaseConverter converter)
    {
        super();
        this._objectClass = objectClass;
        this._converter = converter;
    }
    
    public Class<T> getDatabaseObjectClass()
    {
        return _objectClass;
    }
    
    public void readElement(T o, Element el)
    {
        o.setId(XMLUtilities.getUUIDAttribute(el, ATTRIBUTENAME_ID));
    }

    public void writeElement(T o, Element el)
    {
        XMLUtilities.setUUIDAttribute(el, ATTRIBUTENAME_ID, o.getId());
    }
    
    public void postProcess(Database db)
    {
        
    }
    
    public void preProcess()
    {
        
    }
    
    public Element createElement(T o, Document owner)
    {
        if (o.getClass() == _objectClass)
        {
            Element el = createElement(_objectClass.getSimpleName(), owner);
            writeElement(o, el);
            return el;
        }
        else
        {
            return null;
        }
    }
    
    public static Element createElement(String name, Document owner)
    {
        return owner.createElementNS(XMLDatabaseStorageStrategy.NAMESPACE, name);
    }
    
    public static Element createElement(String name, Element elementInDocument)
    {
        return createElement(name, elementInDocument.getOwnerDocument());
    }
}
