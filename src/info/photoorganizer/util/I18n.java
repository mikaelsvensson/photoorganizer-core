package info.photoorganizer.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class I18n
{
    private static volatile I18n singleton = null;
    
    private static Logger L = Log.getLogger(I18n.class);
    
    public static I18n getInstance()
    {
        if (singleton == null)
        {
            synchronized (I18n.class)
            {
                if (singleton == null)
                {
                    singleton = new I18n();
                }
            }
        }
        return singleton;
    }
    
    private Class<?> _defaultBundle = null;
    private Locale _preferredLocale = null;
    private Map<Locale, Map<String, ResourceBundle>> _resources = new HashMap<Locale, Map<String,ResourceBundle>>();
    
    protected I18n()
    {
        this(Locale.getDefault());
    }
    
    protected I18n(Class<?> defaultBundle)
    {
        this(null, defaultBundle);
    }
    
    protected I18n(Locale preferredLocale)
    {
        this(preferredLocale, null);
    }
    
    protected I18n(Locale preferredLocale, Class<?> defaultBundle)
    {
        _preferredLocale = preferredLocale;
        _defaultBundle = defaultBundle;
    }

    public final Locale getPreferredLocale()
    {
        return _preferredLocale;
    }

    public String getString(Class<?> bundle, String key, Object... parameters)
    {
        return getString(bundle.getName(), key, _preferredLocale, parameters);
    }

    public final void setPreferredLocale(Locale preferredLocale)
    {
        _preferredLocale = preferredLocale;
    }
    
    private ResourceBundle getBundle(String bundleName, Locale locale)
    {
        Map<String, ResourceBundle> bundles = _resources.get(locale);
        if (null == bundles)
        {
            bundles = new HashMap<String, ResourceBundle>();
            _resources.put(locale, bundles);
        }
        
        ResourceBundle bundle = bundles.get(bundleName);
        if (null == bundle)
        {
            try
            {
                bundle = ResourceBundle.getBundle(bundleName, locale);
            }
            catch (MissingResourceException e)
            {
                L.fine("Could not find properties file for " + bundleName);
                return null;
            }
            bundles.put(bundleName, bundle);
        }
        
        return bundle;
    }
    
    private String getProperty(String bundle, String key, Locale locale)
    {
        String[] bundleNames = null;
        if (_defaultBundle != null)
        {
            bundleNames = new String[] { bundle, _defaultBundle.getName() };
        }
        else
        {
            bundleNames = new String[] { bundle };
        }
        for (String bundleName : bundleNames)
        {
            ResourceBundle b = getBundle(bundleName, locale);
            if (null != b)
            {
                try
                {
                    return b.getString(key);
                }
                catch (MissingResourceException e)
                {
                    L.fine("Could not find property '" + key + "' in resource bundle " + bundleName);
                }
            }
        }
        return key;
    }
    
    private String getString(String bundle, String key, Locale locale, Object... parameters)
    {
        String prop = getProperty(bundle, key, locale);
        if (parameters.length > 0)
        {
            return MessageFormat.format(prop, parameters);
        }
        else
        {
            return prop;
        }
    }
}
