package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.DatabaseObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class ElementHandler<T extends Object>
{
    
    protected Map<UUID, DatabaseObject> _processedObjects = null;

    public static Element createElement(String name, Document owner)
    {
        return owner.createElementNS(XMLDatabaseStorageStrategy.NAMESPACE, name);
    }

    public static Element createElement(String name, Element elementInDocument)
    {
        return createElement(name, elementInDocument.getOwnerDocument());
    }

    protected XMLDatabaseStorageStrategy _storageStrategy = null;

    private Class<T> _objectClass = null;

    public ElementHandler(Class<T> objectClass, XMLDatabaseStorageStrategy storageStrategy)
    {
        super();
        _objectClass = objectClass;
        _storageStrategy = storageStrategy;
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

    public Class<T> getDatabaseObjectClass()
    {
        return _objectClass;
    }

    public void postProcess(Database db)
    {
        _processedObjects = null;
    }

    public void preProcess()
    {
        _processedObjects = new HashMap<UUID, DatabaseObject>();
    }
    
    public abstract T createObject(Element el);

    public void readElement(T o, Element el)
    {
    }

    public void writeElement(T o, Element el)
    {
    }

}
