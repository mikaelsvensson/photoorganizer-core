package info.photoorganizer.database.xml;

import info.photoorganizer.metadata.Image;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DatabaseObjectIterator<T extends Object> implements Iterator<T>
{
    private Node _startNode = null;
    private Class<T> _cls = null;

    private XMLDatabaseStorageStrategy _strategy = null;
    
    public DatabaseObjectIterator(Element parent, XMLDatabaseStorageStrategy strategy, Class<T> cls)
    {
        super();
        _strategy = strategy;
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
        return _strategy.fromElement(next, _cls);
    }
    
    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
