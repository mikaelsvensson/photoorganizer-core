package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.IntegerNumberTagDefinition;

import org.w3c.dom.Element;

public class IntegerNumberTagDefinitionHandler extends ValueTagDefinitionHandler<IntegerNumberTagDefinition>
{
    public IntegerNumberTagDefinitionHandler(XMLDatabaseConverter converter)
    {
        super(IntegerNumberTagDefinition.class, converter);
    }

    @Override
    public void readElement(IntegerNumberTagDefinition o, Element el)
    {
        super.readElement(o, el);
    }

    @Override
    public void writeElement(IntegerNumberTagDefinition o, Element el)
    {
        super.writeElement(o, el);
    }

}
