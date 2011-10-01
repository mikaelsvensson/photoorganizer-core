package info.photoorganizer.database.xml;

import info.photoorganizer.database.xml.elementhandlers.SingleParameterFunctionHandler;
import info.photoorganizer.metadata.RegexpFileFilter;

import org.w3c.dom.Element;

public class RegexpFileFilterHandler extends SingleParameterFunctionHandler<RegexpFileFilter>
{

    public RegexpFileFilterHandler(StorageContext context)
    {
        super(RegexpFileFilter.class, context);
    }

    @Override
    public RegexpFileFilter createObject(Element el)
    {
        return new RegexpFileFilter();
    }
    
}