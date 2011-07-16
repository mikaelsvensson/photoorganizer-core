package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.metadata.RationalNumberTagDefinition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RationalNumberTagDefinitionHandler extends ValueTagDefinitionHandler<RationalNumberTagDefinition>
{
    public RationalNumberTagDefinitionHandler(StorageContext context)
    {
        super(RationalNumberTagDefinition.class, context);
    }

    @Override
    public RationalNumberTagDefinition createObject(Element el)
    {
        return new RationalNumberTagDefinition(_context.getStrategy());
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
