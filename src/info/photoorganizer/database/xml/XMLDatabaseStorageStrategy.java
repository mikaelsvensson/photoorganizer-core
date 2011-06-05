package info.photoorganizer.database.xml;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
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
import info.photoorganizer.database.xml.elementhandlers.MultiParameterFunctionHandler;
import info.photoorganizer.database.xml.elementhandlers.RationalNumberTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.RationalNumberTagHandler;
import info.photoorganizer.database.xml.elementhandlers.RealNumberTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.RealNumberTagHandler;
import info.photoorganizer.database.xml.elementhandlers.SingleParameterFunctionHandler;
import info.photoorganizer.database.xml.elementhandlers.TagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.TextTagDefinitionHandler;
import info.photoorganizer.database.xml.elementhandlers.TextTagHandler;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.IndexingConfiguration;
import info.photoorganizer.metadata.KeywordTranslatorFileFilter;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.StringUtils;
import info.photoorganizer.util.XMLUtilities;
import info.photoorganizer.util.transform.ReplaceTransformer;
import info.photoorganizer.util.transform.TextCaseTransformer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XMLDatabaseStorageStrategy implements DatabaseStorageStrategy
{
    private static DocumentBuilder docBuilder = null;
    private static DocumentBuilderFactory docBuilderFactory = null;

    public static final String NAMESPACE = "http://photoorganizer.info/database";

    private static final String SCHEMA_LANG = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    
    private static final String SCHEMA_NAMESPACE = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
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
    
    public static boolean handles(URL databaseUrl)
    {
        return true;
    }
    
    private File _databaseFile = null;
    private Date _lastOperation = null;

    private Document doc = null;
    private final KeywordTagHandler KEYWORD_TAG_HANDLER = new KeywordTagHandler(this);
    private final KeywordTagDefinitionHandler KEYWORD_TAG_DEFINITION_HANDLER = new KeywordTagDefinitionHandler(this);
    private ElementHandler[] HANDLERS = null;
    
    private class KeywordTranslatorFileFilterHandler extends SingleParameterFunctionHandler<KeywordTranslatorFileFilter>
    {

        public KeywordTranslatorFileFilterHandler(XMLDatabaseStorageStrategy storageStrategy)
        {
            super(KeywordTranslatorFileFilter.class, storageStrategy);
        }

        @Override
        public KeywordTranslatorFileFilter createObject(Element el)
        {
            return new KeywordTranslatorFileFilter();
        }
        
    }
    
    private class ReplaceTransformerHandler extends MultiParameterFunctionHandler<ReplaceTransformer>
    {
        
        public ReplaceTransformerHandler(XMLDatabaseStorageStrategy storageStrategy)
        {
            super(ReplaceTransformer.class, storageStrategy);
        }

        @Override
        public ReplaceTransformer createObject(Element el)
        {
            return new ReplaceTransformer();
        }
        
    }
    
    private class TextCaseTransformerHandler extends SingleParameterFunctionHandler<TextCaseTransformer>
    {
        
        public TextCaseTransformerHandler(XMLDatabaseStorageStrategy storageStrategy)
        {
            super(TextCaseTransformer.class, storageStrategy);
        }
        
        @Override
        public TextCaseTransformer createObject(Element el)
        {
            return new TextCaseTransformer();
        }
        
    }
    
    public XMLDatabaseStorageStrategy(URL databaseUrl)
    {
        HANDLERS = new ElementHandler[] { 
                new ImageHandler(this),
                
                KEYWORD_TAG_HANDLER,
                new TextTagHandler(this),
                new IntegerNumberTagHandler(this),
                new RealNumberTagHandler(this),
                new RationalNumberTagHandler(this),
                new DatetimeTagHandler(this),
                
                KEYWORD_TAG_DEFINITION_HANDLER,
                new TextTagDefinitionHandler(this),
                new IntegerNumberTagDefinitionHandler(this),
                new RealNumberTagDefinitionHandler(this),
                new RationalNumberTagDefinitionHandler(this),
                new DatetimeTagDefinitionHandler(this),
                
                new IndexingConfigurationHandler(this),
                new MetadataMappingConfigurationHandler(this),
                new KeywordTranslatorFileFilterHandler(this),
                new ReplaceTransformerHandler(this),
                new TextCaseTransformerHandler(this),
                
                new DatabaseHandler(this) 
                };
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
    
    @Override
    public void addImage(Image img) throws DatabaseStorageException
    {
        // TODO Auto-generated method stub
        
    }
    
//    private File getDatabaseFile()
//    {
//        String databasePath = ConfigurationProperty.dbPath.get();
//        File f = new File(databasePath);
//        return f;
//    }

    @Override
    public void addTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException
    {
        storeTagDefinition(tagDefinition);
    }

    @Override
    public void close() throws DatabaseStorageException
    {
        if (true /*isDirty()*/)
        {
            try
            {
                storeDatabase();
            }
            catch (IOException e)
            {
                throw new DatabaseStorageException("Could not save database before closing it. Changes not saved. Database closed.", e);
            }
        }
    }

//    @Override
//    public TagDefinition getRootTag()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }

    public Element createElement(String name)
    {
        return getDocument().createElementNS(NAMESPACE, name);
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
    
    public <T extends Object> T fromElement(Element el, Class<T> cls)
    {
        T res = null;
        if (null != el)
        {
            String name = el.getLocalName();
            for (ElementHandler handler : HANDLERS)
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
    
    public Element getDatabaseObjectElement(DatabaseObject o)
    {
        return getDatabaseObjectElement(o.getId());
    }

    public Element getDatabaseObjectElement(UUID id)
    {
        getDocument().normalizeDocument();
        return getDocument().getElementById(getXMLIdFromUUID(id));
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

    @Override
    public Iterator<Image> getImages()
    {
        return new DatabaseObjectIterator(XMLUtilities.getNamedChild(getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_IMAGES), this, Image.class);
    }

    public TagDefinition getTagDefinition(Element el, String idAttrName)
    {
        Element element = getDocument().getElementById(el.getAttribute(idAttrName));
        return fromElement(element, TagDefinition.class);
    }

    @Override
    public TagDefinition getTagDefinition(String name)
    {
        Iterator<TagDefinition> definitions = getTagDefinitions();
        while (definitions.hasNext())
        {
            TagDefinition definition = definitions.next();
            if (definition.getName().equalsIgnoreCase(name))
            {
                return definition;
            }
        }
        
//        for (ElementHandler<? extends Object> eh : HANDLERS)
//        {
//           String localName = eh.getDatabaseObjectClass().getSimpleName(); 
//           NodeList elements = getDocument().getElementsByTagNameNS(NAMESPACE, localName);
//           
//           for (Element e : XMLUtilities.getNamedDecendants(getDocument().getDocumentElement(), NAMESPACE, localName))
//           {
//               TagDefinition tagDefinition = fromElement(e, TagDefinition.class);
//               if (tagDefinition.getName().equals(name))
//               {
//                   return tagDefinition;
//               }
//           }
//        }
        return null;
    }

    @Override
    public <T extends TagDefinition> T getTagDefinition(String name, Class<T> type)
    {
        TagDefinition def = getTagDefinition(name);
        if (type.isAssignableFrom(def.getClass()))
        {
            return (T) def;
        }
        return null;
    }

    @Override
    public TagDefinition getTagDefinition(UUID id)
    {
        Element element = getDatabaseObjectElement(id);
        return fromElement(element, TagDefinition.class);
    }

    @Override
    public <T extends TagDefinition> T getTagDefinition(UUID id, Class<T> type)
    {
        TagDefinition def = getTagDefinition(id);
        if (type.isAssignableFrom(def.getClass()))
        {
            return (T) def;
        }
        return null;
    }

    @Override
    public Iterator<TagDefinition> getTagDefinitions()
    {
        List<TagDefinition> res = new LinkedList<TagDefinition>();
        for (ElementHandler<? extends Object> eh : HANDLERS)
        {
            if (TagDefinition.class.isAssignableFrom(eh.getDatabaseObjectClass()))
            {
                String localName = eh.getDatabaseObjectClass().getSimpleName();

                for (Element e : XMLUtilities.getNamedDecendants(getDocument().getDocumentElement(), NAMESPACE, localName))
                {
                    TagDefinition tagDefinition = fromElement(e, TagDefinition.class);
                    if (null != tagDefinition)
                    {
                        res.add(tagDefinition);
                    }
                }
            }
        }
        return res.iterator();

//        return new DatabaseObjectIterator(XMLUtilities.getNamedChild(getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_TAGDEFINITIONS), this, TagDefinition.class);
    }

    private UUID getUUIDfromXMLId(String id)
    {
        if (id.length() > 3)
        {
            return UUID.fromString(id.substring(3));
        }
        else
        {
            return null;
        }
    }

    private String getXMLIdFromUUID(UUID id)
    {
        return "id-" + id.toString();
    }

    public boolean isDirty()
    {
        return _lastOperation != null;
    }
    
    public void makeDirty()
    {
        _lastOperation = Calendar.getInstance().getTime();
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

    @Override
    public void removeImage(Image img) throws DatabaseStorageException
    {
        // TODO Auto-generated method stub
        
    }
    
    private void storeDatabase() throws IOException
    {
        //          addSynonymsToDocument(db, document);
        //          if (isValidDocument(document))
        //          {
                      File f = _databaseFile;
                      XMLUtilities.documentToFile(getDocument(), f, XMLUtilities.UTF_8);
        //          }
        //          else
        //          {
        //              throw new IOException(
        //                      "Database contains data that is not allowed according to the XML schema.");
        //          }
    }

    @Override
    public void storeImage(Image img) throws DatabaseStorageException
    {
        ImageHandler handler = getElementHandler(img);
        handler.storeElement(img);
    }

    @Override
    public void storeTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException
    {
        TagDefinitionHandler<TagDefinition> handler = getElementHandler(tagDefinition);
        handler.storeElement(tagDefinition);
        /*
        Element element = getDocument().getElementById(getXMLIdFromUUID(tag.getId()));
        
        if (null == element)
        {
            // Tag not previously saved. Add to tree.
            Element imagesElement = XMLUtilities.getNamedChild(getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_TAGDEFINITIONS);
            if (null != imagesElement)
            {
                imagesElement.appendChild(element);
            }
        }
        else
        {
            // Replace current tag definition with new one.
            element.getParentNode().replaceChild(toElement(getDocument(), tag), element);
        }
        */
    }
    
    public void setUUIDAttribute(Element el, String attr, UUID value)
    {
        if (value != null)
        {
            el.setAttribute(attr, getXMLIdFromUUID(value));
        }
        else
        {
            el.removeAttribute(attr);
        }
    }
    
    public void setUUIDsAttribute(Element el, String attr, UUID[] values)
    {
        if (values != null && values.length > 0)
        {
            StringBuilder sb = new StringBuilder();
            for (UUID value : values)
            {
                if (sb.length() > 0)
                {
                    sb.append(' ');
                }
                sb.append(getXMLIdFromUUID(value));
            }
            el.setAttribute(attr, sb.toString());
        }
        else
        {
            el.removeAttribute(attr);
        }
    }
    
    public UUID getUUIDAttribute(Element el, String attr)
    {
        try
        {
            return getUUIDfromXMLId(el.getAttribute(attr));
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
    
    public UUID[] getUUIDsAttribute(Element el, String attr)
    {
        String value = el.getAttribute(attr);
        String[] ids = StringUtils.split(value, ' ');
        UUID[] res = new UUID[ids.length];
        int i=0;
        for (String id : ids)
        {
            try
            {
                res[i++] = getUUIDfromXMLId(id);
            }
            catch (IllegalArgumentException e)
            {
            }
        }
        return res;
    }
    
    private <X extends Object, Y extends ElementHandler<X>> Y getElementHandler(X o)
    {
        for (ElementHandler<X> eh : HANDLERS)
        {
            if (eh.getDatabaseObjectClass() == o.getClass())
            {
                return (Y) eh;
            }
        }
        return null;
    }

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

    public Iterable<Element> toElements(Document owner, Iterable<? extends Object> objects)
    {
        LinkedList<Element> res = new LinkedList<Element>();
        for (Object o : objects)
        {
            res.add(toElement(owner, o));
        }
        return res;
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

    @Override
    public Iterator<Image> getImagesWithTag(TagDefinition tagDefinition)
    {
        List<Image> res = new LinkedList<Image>();
        Iterator<Image> images = getImages();
        while (images.hasNext())
        {
            Image image = images.next();
            if (image.hasTag(tagDefinition))
            {
                res.add(image);
            }
        }
        return res.iterator();
    }

    @Override
    public void removeTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException
    {
        TagDefinitionHandler<TagDefinition> handler = getElementHandler(tagDefinition);
        handler.remove(tagDefinition);
    }

    @Override
    public Iterator<IndexingConfiguration> getIndexingConfigurations()
    {
        return new DatabaseObjectIterator(XMLUtilities.getNamedChild(getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_INDEXINGCONFIGURATIONS), this, IndexingConfiguration.class);
    }

    @Override
    public void addIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        storeIndexingConfiguration(translator);
    }

    @Override
    public void storeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        IndexingConfigurationHandler handler = getElementHandler(translator);
        handler.storeElement(translator);
    }

    @Override
    public void removeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        IndexingConfigurationHandler handler = getElementHandler(translator);
        handler.remove(translator);
    }
}
