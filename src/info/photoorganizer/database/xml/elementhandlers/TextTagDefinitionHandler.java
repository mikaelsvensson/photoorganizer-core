package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.TextTagDefinition;

import org.w3c.dom.Element;

public class TextTagDefinitionHandler extends ValueTagDefinitionHandler<TextTagDefinition>
{
    public TextTagDefinitionHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(TextTagDefinition.class, storageStrategy);
    }

    @Override
    public TextTagDefinition createObject(Element el)
    {
        return new TextTagDefinition(_storageStrategy);
    }

//    @Override
//    public void readElement(TextTagDefinition o, Element el)
//    {
//        super.readElement(o, el);
//    }
//
//    @Override
//    public void writeElement(TextTagDefinition o, Element el)
//    {
//        super.writeElement(o, el);
//    }

}
