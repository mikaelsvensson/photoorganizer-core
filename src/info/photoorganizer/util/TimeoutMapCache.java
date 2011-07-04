package info.photoorganizer.util;

import java.util.HashMap;
import java.util.Map;

public abstract class TimeoutMapCache<TYPE, KEY> implements MapCache<TYPE, KEY>
{
    private Map<KEY, TYPE> _values = new HashMap<KEY, TYPE>();
    private long _timeout = 0;
    private long _lastRefresh = 0;

    public TimeoutMapCache(long timeoutSeconds)
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
    
    protected synchronized void add(KEY key, TYPE value)
    {
        _values.put(key, value);
    }
    
    private void updateLastRefresh()
    {
        _lastRefresh = System.currentTimeMillis();
    }
    
    /* (non-Javadoc)
     * @see info.mikaelsvensson.docase.core.Cache#clear()
     */
    @Override
    public synchronized void clear()
    {
        _values.clear();
        updateLastRefresh();
    }
    
    /* (non-Javadoc)
     * @see info.mikaelsvensson.docase.core.Cache#get(java.lang.Object)
     */
    @Override
    public synchronized TYPE get(KEY key)
    {
        long now = System.currentTimeMillis();
        if (now > _lastRefresh + _timeout)
        {
            refresh();
        }
        TYPE value = _values.get(key);
        return value;
    }

    public synchronized void invalidate()
    {
        _lastRefresh = 0;
    }
    
    public synchronized void invalidateIfContainsKey(KEY key)
    {
        if (_values.containsKey(key))
        {
            invalidate();
        }
    }

    public synchronized void invalidateIfContainsValue(TYPE value)
    {
        if (_values.containsValue(value))
        {
            invalidate();
        }
    }
}
