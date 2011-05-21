package info.photoorganizer.database.xml;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;
import info.photoorganizer.util.config.ConfigurationProperty;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
            // docBuilderFactory.setValidating(true);

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
    private File getDatabaseFile()
    {
        String databasePath = ConfigurationProperty.dbPath.get();
        File f = new File(databasePath);
        return f;
    }

    public Document getDocument()
    {
        if (null == doc)
        {
            File f = getDatabaseFile();
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
                doc = XMLUtilities.documentFromFile(f);
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

    @Override
    public TagDefinition getRootTag()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TagDefinition getTag(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TagDefinition getTag(UUID id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean handles(URL databaseUrl)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Database load()
    {
        Document document = getDocument();
        if (null != document)
        {
            XMLDatabaseConverter converter = new XMLDatabaseConverter();
            return converter.fromDocument(document);
        }
        return null;
    }

    @Override
    public void store(Database db) throws IOException
    {
        Document document = getDocument();
        if (null != document)
        {
            XMLDatabaseConverter converter = new XMLDatabaseConverter();
            converter.updateDocument(document, db);
            
            storeDocument(document);
        }
    }

    private void storeDocument(Document document) throws IOException
    {
        //          addSynonymsToDocument(db, document);
        //          if (isValidDocument(document))
        //          {
                      File f = getDatabaseFile();
                      XMLUtilities.documentToFile(document, f, XMLUtilities.UTF_8);
        //          }
        //          else
        //          {
        //              throw new IOException(
        //                      "Database contains data that is not allowed according to the XML schema.");
        //          }
    }

    @Override
    public void storeTag(TagDefinition tag) throws DatabaseStorageException
    {
        // TODO Auto-generated method stub
    }
    
}
