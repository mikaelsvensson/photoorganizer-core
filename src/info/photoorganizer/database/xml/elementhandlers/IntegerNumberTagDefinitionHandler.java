package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.IntegerNumberTagDefinition;

import org.w3c.dom.Element;

public class IntegerNumberTagDefinitionHandler extends ValueTagDefinitionHandler<IntegerNumberTagDefinition>
{
    public IntegerNumberTagDefinitionHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(IntegerNumberTagDefinition.class, storageStrategy);
    }

    @Override
    public IntegerNumberTagDefinition createObject(Element el)
    {
        return new IntegerNumberTagDefinition(_storageStrategy);
    }

//    @Override
//    public void readElement(IntegerNumberTagDefinition o, Element el)
//    {
//        super.readElement(o, el);
//    }
//
//    @Override
//    public void writeElement(IntegerNumberTagDefinition o, Element el)
//    {
//        super.writeElement(o, el);
//    }

}
