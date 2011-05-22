package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.KeywordTag;

public class KeywordTagHandler extends TagHandler<KeywordTag>
{

    public KeywordTagHandler(XMLDatabaseConverter converter)
    {
        super(KeywordTag.class, converter);
    }

}
