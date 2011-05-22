package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.IntegerNumberTag;
import info.photoorganizer.metadata.RealNumberTag;
import info.photoorganizer.metadata.TextTag;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Element;

public class RealNumberTagHandler extends TagHandler<RealNumberTag>
{

    private static final String ATTRIBUTENAME_VALUE = "value";

    public RealNumberTagHandler(XMLDatabaseConverter converter)
    {
        super(RealNumberTag.class, converter);
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

}
