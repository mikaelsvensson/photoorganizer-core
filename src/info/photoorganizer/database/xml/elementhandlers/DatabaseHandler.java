package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatabaseHandler extends DatabaseObjectHandler<Database>
{
    private static final String ATTRIBUTENAME_NAME = "name";

    private static final String ELEMENTNAME_TAGDEFINITIONS = "TagDefinitions";
    private static final String ELEMENTNAME_IMAGES = "Images";

    public DatabaseHandler(XMLDatabaseConverter converter)
    {
        super(Database.class, converter);
    }

    @Override
    public void readElement(Database o, Element el)
    {
        
        o.setName(XMLUtilities.getTextAttribute(el, ATTRIBUTENAME_NAME, "untitled"));
        
        readTagDefinitionElements(o, el);
        
        readImageElements(o, el);
        
//        Iterator<KeywordTagDefinition> keywords = _converter.fromElementChildren(el, KeywordTagDefinition.class).iterator();
//        if (keywords.hasNext())
//        {
//            o.setRootKeyword(keywords.next());
//        }
        
        super.readElement(o, el);
    }

    private void readTagDefinitionElements(Database o, Element el)
    {
        Iterator<TagDefinition> i = _converter.fromElementChildren(XMLUtilities.getNamedChild(el, ELEMENTNAME_TAGDEFINITIONS), TagDefinition.class).iterator();
        while (i.hasNext())
        {
            o.getTagDefinitions().add(i.next());
        }
    }

    private void readImageElements(Database o, Element el)
    {
        Iterator<Image> i = _converter.fromElementChildren(XMLUtilities.getNamedChild(el, ELEMENTNAME_IMAGES), Image.class).iterator();
        while (i.hasNext())
        {
            o.getImages().add(i.next());
        }
    }
    
    @Override
    public void writeElement(Database o, Element el)
    {
        XMLUtilities.setUUIDAttribute(el, ATTRIBUTENAME_NAME, o.getId());
        
        Document owner = el.getOwnerDocument();
        
        Element tagDefinitionsEl = createElement(ELEMENTNAME_TAGDEFINITIONS, owner);
        el.appendChild(tagDefinitionsEl);
        XMLUtilities.appendChildren(tagDefinitionsEl, _converter.toElements(owner, o.getTagDefinitions()));
        
        Element imagesEl = createElement(ELEMENTNAME_IMAGES, owner);
        el.appendChild(imagesEl);
        XMLUtilities.appendChildren(imagesEl, _converter.toElements(owner, o.getImages()));
        
        
//        KeywordTagDefinition rootKeyword = o.getRootKeyword();
//        if (null != rootKeyword)
//        {
//            el.appendChild(_converter.toElement(owner, rootKeyword));
//        }
        
        super.writeElement(o, el);
    }
    
}
