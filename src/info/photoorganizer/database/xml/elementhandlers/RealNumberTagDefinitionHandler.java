package info.photoorganizer.database.xml.elementhandlers;

import org.w3c.dom.Element;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.RealNumberTagDefinition;

public class RealNumberTagDefinitionHandler extends ValueTagDefinitionHandler<RealNumberTagDefinition>
{
    public RealNumberTagDefinitionHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(RealNumberTagDefinition.class, storageStrategy);
    }

    @Override
    public RealNumberTagDefinition createObject(Element el)
    {
        return new RealNumberTagDefinition(_storageStrategy);
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
