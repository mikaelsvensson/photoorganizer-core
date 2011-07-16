package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.metadata.RealNumberTagDefinition;

import org.w3c.dom.Element;

public class RealNumberTagDefinitionHandler extends ValueTagDefinitionHandler<RealNumberTagDefinition>
{
    public RealNumberTagDefinitionHandler(StorageContext context)
    {
        super(RealNumberTagDefinition.class, context);
    }

    @Override
    public RealNumberTagDefinition createObject(Element el)
    {
        return new RealNumberTagDefinition(_context.getStrategy());
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
