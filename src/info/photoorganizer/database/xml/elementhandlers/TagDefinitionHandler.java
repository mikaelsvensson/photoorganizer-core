package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Element;

public abstract class TagDefinitionHandler<T extends TagDefinition> extends DatabaseObjectHandler<T>
{
    public TagDefinitionHandler(Class<T> cls, XMLDatabaseStorageStrategy storageStrategy)
    {
        super(cls, storageStrategy);
    }

    private static String ATTRIBUTENAME_NAME = "name";
    private static String ATTRIBUTENAME_USEREDITABLE = "isuserallowedtoedittags";
    private static String ATTRIBUTENAME_USERCREATABLE= "isuserallowedtocreatetags";
    
    @Override
    public void readElement(T o, Element el)
    {
        o.setName(XMLUtilities.getTextAttribute(el, ATTRIBUTENAME_NAME, "untitled"));
        o.setUserAllowedToCreateTags(XMLUtilities.getBooleanAttribute(el, ATTRIBUTENAME_USERCREATABLE));
        o.setUserAllowedToEditTags(XMLUtilities.getBooleanAttribute(el, ATTRIBUTENAME_USEREDITABLE));
        
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(T o, Element el)
    {
        XMLUtilities.setTextAttribute(el, ATTRIBUTENAME_NAME, o.getName());
        XMLUtilities.setBooleanAttribute(el, ATTRIBUTENAME_USERCREATABLE, o.isUserAllowedToCreateTags());
        XMLUtilities.setBooleanAttribute(el, ATTRIBUTENAME_USEREDITABLE, o.isUserAllowedToEditTags());
        
        super.writeElement(o, el);
    }

    @Override
    public void storeElement(T o) throws DatabaseStorageException
    {
        storeElementInRoot(o, DatabaseHandler.ELEMENTNAME_TAGDEFINITIONS);
//        Element rootKeywordContainerEl = XMLUtilities.getNamedChild(_storageStrategy.getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_TAGDEFINITIONS);
//        
//        Element newElement = createElement();
//        Element currentEl = _storageStrategy.getDatabaseObjectElement(o);
//        if (currentEl != null)
//        {
//            rootKeywordContainerEl.replaceChild(newElement, currentEl);
//        }
//        else
//        {
//            rootKeywordContainerEl.appendChild(newElement);
//        }
//        writeElement(o, newElement);
    }
    
    /**
     * Steps to perform when removing tag definition:
     * if (tag definition is assigned to at least one image)
     *   if (user confirms removal)
     *     remove tags
     * remove tag definition
     * @throws DatabaseStorageException 
     */
    public void remove(T o) throws DatabaseStorageException
    {
        Element element = _storageStrategy.getDatabaseObjectElement(o);
        element.getParentNode().removeChild(element);
    }

}
