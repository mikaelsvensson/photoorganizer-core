package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

//    protected XMLDatabaseStorageStrategy _storageStrategy = null;
//    protected Document _doc = null;
    protected StorageContext _context = null;

    private Class<T> _objectClass = null;

    public ElementHandler(Class<T> objectClass, StorageContext context/*Document doc*//*, XMLDatabaseStorageStrategy storageStrategy*/)
    {
        super();
        _objectClass = objectClass;
//        _storageStrategy = storageStrategy;
//        _doc = doc;
        _context = context;
    }

    public Iterable<Element> toElements(List<? extends Object> objects)
    {
        return _context.toElements(objects);
    }

    public <T> Iterable<T> fromElementChildren(Element el, Class<T> cls)
    {
        return _context.fromElementChildren(el, cls);
    }

    public Element toElement(Object o)
    {
        return _context.toElement(o);
    }

    public Iterable<Element> toElements(Iterable<? extends Object> objects)
    {
        return _context.toElements(objects);
    }

    public Iterable<Element> toElements(Iterator<? extends Object> objects)
    {
        return _context.toElements(objects);
    }

    public <T> T fromElement(Element el, Class<T> cls)
    {
        return _context.fromElement(el, cls);
    }

    protected TagDefinition getTagDefinition(Element el, String idAttrName)
    {
        return _context.getTagDefinition(el, idAttrName);
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
        return _context.getDocument().createElementNS(XMLDatabaseStorageStrategy.NAMESPACE, _objectClass.getSimpleName());
//        return _storageStrategy.createElement(_objectClass.getSimpleName());
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
            Element currentEl = getDatabaseObjectElement((DatabaseObject) o);
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
    
    protected Element getDatabaseObjectElement(DatabaseObject o)
    {
        return getDatabaseObjectElement(o.getId());
    }

    protected Element getDatabaseObjectElement(UUID id)
    {
        _context.getDocument().normalizeDocument();
        return _context.getDocument().getElementById(XMLDatabaseStorageStrategy.getXMLIdFromUUID(id));
    }

    private Element getOrCreateRootElement(String rootElementName)
    {
        Element rootKeywordContainerEl = XMLUtilities.getNamedChild(_context.getDocument().getDocumentElement(), rootElementName);
        if (null == rootKeywordContainerEl)
        {
            rootKeywordContainerEl = createElement(rootElementName, _context.getDocument());
            _context.getDocument().getDocumentElement().appendChild(rootKeywordContainerEl);
        }
        return rootKeywordContainerEl;
    }

}
