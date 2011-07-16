package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.KeywordTag;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KeywordTagHandler extends TagHandler<KeywordTag>
{

    public KeywordTagHandler(StorageContext context)
    {
        super(KeywordTag.class, context);
    }

    @Override
    public KeywordTag createObject(Element el)
    {
        TagDefinition tagDefinition = getTagDefinition(el, TagHandler.ATTRIBUTENAME_DEFINITION);
        return new KeywordTag((KeywordTagDefinition) tagDefinition);
    }

}
