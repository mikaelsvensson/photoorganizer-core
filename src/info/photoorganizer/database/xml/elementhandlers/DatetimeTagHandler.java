package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.DatetimeTag;
import info.photoorganizer.metadata.DatetimeTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatetimeTagHandler extends TagHandler<DatetimeTag>
{

    private static final String ATTRIBUTENAME_VALUE = "value";

    public DatetimeTagHandler(StorageContext context)
    {
        super(DatetimeTag.class, context);
    }

    @Override
    public void readElement(DatetimeTag o, Element el)
    {
        try
        {
            Date value = XMLUtilities.getDateAttribute(el, ATTRIBUTENAME_VALUE, null);
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
    public void writeElement(DatetimeTag o, Element el)
    {
        Date value = o.getValue();
        if (null != value)
        {
            XMLUtilities.setDateAttribute(el, ATTRIBUTENAME_VALUE, value);
        }

        super.writeElement(o, el);
    }

    @Override
    public DatetimeTag createObject(Element el)
    {
        TagDefinition tagDefinition = _context.getTagDefinition(el, TagHandler.ATTRIBUTENAME_DEFINITION);
        return new DatetimeTag((DatetimeTagDefinition) tagDefinition);
    }

}
