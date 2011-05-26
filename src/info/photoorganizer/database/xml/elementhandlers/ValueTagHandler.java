package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.ValueTag;

public abstract class ValueTagHandler<T extends ValueTag> extends TagHandler<T>
{

    private ValueTagHandler(Class<T> cls, XMLDatabaseStorageStrategy storageStrategy)
    {
        super(cls, storageStrategy);
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
