package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Element;

public abstract class TagDefinitionHandler<T extends TagDefinition> extends DatabaseObjectHandler<T>
{
    public TagDefinitionHandler(Class<T> cls, XMLDatabaseConverter converter)
    {
        super(cls, converter);
    }

    private static String ATTRIBUTENAME_NAME = "name";
    
    @Override
    public void readElement(T o, Element el)
    {
        o.setName(XMLUtilities.getTextAttribute(el, ATTRIBUTENAME_NAME, "untitled"));
        
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(T o, Element el)
    {
        XMLUtilities.setTextAttribute(el, ATTRIBUTENAME_NAME, o.getName());
        
        super.writeElement(o, el);
    }

}
