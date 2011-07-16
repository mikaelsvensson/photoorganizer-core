package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.metadata.TextTag;
import info.photoorganizer.metadata.TextTagDefinition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextTagHandler extends TagHandler<TextTag>
{

    public TextTagHandler(StorageContext context)
    {
        super(TextTag.class, context);
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

    @Override
    public TextTag createObject(Element el)
    {
        TagDefinition tagDefinition = getTagDefinition(el, TagHandler.ATTRIBUTENAME_DEFINITION);
        return new TextTag((TextTagDefinition) tagDefinition);
    }

}
