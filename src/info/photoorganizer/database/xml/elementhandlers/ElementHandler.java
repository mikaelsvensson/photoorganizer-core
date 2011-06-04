package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.util.XMLUtilities;

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
    
    protected Element createElement()
    {
        return _storageStrategy.createElement(_objectClass.getSimpleName());
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
    
    public abstract void storeElement(T o) throws DatabaseStorageException;
    
    protected void storeElementInRoot(T o, String rootElementName) throws DatabaseStorageException
    {
        if (o instanceof DatabaseObject)
        {
            Element rootKeywordContainerEl = getOrCreateRootElement(rootElementName);
            Element newElement = createElement();
            Element currentEl = _storageStrategy.getDatabaseObjectElement((DatabaseObject) o);
            if (currentEl != null)
            {
                rootKeywordContainerEl.replaceChild(newElement, currentEl);
            }
            else
            {
                rootKeywordContainerEl.appendChild(newElement);
            }
            writeElement(o, newElement);
        }
        else
        {
            throw new DatabaseStorageException("Implementation only supports DatabaseObject instances.");
        }
    }

    private Element getOrCreateRootElement(String rootElementName)
    {
        Element rootKeywordContainerEl = XMLUtilities.getNamedChild(_storageStrategy.getDocument().getDocumentElement(), rootElementName);
        if (null == rootKeywordContainerEl)
        {
            rootKeywordContainerEl = createElement(rootElementName, _storageStrategy.getDocument());
            _storageStrategy.getDocument().getDocumentElement().appendChild(rootKeywordContainerEl);
        }
        return rootKeywordContainerEl;
    }

}
