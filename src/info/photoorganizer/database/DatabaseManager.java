package info.photoorganizer.database;

import info.photoorganizer.metadata.CoreTagDefinition;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.I18n;

import java.io.IOException;

public class DatabaseManager
{
    public static final String KEYWORD_OBJECTS = "Objects";
    public static final String KEYWORD_LOCATIONS = "Locations";
    public static final String KEYWORD_PEOPLE = "People";
    
    private static volatile DatabaseManager dbMngr = null;
//    private static DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
//    private static DocumentBuilder docBuilder = null;
//    private static SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//    private static Schema schema = null;
//    private static Validator schemaValidator = null;
    /*
    static 
    {
        docBuilderFactory.setNamespaceAware(true);
        try
        {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            schema = schemaFactory.newSchema(DatabaseManager.class.getResource("/database.xsd"));
            schemaValidator = schema.newValidator();
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
    */
    private DatabaseStorageContext context = new DatabaseStorageContext(DatabaseStoragePolicy.newDefaultStrategy());
    
    protected DatabaseManager()
    {
        
    }
    
//    private Document doc = null;
    
    private Database db = null;
    
    public Database getDatabase()
    {
        if (null == db)
        {
            db = context.load();
            initDatabase(db);
        }
        return db;
    }
    
    private void initDatabase(Database db)
    {
        System.out.println("Database keyword list is empty. Adding default keywords to database.");
        
        db.getTagDefinitions().add(new KeywordTagDefinition(KEYWORD_PEOPLE));
        db.getTagDefinitions().add(new KeywordTagDefinition(KEYWORD_LOCATIONS));
        db.getTagDefinitions().add(new KeywordTagDefinition(KEYWORD_OBJECTS));
    }

    public void saveDatabase() throws IOException
    {
        if (db != null)
        {
            context.store(db);
        }
    }
    /*
    public Document getDocument()
    {
        if (null == doc)
        {
            String databasePath = ConfigurationProperty.databasePath.get();
            File f = new File(databasePath);
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
                    if (schemaValidator != null)
                    {
                        schemaValidator.validate(new DOMSource(parsedDoc));
                        doc = parsedDoc;
                    }
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
    */
    public static DatabaseManager getInstance()
    {
        if (dbMngr == null)
        {
            synchronized (DatabaseManager.class)
            {
                if (dbMngr == null)
                {
                    dbMngr = new DatabaseManager();
                }
            }
        }
        return dbMngr;
    }

}
