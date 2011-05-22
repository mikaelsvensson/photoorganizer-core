package info.photoorganizer.database.xml;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.xml.elementhandlers.DatabaseHandler;
import info.photoorganizer.database.xml.elementhandlers.DatabaseObjectHandler;
import info.photoorganizer.database.xml.elementhandlers.IntegerNumberTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.KeywordTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.RealNumberTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.TextTagDefinitionHandler;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.util.XMLUtilities;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLDatabaseConverter
{
    private final DatabaseObjectHandler[] HANDLERS = new DatabaseObjectHandler[] { 
            new DatabaseHandler(this), 
            new KeywordTagDefinitionHandler(this),
            new TextTagDefinitionHandler(this),
            new IntegerNumberTagDefinitionHandler(this),
            new RealNumberTagDefinitionHandler(this)
            };
    
    public <T extends DatabaseObject> T fromElement(Element el, Class<T> cls)
    {
        DatabaseObject res = null;
        String name = el.getLocalName();
        for (DatabaseObjectHandler handler : HANDLERS)
        {
            if (handler.getDatabaseObjectClass().getSimpleName().equals(name))
            {
                try
                {
                    if (handler.getDatabaseObjectClass().isAssignableFrom(cls))
                    {
                        res = (DatabaseObject) handler.getDatabaseObjectClass().newInstance();
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
            }
        }
        return null;
    }
    
    private void postProcessHandlers(Database db)
    {
        for (DatabaseObjectHandler handler : HANDLERS)
        {
            handler.postProcess(db);
        }
    }
    
    private void preProcessHandlers()
    {
        for (DatabaseObjectHandler handler : HANDLERS)
        {
            handler.preProcess();
        }
    }

    public Element toElement(Document owner, DatabaseObject o)
    {
        for (DatabaseObjectHandler handler : HANDLERS)
        {
            Element el = handler.createElement(o, owner);
            if (el != null)
            {
                return el;
            }
        }
        return null;
    }
    
    public <T extends DatabaseObject> Iterable<T> fromElementChildren(Element el, Class<T> cls)
    {
        LinkedList<T> res = new LinkedList<T>();
     
        if (null != el)
        {
            for (Element child : XMLUtilities.getChildElements(el))
            {
                res.add(fromElement(child, cls));
            }
        }
        return res;
    }
    
    public Iterable<Element> toElements(Document owner, Iterable<? extends DatabaseObject> objects)
    {
        LinkedList<Element> res = new LinkedList<Element>();
        for (DatabaseObject o : objects)
        {
            res.add(toElement(owner, o));
        }
        return res;
    }

    public Database fromDocument(Document doc)
    {
        preProcessHandlers();
        Database db = fromElement(doc.getDocumentElement(), Database.class);
        postProcessHandlers(db);
        return db;
    }
    
    public void updateDocument(Document doc, Database db)
    {
        Element newDocRoot = toElement(doc, db);
        doc.replaceChild(newDocRoot, doc.getDocumentElement());
    }
}