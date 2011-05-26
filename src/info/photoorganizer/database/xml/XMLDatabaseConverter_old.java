package info.photoorganizer.database.xml;

import info.photoorganizer.database.Database;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLDatabaseConverter_old
{
    private XMLDatabaseStorageStrategy _strategy = null;
    
    public XMLDatabaseConverter_old(XMLDatabaseStorageStrategy strategy)
    {
        super();
        _strategy = strategy;
    }
    /*
    private final ElementHandler[] HANDLERS = new ElementHandler[] { 
            new ImageHandler(this),
            
            new KeywordTagHandler(this),
            new TextTagHandler(this),
            new IntegerNumberTagHandler(this),
            new RealNumberTagHandler(this),
            
            new KeywordTagDefinitionHandler(this),
            new TextTagDefinitionHandler(this),
            new IntegerNumberTagDefinitionHandler(this),
            new RealNumberTagDefinitionHandler(this),
            
            new DatabaseHandler(this) 
            };
    */
    public <T extends Object> T fromElement(Element el, Class<T> cls)
    {
        return _strategy.fromElement(el, cls);
        /*
        DatabaseObject res = null;
        String name = el.getLocalName();
        for (ElementHandler handler : HANDLERS)
        {
            if (handler.getDatabaseObjectClass().getSimpleName().equals(name))
            {
                try
                {
                    if (cls.isAssignableFrom(handler.getDatabaseObjectClass()))
                    {
                        Constructor<? extends Object> constructor = handler.getDatabaseObjectClass().getDeclaredConstructor(DatabaseStorageStrategy.class);
                        res = (DatabaseObject) constructor.newInstance(_strategy);
                        //res = (DatabaseObject) handler.getDatabaseObjectClass().newInstance();
                        handler.readElement(res, el);
                        return (T) res;
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
            }
        }
        return null;
        */
    }
    /*
    private void postProcessHandlers(Database db)
    {
        for (ElementHandler handler : HANDLERS)
        {
            handler.postProcess(db);
        }
    }
    
    private void preProcessHandlers()
    {
        for (ElementHandler handler : HANDLERS)
        {
            handler.preProcess();
        }
    }
    */
    public Element toElement(Document owner, Object o)
    {
        return _strategy.toElement(owner, o);
        /*
        for (ElementHandler handler : HANDLERS)
        {
            Element el = handler.createElement(o, owner);
            if (el != null)
            {
                return el;
            }
        }
        return null;
        */
    }
    
    public <T extends Object> Iterable<T> fromElementChildren(Element el, Class<T> cls)
    {
        return _strategy.fromElementChildren(el, cls);
        /*
        LinkedList<T> res = new LinkedList<T>();
     
        if (null != el)
        {
            for (Element child : XMLUtilities.getChildElements(el))
            {
                res.add(fromElement(child, cls));
            }
        }
        return res;
        */
    }
    
    public Iterable<Element> toElements(Document owner, Iterable<? extends Object> objects)
    {
        return _strategy.toElements(owner, objects);
        /*
        LinkedList<Element> res = new LinkedList<Element>();
        for (Object o : objects)
        {
            res.add(toElement(owner, o));
        }
        return res;
        */
    }
    
    public Iterable<Element> toElements(Document owner, Iterator<? extends Object> objects)
    {
        return _strategy.toElements(owner, objects);
        /*
        LinkedList<Element> res = new LinkedList<Element>();
        while (objects.hasNext())
        {
            res.add(toElement(owner, objects.next()));
        }
        return res;
        */
    }

    public Database fromDocument(Document doc)
    {
//        preProcessHandlers();
        Database db = fromElement(doc.getDocumentElement(), Database.class);
//        postProcessHandlers(db);
        return db;
    }
    
    public void updateDocument(Document doc, Database db)
    {
        Element newDocRoot = toElement(doc, db);
        doc.replaceChild(newDocRoot, doc.getDocumentElement());
    }
}
