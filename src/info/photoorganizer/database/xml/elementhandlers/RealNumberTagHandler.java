package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.RealNumberTag;
import info.photoorganizer.metadata.RealNumberTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Element;

public class RealNumberTagHandler extends TagHandler<RealNumberTag>
{

    private static final String ATTRIBUTENAME_VALUE = "value";

    public RealNumberTagHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(RealNumberTag.class, storageStrategy);
    }

    @Override
    public void readElement(RealNumberTag o, Element el)
    {
        try
        {
            Double value = XMLUtilities.getDoubleAttribute(el, ATTRIBUTENAME_VALUE, null);
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
    public void writeElement(RealNumberTag o, Element el)
    {
        Double value = o.getValue();
        if (null != value)
        {
            XMLUtilities.setDoubleAttribute(el, ATTRIBUTENAME_VALUE, value);
        }

        super.writeElement(o, el);
    }

    @Override
    public RealNumberTag createObject(Element el)
    {
        TagDefinition tagDefinition = _storageStrategy.getTagDefinition(el, TagHandler.ATTRIBUTENAME_DEFINITION);
        return new RealNumberTag((RealNumberTagDefinition) tagDefinition);
    }

}
