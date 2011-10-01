package info.photoorganizer.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

public class PopularityMapCache<TYPE, KEY> implements MapCache<TYPE, KEY>
{
    private static final int DEFAULT_PURGE_CYCLE = 10;
    private static final int DEFAULT_CACHE_SIZE = 10;
    private int _cacheSize = 0;
    private int _purgeCycle = 0;
    private int _addsUntilPurge = 0;
    private String _typeOfCacheName = null;

    private HashMap<KEY, CacheEntry> _cache = new HashMap<KEY, CacheEntry>();

    private class CacheEntry implements Comparable<CacheEntry>
    {
        private int _hits = 1;
        private TYPE _object = null;

        public CacheEntry(TYPE object)
        {
            _object = object;
        }

        public void increaseHits()
        {
            _hits++;
        }

        @Override
        public int compareTo(CacheEntry arg1)
        {
            return _hits - arg1.getHits();
        }

        public int getHits()
        {
            return _hits;
        }

        public TYPE getObject()
        {
            return _object;
        }

        @Override
        public boolean equals(Object o)
        {
            if (null == o)
            {
                return false;
            }
            if (null != _object)
            {
                return _object.equals(o);
            }
            else
            {
                return false;
            }
        }

        @Override
        public int hashCode()
        {
            if (null != _object)
            {
                return _object.hashCode();
            }
            else
            {
                return 0;
            }
        }
    }

    public PopularityMapCache()
    {
        this(DEFAULT_CACHE_SIZE, DEFAULT_PURGE_CYCLE);
    }

    public PopularityMapCache(int cacheSize)
    {
        this(cacheSize, DEFAULT_PURGE_CYCLE);
    }

    public PopularityMapCache(int cacheSize, int purgeCycle)
    {
        _cacheSize = cacheSize;
        _purgeCycle = purgeCycle;
        _addsUntilPurge = _purgeCycle;
    }

    public synchronized void add(KEY key, TYPE value)
    {
        if (_addsUntilPurge == 0)
        {
            _addsUntilPurge = _purgeCycle;
            purge(false);
        }
        else
        {
            _addsUntilPurge--;
        }

        if (!_cache.containsKey(key))
        {
            log("Added '" + value + "' to cache.");
            _cache.put(key, new CacheEntry(value));
        }
    }

    /* (non-Javadoc)
     * @see info.mikaelsvensson.docase.core.Cache#get(KEY)
     */
    public TYPE get(KEY key)
    {
        CacheEntry entry = _cache.get(key);
        if (null != entry)
        {
            entry.increaseHits();
            log("Cache hit for '" + entry.getObject() + "' (this was hit number " + entry.getHits() + ")");
            return entry.getObject();
        }
        log("Cache miss for " + key);
        return null;
    }

    /* (non-Javadoc)
     * @see info.mikaelsvensson.docase.core.Cache#clear()
     */
    public synchronized void clear()
    {
        purge(true);
    }
    
    private synchronized void purge(boolean all)
    {
        if (all)
        {
            _cache.clear();
        }
        else if (_cache.size() > _cacheSize)
        {
            List<CacheEntry> list = new ArrayList<CacheEntry>();
            list.addAll(_cache.values());
            Collections.sort(list);
            List<CacheEntry> listToPurge = list.subList(_cacheSize, list.size());

            for (Entry<KEY, CacheEntry> entry : _cache.entrySet())
            {
                if (listToPurge.contains(entry.getValue()))
                {
                    log("Removed '" + entry + "' from cache.");
                    _cache.remove(entry.getKey());
                }
            }
        }
    }

    public synchronized void invalidateEntry(KEY key)
    {
        _cache.remove(key);
    }

    private void log(String msg)
    {
        if (_typeOfCacheName == null)
        {
            _typeOfCacheName = getTypeOfCacheName();
        }
        Log.UTIL.log(Level.FINEST, "Cache for " + (_typeOfCacheName != null ? _typeOfCacheName : "unknown type of objects") + ": " + msg);
    }

    private synchronized String getTypeOfCacheName()
    {
        for (Entry<KEY, CacheEntry> entry : _cache.entrySet())
        {
            if (entry.getValue()._object != null)
            {
                return entry.getValue()._object.getClass().getSimpleName();
            }
        }
        return null;
    }
}
