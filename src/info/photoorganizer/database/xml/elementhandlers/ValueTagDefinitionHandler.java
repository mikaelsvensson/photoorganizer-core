package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.ValueTagDefinition;

import org.w3c.dom.Element;

public class ValueTagDefinitionHandler<T extends ValueTagDefinition> extends TagDefinitionHandler<T>
{
    public ValueTagDefinitionHandler(Class<T> cls, XMLDatabaseConverter converter)
    {
        super(cls, converter);
    }

    @Override
    public void readElement(T o, Element el)
    {
        super.readElement(o, el);
    }

    @Override
    public void writeElement(T o, Element el)
    {
        super.writeElement(o, el);
    }

}
