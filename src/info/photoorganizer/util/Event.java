package info.photoorganizer.util;

import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

public class Event<L extends EventListener, E extends EventObject>
{
    public interface EventExecuter<L, E>
    {
        void fire(L listener, E event);
    }
    
    private EventExecuter<L, E> _executer = null;
    
    public Event(EventExecuter<L, E> executer)
    {
        super();
        this._executer = executer;
    }

    private List<L> _listeners = null;
    
    public synchronized List<L> getListeners()
    {
        if (null == _listeners)
        {
            _listeners = new LinkedList<L>();
        }
        return _listeners;
    }

    public synchronized void addListener(L listener)
    {
        List<L> listeners = getListeners();
        if (!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }
    
    public synchronized void removeListener(L listener)
    {
        List<L> listeners = getListeners();
        listeners.remove(listener);
    }
    
    public synchronized void removeAllListeners()
    {
        getListeners().clear();
    }
    
    public synchronized void fire(E event)
    {
        List<L> listeners = getListeners();
        for (L listener : listeners)
        {
            _executer.fire(listener, event);
        }
    }
}
