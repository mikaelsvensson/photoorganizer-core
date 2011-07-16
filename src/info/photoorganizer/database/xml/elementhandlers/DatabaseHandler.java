package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatabaseHandler extends DatabaseObjectHandler<Database>
{
    private static final String ATTRIBUTENAME_NAME = "name";

    public static final String ELEMENTNAME_TAGDEFINITIONS = "TagDefinitions";
    public static final String ELEMENTNAME_PHOTOS = "Photos";
    public static final String ELEMENTNAME_INDEXINGCONFIGURATIONS = "IndexingConfigurations";

    public DatabaseHandler(StorageContext context)
    {
        super(Database.class, context);
    }

    @Override
    public void readElement(Database o, Element el)
    {
        
        o.setName(XMLUtilities.getTextAttribute(el, ATTRIBUTENAME_NAME, "untitled"));
        
//        readTagDefinitionElements(o, el);
        
//        readImageElements(o, el);
        
//        Iterator<KeywordTagDefinition> keywords = _converter.fromElementChildren(el, KeywordTagDefinition.class).iterator();
//        if (keywords.hasNext())
//        {
//            o.setRootKeyword(keywords.next());
//        }
        
        super.readElement(o, el);
    }

//    private void readTagDefinitionElements(Database o, Element el)
//    {
//        Iterator<TagDefinition> i = _converter.fromElementChildren(XMLUtilities.getNamedChild(el, ELEMENTNAME_TAGDEFINITIONS), TagDefinition.class).iterator();
//        while (i.hasNext())
//        {
//            o.getTagDefinitions().add(i.next());
//        }
//    }

//    private void readImageElements(Database o, Element el)
//    {
//        Iterator<Image> i = _converter.fromElementChildren(XMLUtilities.getNamedChild(el, ELEMENTNAME_IMAGES), Image.class).iterator();
//        while (i.hasNext())
//        {
//            o.getImages().add(i.next());
//        }
//    }
    
    @Override
    public void writeElement(Database o, Element el)
    {
        XMLDatabaseStorageStrategy.setUUIDAttribute(el, ATTRIBUTENAME_NAME, o.getId());
        
        Document owner = el.getOwnerDocument();
        
        Element tagDefinitionsEl = createElement(ELEMENTNAME_TAGDEFINITIONS, owner);
        el.appendChild(tagDefinitionsEl);
        XMLUtilities.appendChildren(tagDefinitionsEl, _context.toElements(o.getTagDefinitions()));
        
        Element keywordTranslatorsEl = createElement(ELEMENTNAME_INDEXINGCONFIGURATIONS, owner);
        el.appendChild(keywordTranslatorsEl);
        XMLUtilities.appendChildren(keywordTranslatorsEl, _context.toElements(o.getIndexingConfigurations()));
        
        Element imagesEl = createElement(ELEMENTNAME_PHOTOS, owner);
        el.appendChild(imagesEl);
        XMLUtilities.appendChildren(imagesEl, _context.toElements(o.getPhotos()));
        
        
//        KeywordTagDefinition rootKeyword = o.getRootKeyword();
//        if (null != rootKeyword)
//        {
//            el.appendChild(_converter.toElement(owner, rootKeyword));
//        }
        
        super.writeElement(o, el);
    }

    @Override
    public Database createObject(Element el)
    {
        return new Database(_context.getStrategy());
    }

    @Override
    public void storeElement(Database o) throws DatabaseStorageException
    {
        throw new DatabaseStorageException("Feature to store database not implemented. Store each database item, such as keyword definitions and images, separately.");
    }
    
}
