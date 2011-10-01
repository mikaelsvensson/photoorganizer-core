package info.photoorganizer.database.xml;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.elementhandlers.DatabaseHandler;
import info.photoorganizer.database.xml.elementhandlers.DatetimeTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.DatetimeTagHandler;
import info.photoorganizer.database.xml.elementhandlers.ElementHandler;
import info.photoorganizer.database.xml.elementhandlers.ImageHandler;
import info.photoorganizer.database.xml.elementhandlers.IndexingConfigurationHandler;
import info.photoorganizer.database.xml.elementhandlers.IntegerNumberTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.IntegerNumberTagHandler;
import info.photoorganizer.database.xml.elementhandlers.KeywordTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.KeywordTagHandler;
import info.photoorganizer.database.xml.elementhandlers.MetadataMappingConfigurationHandler;
import info.photoorganizer.database.xml.elementhandlers.RationalNumberTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.RationalNumberTagHandler;
import info.photoorganizer.database.xml.elementhandlers.RealNumberTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.RealNumberTagHandler;
import info.photoorganizer.database.xml.elementhandlers.TextTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.TextTagHandler;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StorageContext
{
    public XMLDatabaseStorageStrategy getStrategy()
    {
        return _strategy;
    }

    private Document _doc = null;
    
    XMLDatabaseStorageStrategy _strategy = null;

    public StorageContext(Document doc, XMLDatabaseStorageStrategy strategy)
    {
        super();
        _doc = doc;
        _strategy = strategy;
        _handlers = createElementHandlers();
    }

    private ElementHandler[] _handlers = null;

    public Document getDocument()
    {
        return _doc;
    }

    public ElementHandler[] getHandlers()
    {
        return _handlers;
    }
    
    public void storeDatabaseObject(DatabaseObject obj) throws DatabaseStorageException
    {
        ElementHandler<DatabaseObject> handler = getElementHandler(obj);
        handler.storeElement(obj);
    }

    public Iterable<Element> toElements(List<? extends Object> objects)
    {
        return toElements(objects);
    }
    
    public <T extends Object> Iterable<T> fromElementChildren(Element el, Class<T> cls)
    {
        LinkedList<T> res = new LinkedList<T>();
     
        if (null != el)
        {
            for (Element child : XMLUtilities.getChildElements(el))
            {
                T o = fromElement(child, cls);
                if (null != o)
                {
                    res.add(o);
                }
            }
        }
        return res;
    }
    
    private <X extends Object, Y extends ElementHandler<X>> Y getElementHandler(X o)
    {
        for (ElementHandler<X> eh : getHandlers())
        {
            if (eh.getDatabaseObjectClass() == o.getClass())
            {
                return (Y) eh;
            }
        }
        return null;
    }

    public Element toElement(Object o)
    {
        for (ElementHandler handler : getHandlers())
        {
            Element el = handler.createElement(o, getDocument());
            if (el != null)
            {
                return el;
            }
        }
        return null;
    }

    public Iterable<Element> toElements(Iterable<? extends Object> objects)
    {
        LinkedList<Element> res = new LinkedList<Element>();
        for (Object o : objects)
        {
            res.add(toElement(o));
        }
        return res;
    }

    public Iterable<Element> toElements(Iterator<? extends Object> objects)
    {
        LinkedList<Element> res = new LinkedList<Element>();
        while (objects.hasNext())
        {
            res.add(toElement(objects.next()));
        }
        return res;
    }
    
    public ElementHandler[] createElementHandlers()
    {
        return new ElementHandler[] { 
                new ImageHandler(this),
                
                new KeywordTagHandler(this),
                new TextTagHandler(this),
                new IntegerNumberTagHandler(this),
                new RealNumberTagHandler(this),
                new RationalNumberTagHandler(this),
                new DatetimeTagHandler(this),
                
                new KeywordTagDefinitionHandler(this),
                new TextTagDefinitionHandler(this),
                new IntegerNumberTagDefinitionHandler(this),
                new RealNumberTagDefinitionHandler(this),
                new RationalNumberTagDefinitionHandler(this),
                new DatetimeTagDefinitionHandler(this),
                
                new IndexingConfigurationHandler(this),
                new MetadataMappingConfigurationHandler(this),
                new RegexpFileFilterHandler(this),
                new ReplaceTransformerHandler(this),
                new TextCaseTransformerHandler(this),
                
                new DatabaseHandler(this) 
                };
    }
    public TagDefinition getTagDefinition(Element el, String idAttrName)
    {
        String id = el.getAttribute(idAttrName);
        return _strategy.getTagDefinition(XMLDatabaseStorageStrategy.getUUIDfromXMLId(id));
    }
    
    public <T extends Object> T fromElement(Element el, Class<T> cls)
    {
        T res = null;
        if (null != el)
        {
            String name = el.getLocalName();
            for (ElementHandler handler : getHandlers())
            {
                if (handler.getDatabaseObjectClass().getSimpleName().equals(name) && cls.isAssignableFrom(handler.getDatabaseObjectClass()))
                {
                    res = (T) handler.createObject(el);
                    handler.readElement(res, el);
                    return res;
                    /*
                    try
                    {
                        if (cls.isAssignableFrom(handler.getDatabaseObjectClass()))
                        {
                            Constructor<? extends Object> constructor = handler.getDatabaseObjectClass().getDeclaredConstructor(DatabaseStorageStrategy.class);
                            res = (T) constructor.newInstance(this);
                            //res = (DatabaseObject) handler.getDatabaseObjectClass().newInstance();
                            handler.readElement(res, el);
                            return res;
                        }
                    }
                    catch (InstantiationException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                    catch (SecurityException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (NoSuchMethodException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (IllegalArgumentException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (InvocationTargetException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    */
                }
            }
        }
        return null;
    }
}
