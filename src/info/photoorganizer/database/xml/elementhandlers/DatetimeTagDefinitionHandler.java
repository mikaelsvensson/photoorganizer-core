package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.metadata.DatetimeTagDefinition;

import org.w3c.dom.Element;

public class DatetimeTagDefinitionHandler extends ValueTagDefinitionHandler<DatetimeTagDefinition>
{
    public DatetimeTagDefinitionHandler(StorageContext context)
    {
        super(DatetimeTagDefinition.class, context);
    }

    @Override
    public DatetimeTagDefinition createObject(Element el)
    {
        return new DatetimeTagDefinition(_context.getStrategy());
    }

//    @Override
//    public void readElement(RealNumberTagDefinition o, Element el)
//    {
//        super.readElement(o, el);
//    }
//
//    @Override
//    public void writeElement(RealNumberTagDefinition o, Element el)
//    {
//        super.writeElement(o, el);
//    }

}
