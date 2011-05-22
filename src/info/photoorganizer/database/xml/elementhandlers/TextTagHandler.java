package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.TextTag;

import org.w3c.dom.Element;

public class TextTagHandler extends TagHandler<TextTag>
{

    public TextTagHandler(XMLDatabaseConverter converter)
    {
        super(TextTag.class, converter);
    }

    @Override
    public void readElement(TextTag o, Element el)
    {
        o.setValue(el.getTextContent());

        super.readElement(o, el);
    }

    @Override
    public void writeElement(TextTag o, Element el)
    {
        el.setTextContent(o.getValue());

        super.writeElement(o, el);
    }

}
