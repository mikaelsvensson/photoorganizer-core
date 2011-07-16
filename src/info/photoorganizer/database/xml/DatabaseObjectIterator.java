package info.photoorganizer.database.xml;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DatabaseObjectIterator<T extends Object> implements Iterator<T>
{
    private Node _startNode = null;
    private Class<T> _cls = null;

    private StorageContext _context = null;
    
    public DatabaseObjectIterator(Element parent, StorageContext context, Class<T> cls)
    {
        super();
        _context = context;
        _cls = cls;
        if (null != parent)
        {
            _startNode = parent.getFirstChild();
        }
    }

    private Element findNext()
    {
        Node node = _startNode;
        while (node != null)
        {
            if (node.getNodeType() == Node.ELEMENT_NODE && _cls.getSimpleName().equals(node.getLocalName()))
            {
                return (Element) node;
            }
            node = node.getNextSibling();
        }
        return null;
    }

    @Override
    public boolean hasNext()
    {
        return findNext() != null;
    }

    @Override
    public T next()
    {
        Element next = findNext();
        if (null == next)
        {
            throw new NoSuchElementException();
        }
        _startNode = next.getNextSibling();
        return _context.fromElement(next, _cls);
    }
    
    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
