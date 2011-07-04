package info.photoorganizer.util;

/**
 * Basic interface for any type of cache implementation.
 *
 * @param <TYPE> The type of object to cache.
 * @param <KEY>  The type of key used to locate the cached objects.
 */
public interface MapCache<TYPE, KEY>
{
    public TYPE get(KEY key);
    public void clear();
}