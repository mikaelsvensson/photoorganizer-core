package info.photoorganizer.database.xml;

import info.photoorganizer.metadata.Image;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ImagesIterator implements Iterator<Image>
{
    private Node _startNode = null;

    private XMLDatabaseStorageStrategy _strategy = null;
    
    public ImagesIterator(Element parent, XMLDatabaseStorageStrategy strategy)
    {
        super();
        _strategy = strategy;
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
            if (node.getNodeType() == Node.ELEMENT_NODE && "Image".equals(node.getLocalName()))
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
    public Image next()
    {
        Element next = findNext();
        if (null == next)
        {
            throw new NoSuchElementException();
        }
        _startNode = next.getNextSibling();
        return _strategy.fromElement(next, Image.class);
    }
    
    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
