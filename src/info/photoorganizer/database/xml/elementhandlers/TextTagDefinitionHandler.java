package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.TextTagDefinition;

import org.w3c.dom.Element;

public class TextTagDefinitionHandler extends ValueTagDefinitionHandler<TextTagDefinition>
{
    public TextTagDefinitionHandler(XMLDatabaseConverter converter)
    {
        super(TextTagDefinition.class, converter);
    }

    @Override
    public void readElement(TextTagDefinition o, Element el)
    {
        super.readElement(o, el);
    }

    @Override
    public void writeElement(TextTagDefinition o, Element el)
    {
        super.writeElement(o, el);
    }

}
