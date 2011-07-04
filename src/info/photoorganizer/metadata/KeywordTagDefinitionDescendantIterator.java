package info.photoorganizer.metadata;

import java.util.Iterator;

public class KeywordTagDefinitionDescendantIterator implements
        Iterator<KeywordTagDefinition>
{

    private KeywordTagDefinition _root = null;
    private KeywordTagDefinition _current = null;
    private Iterator<KeywordTagDefinition> _childIterator = _current.getChildren().iterator();
    
    public KeywordTagDefinitionDescendantIterator(KeywordTagDefinition root)
    {
        super();
        _root = root;
        _current = _root;
        _childIterator = _current.getChildren().iterator();
    }

    @Override
    public boolean hasNext()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public KeywordTagDefinition next()
    {
        if (_childIterator.hasNext())
        {
            _current = _childIterator.next();
            _childIterator = _current.getChildren().iterator();
            return _current;
        }
        else
        {
            _current = _current.getParent();
            _childIterator = _current.getChildren().iterator();
        }
        return null;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
