package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.RationalNumberTagDefinition;

import org.w3c.dom.Element;

public class RationalNumberTagDefinitionHandler extends ValueTagDefinitionHandler<RationalNumberTagDefinition>
{
    public RationalNumberTagDefinitionHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(RationalNumberTagDefinition.class, storageStrategy);
    }

    @Override
    public RationalNumberTagDefinition createObject(Element el)
    {
        return new RationalNumberTagDefinition(_storageStrategy);
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
