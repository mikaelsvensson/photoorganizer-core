package info.photoorganizer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class TimeoutListCache<T>
{
    private List<T> _values = new ArrayList<T>();
    private long _timeout = 0;
    private long _lastRefresh = 0;

    public TimeoutListCache(long timeoutSeconds)
    {
        _timeout = timeoutSeconds * 1000;
    }
    
    public synchronized void refresh()
    {
        clear();
        refreshData();
        updateLastRefresh();
    }
    
    protected abstract void refreshData();
    
    private void updateLastRefresh()
    {
        _lastRefresh = System.currentTimeMillis();
    }
    
    protected synchronized void clear()
    {
        _values.clear();
        updateLastRefresh();
    }
    
    public synchronized List<T> list()
    {
        refreshIfNecessary();
        return new ArrayList<T>(_values);
    }

    private void refreshIfNecessary()
    {
        long now = System.currentTimeMillis();
        if (now > _lastRefresh + _timeout)
        {
            refresh();
        }
    }

    public synchronized void invalidate()
    {
        _lastRefresh = 0;
    }
    
    public synchronized void invalidateIfContains(T value)
    {
        if (_values.contains(value))
        {
            invalidate();
        }
    }

    /**
     * @param arg0
     * @return
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Object arg0)
    {
        refreshIfNecessary();
        return _values.contains(arg0);
    }

    /**
     * @param arg0
     * @return
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<T> arg0)
    {
        refreshIfNecessary();
        return _values.containsAll(arg0);
    }

    /**
     * @param arg0
     * @return
     * @see java.util.List#get(int)
     */
    public T get(int arg0)
    {
        refreshIfNecessary();
        return _values.get(arg0);
    }

    /**
     * @return
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty()
    {
        refreshIfNecessary();
        return _values.isEmpty();
    }

    /**
     * @return
     * @see java.util.List#iterator()
     */
    public Iterator<T> iterator()
    {
        refreshIfNecessary();
        return _values.iterator();
    }

    /**
     * @return
     * @see java.util.List#size()
     */
    public int size()
    {
        refreshIfNecessary();
        return _values.size();
    }

    /**
     * @param index
     * @param element
     * @see java.util.List#add(int, java.lang.Object)
     */
    protected synchronized void add(int index, T element)
    {
        _values.add(index, element);
    }

    /**
     * @param e
     * @return
     * @see java.util.List#add(java.lang.Object)
     */
    protected synchronized boolean add(T e)
    {
        return _values.add(e);
    }

    /**
     * @param c
     * @return
     * @see java.util.List#addAll(java.util.Collection)
     */
    protected synchronized boolean addAll(Collection<? extends T> c)
    {
        return _values.addAll(c);
    }

    /**
     * @param index
     * @param c
     * @return
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    protected synchronized boolean addAll(int index, Collection<? extends T> c)
    {
        return _values.addAll(index, c);
    }
}
