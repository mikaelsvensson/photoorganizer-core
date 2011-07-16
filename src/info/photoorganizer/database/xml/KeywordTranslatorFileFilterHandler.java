package info.photoorganizer.database.xml;

import info.photoorganizer.database.xml.elementhandlers.SingleParameterFunctionHandler;
import info.photoorganizer.metadata.KeywordTranslatorFileFilter;

import org.w3c.dom.Element;

public class KeywordTranslatorFileFilterHandler extends SingleParameterFunctionHandler<KeywordTranslatorFileFilter>
{

    public KeywordTranslatorFileFilterHandler(StorageContext context)
    {
        super(KeywordTranslatorFileFilter.class, context);
    }

    @Override
    public KeywordTranslatorFileFilter createObject(Element el)
    {
        return new KeywordTranslatorFileFilter();
    }
    
}