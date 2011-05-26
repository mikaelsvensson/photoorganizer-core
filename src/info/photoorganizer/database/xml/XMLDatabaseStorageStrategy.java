package info.photoorganizer.database.xml;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.database.xml.elementhandlers.DatabaseHandler;
import info.photoorganizer.database.xml.elementhandlers.ElementHandler;
import info.photoorganizer.database.xml.elementhandlers.ImageHandler;
import info.photoorganizer.database.xml.elementhandlers.IntegerNumberTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.IntegerNumberTagHandler;
import info.photoorganizer.database.xml.elementhandlers.KeywordTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.KeywordTagHandler;
import info.photoorganizer.database.xml.elementhandlers.RealNumberTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.RealNumberTagHandler;
import info.photoorganizer.database.xml.elementhandlers.TextTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.TextTagHandler;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLDatabaseStorageStrategy implements DatabaseStorageStrategy
{
    private static final String ATTRIBUTENAME_CITY = "city";
    private static final String ATTRIBUTENAME_COUNTRY = "country";
    

    private static final String ATTRIBUTENAME_ID = "id";
    private static final String ATTRIBUTENAME_LATITUDE = "latitude";
    private static final String ATTRIBUTENAME_LONGITUDE = "longitude";
    private static final String ATTRIBUTENAME_NAME = "name";
    private static final String ATTRIBUTENAME_SYNONYMS = "synonyms";
    private static DocumentBuilder docBuilder = null;
    private static DocumentBuilderFactory docBuilderFactory = null;

    private static final String ELEMENTNAME_DATABASE = "database";

    private static final String ELEMENTNAME_KEYWORD = "keyword";

    private static final String ELEMENTNAME_KEYWORDS = "keywords";

    private static final String ELEMENTNAME_LOCATION = "location";

    // private static final String ELEMENTNAME_LOCATIONKEYWORD =
    // "locationkeyword";
    // private static final String ELEMENTNAME_PERSONKEYWORD = "personkeyword";
    private static final String ID_PREFIX = "id-";

    public static final String NAMESPACE = "http://photoorganizer.info/database";
    
    private static final String SCHEMA_LANG = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String SCHEMA_NAMESPACE = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    static
    {
        try
        {
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            // schema =
            // schemaFactory.newSchema(DatabaseManager.class.getResource("/database.xsd"));
            // schemaValidator = schema.newValidator();

            // File schema = new
            // File("D:\\Utveckling\\EclipseWorkspace\\PhotoOrganizer\\resources\\database.xsd");
            // docBuilderFactory.setAttribute(SCHEMA_LANG, XML_SCHEMA);
            // docBuilderFactory.setAttribute(SCHEMA_SOURCE, schema);
            docBuilderFactory.setNamespaceAware(true);
            // docBuilderFactory.setSchema(schema);
            docBuilderFactory.setValidating(true);
            try
            {
                docBuilderFactory.setAttribute(SCHEMA_LANG, SCHEMA_NAMESPACE);
                docBuilderFactory.setAttribute(SCHEMA_SOURCE, XMLDatabaseStorageStrategy.class.getResourceAsStream("/database/xml/schema.xsd"));
            }
            catch (IllegalArgumentException ex)
            {
                // Happens if the parser does not support JAXP 1.2
                ex.printStackTrace();
            }

            docBuilder = docBuilderFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        // catch (SAXException e)
        // {
        // e.printStackTrace();
        // }
    }
    private Document doc = null;
    
    private File _databaseFile = null;
    
    public XMLDatabaseStorageStrategy(URL databaseUrl)
    {
        try
        {
            _databaseFile = new File(databaseUrl.toURI());
        }
        catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
//    private File getDatabaseFile()
//    {
//        String databasePath = ConfigurationProperty.dbPath.get();
//        File f = new File(databasePath);
//        return f;
//    }

    public Document getDocument()
    {
        if (null == doc)
        {
            File f = _databaseFile;
            if (!f.isFile() && f.getParentFile().canWrite())
            {
                try
                {
                    createEmptyDatabase(f);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (f.isFile())
            {
                try
                {
                    Document parsedDoc = docBuilder.parse(f);
                    //validateDocument(parsedDoc);
                    doc = parsedDoc;

                    //doc = XMLUtilities.documentFromFile(f);
                }
                catch (SAXException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return doc;
    }

    private void createEmptyDatabase(File f) throws FileNotFoundException, IOException
    {
        InputStream defaultDatabaseStream = getClass().getResourceAsStream("/database/xml/empty.xml");
        if (null != defaultDatabaseStream)
        {
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(f));
            byte[] buf = new byte[1024];
            int num = -1;
            while ((num = defaultDatabaseStream.read(buf)) != -1)
            {
                os.write(buf, 0, num);
            }
            os.close();
            defaultDatabaseStream.close();
        }
    }

//    @Override
//    public TagDefinition getRootTag()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }

    @Override
    public TagDefinition getTagDefinition(String name)
    {
        // TODO Auto-generated method stub
        for (ElementHandler<? extends Object> eh : HANDLERS)
        {
           String localName = eh.getDatabaseObjectClass().getSimpleName(); 
           NodeList elements = getDocument().getElementsByTagNameNS(NAMESPACE, localName);
           
           for (Element e : XMLUtilities.getNamedDecendants(getDocument().getDocumentElement(), NAMESPACE, localName))
           {
               TagDefinition tagDefinition = fromElement(e, TagDefinition.class);
               if (tagDefinition.getName().equals(name))
               {
                   return tagDefinition;
               }
           }
        }
        return null;
    }

    @Override
    public TagDefinition getTagDefinition(UUID id)
    {
        Element element = getDocument().getElementById(getXMLIdFromUUID(id));
        return fromElement(element, TagDefinition.class);
    }
    
    public TagDefinition getTagDefinition(Element el, String idAttrName)
    {
        Element element = getDocument().getElementById(el.getAttribute(idAttrName));
        return fromElement(element, TagDefinition.class);
    }
    
    private String getXMLIdFromUUID(UUID id)
    {
        return "id-" + id.toString();
    }
    
    private UUID getUUIDfromXMLId(String id)
    {
        return UUID.fromString(id.substring(3));
    }

    public static boolean handles(URL databaseUrl)
    {
        return true;
    }

//    @Override
//    public Database load()
//    {
//        Document document = getDocument();
//        if (null != document)
//        {
//            XMLDatabaseConverter converter = new XMLDatabaseConverter();
//            return converter.fromDocument(document);
//        }
//        return null;
//    }

//    @Override
//    public void store(Database db) throws IOException
//    {
//        Document document = getDocument();
//        if (null != document)
//        {
//            XMLDatabaseConverter converter = new XMLDatabaseConverter();
//            converter.updateDocument(document, db);
//            
//            storeDocument(document);
//        }
//    }

    private void storeDocument(Document document) throws IOException
    {
        //          addSynonymsToDocument(db, document);
        //          if (isValidDocument(document))
        //          {
                      File f = _databaseFile;
                      XMLUtilities.documentToFile(document, f, XMLUtilities.UTF_8);
        //          }
        //          else
        //          {
        //              throw new IOException(
        //                      "Database contains data that is not allowed according to the XML schema.");
        //          }
    }

    @Override
    public void storeTagDefinition(TagDefinition tag) throws DatabaseStorageException
    {
        // TODO Auto-generated method stub
    }

    @Override
    public Iterator<TagDefinition> getTagDefinitions()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addTagDefinition(TagDefinition tag)
            throws DatabaseStorageException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeTagDefinition(TagDefinition tag)
            throws DatabaseStorageException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addImage(Image img) throws DatabaseStorageException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeImage(Image img) throws DatabaseStorageException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void storeImage(Image img) throws DatabaseStorageException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close()
    {
        // TODO Auto-generated method stub
        
    }

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
    
    public <T extends Object> T fromElement(Element el, Class<T> cls)
    {
        T res = null;
        if (null != el)
        {
            String name = el.getLocalName();
            for (ElementHandler handler : HANDLERS)
            {
                if (handler.getDatabaseObjectClass().getSimpleName().equals(name))
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
    
//    private void postProcessHandlers(Database db)
//    {
//        for (ElementHandler handler : HANDLERS)
//        {
//            handler.postProcess(db);
//        }
//    }
//    
//    private void preProcessHandlers()
//    {
//        for (ElementHandler handler : HANDLERS)
//        {
//            handler.preProcess();
//        }
//    }

    public Element toElement(Document owner, Object o)
    {
        for (ElementHandler handler : HANDLERS)
        {
            Element el = handler.createElement(o, owner);
            if (el != null)
            {
                return el;
            }
        }
        return null;
    }
    
    public <T extends Object> Iterable<T> fromElementChildren(Element el, Class<T> cls)
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
    
    public Iterable<Element> toElements(Document owner, Iterable<? extends Object> objects)
    {
        LinkedList<Element> res = new LinkedList<Element>();
        for (Object o : objects)
        {
            res.add(toElement(owner, o));
        }
        return res;
    }

    @Override
    public Iterator<Image> getImages()
    {
        return new ImagesIterator(XMLUtilities.getNamedChild(getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_IMAGES), this);
    }

    public Iterable<Element> toElements(Document owner, Iterator<? extends Object> objects)
    {
        LinkedList<Element> res = new LinkedList<Element>();
        while (objects.hasNext())
        {
            res.add(toElement(owner, objects.next()));
        }
        return res;
    }
}
