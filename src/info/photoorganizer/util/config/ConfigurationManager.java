package info.photoorganizer.util.config;

import java.util.prefs.Preferences;


public class ConfigurationManager
{
    private static volatile ConfigurationManager cfgMngr = null;
    
    protected ConfigurationManager()
    {
        
    }
    
    public String getUserString(String configurationName, String def)
    {
        return getPreferences().get(configurationName, def);
    }
    
    public void setUserString(String configurationName, String value)
    {
        getPreferences().put(configurationName, value);
    }

    public Preferences getPreferences()
    {
        return Preferences.userNodeForPackage(getClass());
    }
    
    public static ConfigurationManager getInstance()
    {
        if (cfgMngr == null)
        {
            synchronized (ConfigurationManager.class)
            {
                if (cfgMngr == null)
                {
                    cfgMngr = new ConfigurationManager();
                }
            }
        }
        return cfgMngr;
    }
}
