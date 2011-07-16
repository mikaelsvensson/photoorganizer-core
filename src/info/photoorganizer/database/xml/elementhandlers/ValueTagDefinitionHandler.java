package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.metadata.ValueTagDefinition;

import org.w3c.dom.Document;

public abstract class ValueTagDefinitionHandler<T extends ValueTagDefinition> extends TagDefinitionHandler<T>
{
    public ValueTagDefinitionHandler(Class<T> cls, StorageContext context)
    {
        super(cls, context);
    }

//    @Override
//    public void readElement(T o, Element el)
//    {
//        super.readElement(o, el);
//    }
//
//    @Override
//    public void writeElement(T o, Element el)
//    {
//        super.writeElement(o, el);
//    }

}
