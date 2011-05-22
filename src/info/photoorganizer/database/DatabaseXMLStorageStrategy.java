package info.photoorganizer.database;

import info.photoorganizer.database.xml.elementhandlers.DatabaseObjectHandler;
import info.photoorganizer.metadata.Location;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

@Deprecated
public class DatabaseXMLStorageStrategy /*implements DatabaseStorageStrategy*/
{
    private static final String ATTRIBUTENAME_CITY = "city";
    private static final String ATTRIBUTENAME_COUNTRY = "country";
    private static final String ATTRIBUTENAME_ID = "id";
    private static final String ATTRIBUTENAME_LATITUDE = "latitude";
    private static final String ATTRIBUTENAME_LONGITUDE = "longitude";
    private static final String ATTRIBUTENAME_NAME = "name";
    private static final String ATTRIBUTENAME_SYNONYMS = "synonyms";
    
    private static DocumentBuilder docBuilder = null;
    
    private static DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    
    private static final String ELEMENTNAME_DATABASE = "database";
    
    private static final String ELEMENTNAME_KEYWORD = "keyword";
    
    private static final String ELEMENTNAME_LOCATION = "location";
    
    private static final String ELEMENTNAME_KEYWORDS = "keywords";

//    private static final String ELEMENTNAME_LOCATIONKEYWORD = "locationkeyword";
//    private static final String ELEMENTNAME_PERSONKEYWORD = "personkeyword";
    private static final String ID_PREFIX = "id-";
    private static final String NAMESPACE = "http://photoorganizer.info/database";
    private static Schema schema = null;
    private static SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private static Validator schemaValidator = null;
    
    final static String SCHEMA_LANG = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    final static String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    final static String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    
    static
    {
        try
        {
            schema = schemaFactory.newSchema(DatabaseManager.class.getResource("/database.xsd"));
            schemaValidator = schema.newValidator();
            
            
//            File schema = new File("D:\\Utveckling\\EclipseWorkspace\\PhotoOrganizer\\resources\\database.xsd");
//            docBuilderFactory.setAttribute(SCHEMA_LANG, XML_SCHEMA);
//            docBuilderFactory.setAttribute(SCHEMA_SOURCE, schema);
            docBuilderFactory.setNamespaceAware(true);
//            docBuilderFactory.setSchema(schema);
//            docBuilderFactory.setValidating(true);
            
            docBuilder = docBuilderFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
    }
    
//    public static Database databaseFromElement(Element element, DatabaseStorageStrategy storageStrategy)
//    {
//        Database db = new Database(storageStrategy);
//        HashMap<UUID, List<UUID>> collectedSynonyms = new HashMap<UUID, List<UUID>>();
//        
//        db.setName(element.getAttribute(ATTRIBUTENAME_NAME));
//        
//        Element keywordsEl = XMLUtilities.getNamedChild(element, ELEMENTNAME_KEYWORDS);
//        if (null != keywordsEl)
//        {
//            for (Element keywordEl : XMLUtilities.getNamedChildren(NAMESPACE, keywordsEl, ELEMENTNAME_KEYWORD))
//            {
//                db.getRootTag().addChild(keywordFromElement(keywordEl, collectedSynonyms));
//            }
//        }
//        
//        for (Entry<UUID, List<UUID>> entry : collectedSynonyms.entrySet())
//        {
//            Tag keyword = db.getRootTag().getChildById(entry.getKey(), true);
//            for (UUID synonymId : entry.getValue())
//            {
//                keyword.addSynonym(db.getRootTag().getChildById(synonymId, true));
//            }
//        }
//        
//        return db;
//    }
    
    public static Element databaseToElement(Database item, Document owner)
    {
        Element el = DatabaseObjectHandler.createElement(ELEMENTNAME_DATABASE, owner);
        
        el.setAttribute(ATTRIBUTENAME_NAME, item.getName());
        
        Element keywordsEl = owner.createElementNS(NAMESPACE, ELEMENTNAME_KEYWORDS);
        el.appendChild(keywordsEl);
        
//        for (TagDefinition k : item.getRootKeyword().getChildren())
//        {
//            keywordsEl.appendChild(keywordToElement(k, owner));
//        }
        return el;
    }
    /*
    public static TagDefinition keywordFromElement(Element element, Map<UUID, List<UUID>> collectedSynonyms)
    {
        String name = element.getAttribute(ATTRIBUTENAME_NAME);
        UUID id = getIdFromString(element.getAttribute(ATTRIBUTENAME_ID));
        
//        TagDefinition k = new TagDefinition(name, id);
        
//        Element locEl = XMLUtilities.getNamedChild(element, ELEMENTNAME_LOCATION);
//        if (locEl != null)
//        {
//            k.setLocation(locationFromElement(locEl));
//        }

        String synonyms = element.getAttribute(ATTRIBUTENAME_SYNONYMS);
        StringTokenizer tokens = new StringTokenizer(synonyms);
        List<UUID> synonymIds = new ArrayList<UUID>();
        while (tokens.hasMoreTokens())
        {
            String token = tokens.nextToken();
            UUID synonymId = getIdFromString(token);
            synonymIds.add(synonymId);
        }
        collectedSynonyms.put(id, synonymIds);
        
        for (Element el : XMLUtilities.getNamedChildren(NAMESPACE, element, ELEMENTNAME_KEYWORD))
        {
//            k.addChild(keywordFromElement(el, collectedSynonyms));
        }
//        return k;
    }
    */
    public static Element keywordToElement(TagDefinition item, Document owner)
    {
        Element el = null;
        
//        Location loc = item.getLocation();
//        if (loc != null)
//        {
//            el.appendChild(locationToElement(loc, owner));
//        }
        
        el.setAttribute(ATTRIBUTENAME_NAME, item.getName());
        
        String id = getStringFromId(item.getId());
        if (id != null && id.length() > 0)
        {
            el.setAttribute(ATTRIBUTENAME_ID, id);
        }
        
//        StringBuilder synonyms = new StringBuilder();
//        for (Tag synonym : item.getSynonyms())
//        {
//            if (synonyms.length() > 0)
//            {
//                synonyms.append(' ');
//            }
//            synonyms.append(getStringFromId(synonym.getId()));
//        }
//        
//        if (synonyms.length() > 0)
//        {
//            el.setAttribute(ATTRIBUTENAME_SYNONYMS, synonyms.toString());
//        }
        
//        for (Keyword k : item.getChildren())
//        {
//            el.appendChild(keywordToElement(k, owner));
//        }
        return el;
    }
    
    public static Location locationFromElement(Element element)
    {
        String city = element.getAttribute(ATTRIBUTENAME_CITY);
        
        String country = element.getAttribute(ATTRIBUTENAME_COUNTRY);

        Double latitude = null;
        String latitudeAttr = element.getAttribute(ATTRIBUTENAME_LATITUDE);
        if (null != latitudeAttr && latitudeAttr.length() > 0)
        {
            try
            {
                latitude = Double.parseDouble(latitudeAttr);
            }
            catch (NumberFormatException e)
            {
            }
        }
        
        Double longitude = null;
        String longitudeAttr = element.getAttribute(ATTRIBUTENAME_LONGITUDE);
        if (null != longitudeAttr && longitudeAttr.length() > 0)
        {
            try
            {
                longitude = Double.parseDouble(longitudeAttr);
            }
            catch (NumberFormatException e)
            {
            }
        }
        return new Location(city, country, latitude, longitude);
    }
    
    public static Element locationToElement(Location loc, Document owner)
    {
        Element el = DatabaseObjectHandler.createElement(ELEMENTNAME_LOCATION, owner);
        
        el.setAttribute(ATTRIBUTENAME_CITY, loc.getCity());
        el.setAttribute(ATTRIBUTENAME_COUNTRY, loc.getCountry());
        Double latitude = loc.getLatitude();
        if (null != latitude)
        {
            el.setAttribute(ATTRIBUTENAME_LATITUDE, Double.toString(latitude));
        }
        Double longitude = loc.getLongitude();
        if (null != longitude)
        {
            el.setAttribute(ATTRIBUTENAME_LONGITUDE, Double.toString(longitude));
        }

        return el;
    }
    
    private static UUID getIdFromString(String str)
    {
        str = str.substring(ID_PREFIX.length());
        return UUID.fromString(str);
    }
    
    private static String getStringFromId(UUID id)
    {
        return ID_PREFIX + id.toString();
    }

    private Document doc = null;

    public Document getDocument()
    {
        if (null == doc)
        {
            File f = getDatabaseFile();
            if (!f.isFile() && f.getParentFile().canWrite())
            {
                try
                {
                    InputStream defaultDatabaseStream = getClass().getResourceAsStream("/database-empty.xml");
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
                    validateDocument(parsedDoc);
                    doc = parsedDoc;
                }
                catch (SAXException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return doc;
    }

//    public Database load()
//    {
//        Document document = getDocument();
//        if (null != document)
//        {
//            Database database = databaseFromElement(document.getDocumentElement(), this);
////            addSynonymsToDatabase(document, database);
//            return database;
//        }
//        return null;
//    }
    
//    public void store(Database db) throws IOException
//    {
//        Document document = getDocument();
//        if (null != document)
//        {
//            Element newDocRoot = databaseToElement(db, document);
//            document.replaceChild(newDocRoot, document.getDocumentElement());
//            addSynonymsToDocument(db, document);
//            if (isValidDocument(document))
//            {
//                File f = getDatabaseFile();
//                XMLUtilities.documentToFile(document, f, XMLUtilities.UTF_8);
//            }
//            else
//            {
//                throw new IOException("Database contains data that is not allowed according to the XML schema.");
//            }
//        }
//    }
    
    private void addSynonymsToDocument(Database database, Document document)
    {
//        for (TagDefinition k : database.getRootKeyword().getDescendants())
//        {
//            Element element = document.getElementById(ID_PREFIX + k.getId());
//            
//            String rawIds = StringUtils.join(k.getSynonymIds(), Tag.DEFAULT_KEYWORD_SEPARATOR, true, Tag.DEFAULT_KEYWORD_QUOTATION_MARK);
//            if (rawIds != null && rawIds.length() > 0)
//            {
//                String synonyms = ID_PREFIX + rawIds.replace(" ", " " + ID_PREFIX);
//                element.setAttribute(ATTRIBUTENAME_SYNONYMS, synonyms);
//            }
//        }
    }
    
//    private void addSynonymsToDatabase(Document document, Database database)
//    {
//        for (Element element : XMLUtilities.getNamedDecendants(document.getDocumentElement(), NAMESPACE, ELEMENTNAME_KEYWORD, ELEMENTNAME_LOCATIONKEYWORD, ELEMENTNAME_PERSONKEYWORD))
//        {
//            String id = element.getAttribute(ATTRIBUTENAME_ID);
//            if (id.startsWith(ID_PREFIX))
//            {
//                id = id.substring(ID_PREFIX.length());
//            
//                Keyword keyword = database.getRootKeyword().getChildById(id, true);
//                
//                String synonyms = element.getAttribute(ATTRIBUTENAME_SYNONYMS);
//                if (null != synonyms && synonyms.length() > 0)
//                {
//                    List<WordInfo> words = StringUtils.split(synonyms, Keyword.DEFAULT_KEYWORD_SEPARATOR, Keyword.DEFAULT_KEYWORD_QUOTATION_MARK, false);
//                    for (WordInfo word : words)
//                    {
//                        String synonymId = word.getWord().substring(ID_PREFIX.length());
//                        Keyword synonymKeyword = database.getRootKeyword().getChildById(synonymId, true);
//                        if (null != synonymKeyword)
//                        {
//                            keyword.addSynonym(synonymKeyword);
//                        }
//                    }
//                }
//            }
//        }
//    }

    private File getDatabaseFile()
    {
        String databasePath = ConfigurationProperty.databasePath.get();
        File f = new File(databasePath);
        return f;
    }
    
    private boolean isValidDocument(Document parsedDoc)
    {
        if (schemaValidator != null)
        {
            try
            {
                validateDocument(parsedDoc);
                return true;
            }
            catch (SAXException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    private void validateDocument(Document parsedDoc) throws SAXException, IOException
    {
        schemaValidator.validate(new DOMSource(parsedDoc));
    }
    
}
