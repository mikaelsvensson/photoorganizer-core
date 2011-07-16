package info.photoorganizer.database.xml;

import info.photoorganizer.database.xml.elementhandlers.SingleParameterFunctionHandler;
import info.photoorganizer.util.transform.TextCaseTransformer;

import org.w3c.dom.Element;

public class TextCaseTransformerHandler extends SingleParameterFunctionHandler<TextCaseTransformer>
{
    
    public TextCaseTransformerHandler(StorageContext context)
    {
        super(TextCaseTransformer.class, context);
    }
    
    @Override
    public TextCaseTransformer createObject(Element el)
    {
        return new TextCaseTransformer();
    }
    
}