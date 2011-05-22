package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.RealNumberTagDefinition;

import org.w3c.dom.Element;

public class RealNumberTagDefinitionHandler extends ValueTagDefinitionHandler<RealNumberTagDefinition>
{
    public RealNumberTagDefinitionHandler(XMLDatabaseConverter converter)
    {
        super(RealNumberTagDefinition.class, converter);
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
