package info.photoorganizer.database.xml.elementhandlers;

import java.util.Iterator;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatabaseHandler extends DatabaseObjectHandler<Database>
{
    private static final String ATTRIBUTENAME_NAME = "name";

    private static final String ELEMENTNAME_TAGDEFINITIONS = "TagDefinitions";

    public DatabaseHandler(XMLDatabaseConverter converter)
    {
        super(Database.class, converter);
    }

    @Override
    public void readElement(Database o, Element el)
    {
        
        o.setName(XMLUtilities.getTextAttribute(el, ATTRIBUTENAME_NAME, "untitled"));
        
        Iterator<TagDefinition> i = _converter.fromElementChildren(XMLUtilities.getNamedChild(el, ELEMENTNAME_TAGDEFINITIONS), TagDefinition.class).iterator();
        while (i.hasNext())
        {
            o.getTagDefinitions().add(i.next());
        }
        
//        Iterator<KeywordTagDefinition> keywords = _converter.fromElementChildren(el, KeywordTagDefinition.class).iterator();
//        if (keywords.hasNext())
//        {
//            o.setRootKeyword(keywords.next());
//        }
        
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(Database o, Element el)
    {
        XMLUtilities.setUUIDAttribute(el, ATTRIBUTENAME_NAME, o.getId());
        
        Document owner = el.getOwnerDocument();
        
        Element tagDefinitionsEl = createElement(ELEMENTNAME_TAGDEFINITIONS, owner);
        el.appendChild(tagDefinitionsEl);
        XMLUtilities.appendChildren(tagDefinitionsEl, _converter.toElements(owner, o.getTagDefinitions()));
        
        
//        KeywordTagDefinition rootKeyword = o.getRootKeyword();
//        if (null != rootKeyword)
//        {
//            el.appendChild(_converter.toElement(owner, rootKeyword));
//        }
        
        super.writeElement(o, el);
    }
    
}
