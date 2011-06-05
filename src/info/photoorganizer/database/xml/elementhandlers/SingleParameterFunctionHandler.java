package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.util.transform.SingleParameterFunction;

import org.w3c.dom.Element;

public abstract class SingleParameterFunctionHandler<T extends SingleParameterFunction> extends ElementHandler<T>
{
    public SingleParameterFunctionHandler(Class<T> cls, XMLDatabaseStorageStrategy storageStrategy)
    {
        super(cls, storageStrategy);
    }
    
    @Override
    public void readElement(T o, Element el)
    {
        o.setParam(el.getAttribute("param"));
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(T o, Element el)
    {
        el.setAttribute("param", o.getParam());
        super.writeElement(o, el);
    }

    @Override
    public void storeElement(T o) throws DatabaseStorageException
    {
        throw new DatabaseStorageException("One can not invoke storeElement directly for text transformers. Invoke storeElement for the keyword translator instead.");
    }

}
