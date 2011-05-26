package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.IntegerNumberTag;
import info.photoorganizer.metadata.IntegerNumberTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Element;

public class IntegerNumberTagHandler extends TagHandler<IntegerNumberTag>
{

    private static final String ATTRIBUTENAME_VALUE = "value";

    public IntegerNumberTagHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(IntegerNumberTag.class, storageStrategy);
    }

    @Override
    public void readElement(IntegerNumberTag o, Element el)
    {
        try
        {
            Integer value = XMLUtilities.getIntegerAttribute(el, ATTRIBUTENAME_VALUE, null);
            if (null != value)
            {
                o.setValue(value);
            }
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        super.readElement(o, el);
    }

    @Override
    public void writeElement(IntegerNumberTag o, Element el)
    {
        Integer value = o.getValue();
        if (null != value)
        {
            XMLUtilities.setIntegerAttribute(el, ATTRIBUTENAME_VALUE, value);
        }

        super.writeElement(o, el);
    }

    @Override
    public IntegerNumberTag createObject(Element el)
    {
        TagDefinition tagDefinition = _storageStrategy.getTagDefinition(el, TagHandler.ATTRIBUTENAME_DEFINITION);
        return new IntegerNumberTag((IntegerNumberTagDefinition) tagDefinition);
    }

}
