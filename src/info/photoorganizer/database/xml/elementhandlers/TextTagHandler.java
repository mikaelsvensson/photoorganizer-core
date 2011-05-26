package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.metadata.TextTag;
import info.photoorganizer.metadata.TextTagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Element;

public class TextTagHandler extends TagHandler<TextTag>
{

    public TextTagHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(TextTag.class, storageStrategy);
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
        TagDefinition tagDefinition = _storageStrategy.getTagDefinition(el, TagHandler.ATTRIBUTENAME_DEFINITION);
        return new TextTag((TextTagDefinition) tagDefinition);
    }

}
