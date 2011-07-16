package info.photoorganizer.database.xml;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.database.xml.elementhandlers.DatabaseHandler;
import info.photoorganizer.database.xml.elementhandlers.ElementHandler;
import info.photoorganizer.metadata.IndexingConfiguration;
import info.photoorganizer.metadata.Photo;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.StringUtils;
import info.photoorganizer.util.XMLUtilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
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

    //private Document doc = null;
    
    private Collection<IndexingConfiguration> _indexingConfigurations = null;
    private Collection<Photo> _photos = null;
    private Collection<TagDefinition> _tagDefinitions = null;
    
//    private final KeywordTagHandler KEYWORD_TAG_HANDLER = new KeywordTagHandler(this);
//    private final KeywordTagDefinitionHandler KEYWORD_TAG_DEFINITION_HANDLER = new KeywordTagDefinitionHandler(this);
//    private ElementHandler[] HANDLERS = null;
    
    public XMLDatabaseStorageStrategy(URL databaseUrl)
    {
        try
        {
            _databaseFile = new File(databaseUrl.toURI());
            load(_databaseFile);
        }
        catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void addPhoto(Photo photo) throws DatabaseStorageException
    {
        storePhoto(photo);
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
        if (isDirty())
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

    public Element createElement(String name, Document doc)
    {
        return doc.createElementNS(NAMESPACE, name);
    }
    
    private Document createDocument()
    {
        try
        {
            return docBuilder.parse(getClass().getResourceAsStream("/database/xml/empty.xml"));
        }
        catch (SAXException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
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
    
    
    
//    public Element getDatabaseObjectElement(DatabaseObject o, Document doc)
//    {
//        return getDatabaseObjectElement(o.getId(), doc);
//    }
//
//    public Element getDatabaseObjectElement(UUID id, Document doc)
//    {
//        doc.normalizeDocument();
//        return doc.getElementById(getXMLIdFromUUID(id));
//    }

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

    private Document loadDocument(File f)
    {
        Document doc = null;
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
        return doc;
    }

    @Override
    public Collection<Photo> getPhotos()
    {
        synchronized (_photos)
        {
            return Collections.unmodifiableCollection(_photos);
        }
    }

    private void loadPhotos(StorageContext context)
    {
        synchronized (_photos)
        {
            _photos = new LinkedHashSet<Photo>();
            DatabaseObjectIterator<Photo> i = new DatabaseObjectIterator<Photo>(XMLUtilities.getNamedChild(context.getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_PHOTOS), context, Photo.class);
            while (i.hasNext())
            {
                _photos.add(i.next());
            }
        }
    }

    @Override
    public TagDefinition getTagDefinition(String name)
    {
        Collection<TagDefinition> definitions = getTagDefinitions();
        for (TagDefinition definition : definitions)
        {
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
        synchronized (_tagDefinitions)
        {
            for (TagDefinition def : _tagDefinitions)
            {
                if (def.getId().equals(id))
                {
                    return def;
                }
            }
            return null;
        }
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
    public Collection<TagDefinition> getTagDefinitions()
    {
        synchronized (_tagDefinitions)
        {
            return Collections.unmodifiableCollection(_tagDefinitions);
        }
    }

    private void loadTagDefinitions(StorageContext context)
    {
        synchronized (_tagDefinitions)
        {
            _tagDefinitions = new LinkedHashSet<TagDefinition>();
            for (ElementHandler<? extends Object> eh : context.getHandlers())
            {
                if (TagDefinition.class.isAssignableFrom(eh.getDatabaseObjectClass()))
                {
                    String localName = eh.getDatabaseObjectClass().getSimpleName();
                    
                    for (Element e : XMLUtilities.getNamedDecendants(context.getDocument().getDocumentElement(), NAMESPACE, localName))
                    {
                        TagDefinition tagDefinition = context.fromElement(e, TagDefinition.class);
                        if (null != tagDefinition)
                        {
                            _tagDefinitions.add(tagDefinition);
                        }
                    }
                }
            }
        }
    }

    public static UUID getUUIDfromXMLId(String id)
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

    public static String getXMLIdFromUUID(UUID id)
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
    public void removePhoto(Photo photo) throws DatabaseStorageException
    {
        synchronized (_photos)
        {
            _photos.remove(photo);
            makeDirty();
        }
    }
    
    private void storeDatabase() throws IOException, DatabaseStorageException
    {
        File f = _databaseFile;
        Document doc = createDocument();
        
        StorageContext context = new StorageContext(doc, this);
        synchronized (_photos)
        {
            synchronized (_tagDefinitions)
            {
                synchronized (_indexingConfigurations)
                {
                    for (Photo o : _photos)
                    {
                        context.storeDatabaseObject(o);
                    }
                    for (TagDefinition o : _tagDefinitions)
                    {
                        context.storeDatabaseObject(o);
                    }
                    for (IndexingConfiguration o : _indexingConfigurations)
                    {
                        context.storeDatabaseObject(o);
                    }
                }
            }
        }
        XMLUtilities.documentToFile(doc, f, XMLUtilities.UTF_8);
    }

    @Override
    public void storePhoto(Photo photo) throws DatabaseStorageException
    {
        synchronized (_photos)
        {
            _photos.add(photo);
            makeDirty();
        }
    }

    @Override
    public void storeTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException
    {
        synchronized (_tagDefinitions)
        {
            _tagDefinitions.add(tagDefinition);
            makeDirty();
        }
    }
    
    public static void setUUIDAttribute(Element el, String attr, UUID value)
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
    
    public static void setUUIDsAttribute(Element el, String attr, UUID[] values)
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
    
    public static UUID getUUIDAttribute(Element el, String attr)
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
    
    public static UUID[] getUUIDsAttribute(Element el, String attr)
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
    


    @Override
    public Collection<Photo> getPhotosWithTag(TagDefinition tagDefinition)
    {
        synchronized (_photos)
        {
            List<Photo> res = new ArrayList<Photo>();
            for (Photo photo : _photos)
            {
                if (photo.hasTag(tagDefinition))
                {
                    res.add(photo);
                }
            }
            return Collections.unmodifiableCollection(res);
        }
    }

    @Override
    public void removeTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException
    {
        synchronized (_tagDefinitions)
        {
            _tagDefinitions.remove(tagDefinition);
            makeDirty();
        }
    }

    @Override
    public Collection<IndexingConfiguration> getIndexingConfigurations()
    {
        synchronized (_indexingConfigurations)
        {
            return Collections.unmodifiableCollection(_indexingConfigurations);
        }
    }

    private void loadIndexingConfigurations(StorageContext context)
    {
        synchronized (_indexingConfigurations)
        {
            _indexingConfigurations = new LinkedHashSet<IndexingConfiguration>();
            DatabaseObjectIterator<IndexingConfiguration> i = new DatabaseObjectIterator<IndexingConfiguration>(XMLUtilities.getNamedChild(context.getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_INDEXINGCONFIGURATIONS), context, IndexingConfiguration.class);
            while (i.hasNext())
            {
                _indexingConfigurations.add(i.next());
            }
        }
    }

    @Override
    public void addIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        storeIndexingConfiguration(translator);
    }

    @Override
    public void storeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        synchronized (_indexingConfigurations)
        {
            _indexingConfigurations.add(translator);
            makeDirty();
        }
    }

    @Override
    public void removeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        synchronized (_indexingConfigurations)
        {
            _indexingConfigurations.remove(translator);
            makeDirty();
        }
    }
    
    private void load(File dbFile)
    {
        Document doc = loadDocument(dbFile);
        StorageContext context = new StorageContext(doc, this);
        loadIndexingConfigurations(context);
        loadTagDefinitions(context);
        loadPhotos(context);
    }
}
