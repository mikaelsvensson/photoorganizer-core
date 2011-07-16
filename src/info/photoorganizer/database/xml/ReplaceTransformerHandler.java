package info.photoorganizer.database.xml;

import info.photoorganizer.database.xml.elementhandlers.MultiParameterFunctionHandler;
import info.photoorganizer.util.transform.ReplaceTransformer;

import org.w3c.dom.Element;

public class ReplaceTransformerHandler extends MultiParameterFunctionHandler<ReplaceTransformer>
{
    
    public ReplaceTransformerHandler(StorageContext context)
    {
        super(ReplaceTransformer.class, context);
    }

    @Override
    public ReplaceTransformer createObject(Element el)
    {
        return new ReplaceTransformer();
    }
    
}