package info.photoorganizer.database.xml.elementhandlers;

import org.w3c.dom.Element;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.DatetimeTagDefinition;
import info.photoorganizer.metadata.RealNumberTagDefinition;

public class DatetimeTagDefinitionHandler extends ValueTagDefinitionHandler<DatetimeTagDefinition>
{
    public DatetimeTagDefinitionHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(DatetimeTagDefinition.class, storageStrategy);
    }

    @Override
    public DatetimeTagDefinition createObject(Element el)
    {
        return new DatetimeTagDefinition(_storageStrategy);
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
