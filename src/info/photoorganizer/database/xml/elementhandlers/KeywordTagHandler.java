package info.photoorganizer.database.xml.elementhandlers;

import org.w3c.dom.Element;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.KeywordTag;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

public class KeywordTagHandler extends TagHandler<KeywordTag>
{

    public KeywordTagHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(KeywordTag.class, storageStrategy);
    }

    @Override
    public KeywordTag createObject(Element el)
    {
        TagDefinition tagDefinition = _storageStrategy.getTagDefinition(el, TagHandler.ATTRIBUTENAME_DEFINITION);
        return new KeywordTag((KeywordTagDefinition) tagDefinition);
    }

}
