package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.ValueTag;

public abstract class ValueTagHandler<T extends ValueTag> extends TagHandler<T>
{

    private ValueTagHandler(Class<T> cls, XMLDatabaseConverter converter)
    {
        super(cls, converter);
    }

//    @Override
//    public void readElement(T o, Element el)
//    {
//        super.readElement(o, el);
//    }
//
//    @Override
//    public void writeElement(T o, Element el)
//    {
//        super.writeElement(o, el);
//    }

}
