package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.metadata.TextTagDefinition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextTagDefinitionHandler extends ValueTagDefinitionHandler<TextTagDefinition>
{
    public TextTagDefinitionHandler(StorageContext context)
    {
        super(TextTagDefinition.class, context);
    }

    @Override
    public TextTagDefinition createObject(Element el)
    {
        return new TextTagDefinition(_context.getStrategy());
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
