package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.util.XMLUtilities;
import info.photoorganizer.util.transform.MultiParameterFunction;

import org.w3c.dom.Element;

public abstract class MultiParameterFunctionHandler<T extends MultiParameterFunction> extends ElementHandler<T>
{
    private static final String ATTRIBUTENAME_VALUE = "value";
    private static final String ATTRIBUTENAME_NAME = "name";
    private static final String ELEMENTNAME_PARAMETER = "Parameter";

    public MultiParameterFunctionHandler(Class<T> cls, XMLDatabaseStorageStrategy storageStrategy)
    {
        super(cls, storageStrategy);
    }
    
    @Override
    public void readElement(T o, Element el)
    {
        for (Element paramEl : XMLUtilities.getNamedChildren(el, ELEMENTNAME_PARAMETER))
        {
            o.setParam(paramEl.getAttribute(ATTRIBUTENAME_NAME), paramEl.getAttribute(ATTRIBUTENAME_VALUE));
        }
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(T o, Element el)
    {
        for (String paramName : o.getParamNames())
        {
            Element paramEl = createElement(ELEMENTNAME_PARAMETER, el);
            paramEl.setAttribute(ATTRIBUTENAME_NAME, paramName);
            paramEl.setAttribute(ATTRIBUTENAME_VALUE, o.getParam(paramName));
            el.appendChild(paramEl);
        }
        super.writeElement(o, el);
    }

    @Override
    public void storeElement(T o) throws DatabaseStorageException
    {
        throw new DatabaseStorageException("One can not invoke storeElement directly for text transformers. Invoke storeElement for the keyword translator instead.");
    }

}
