package info.photoorganizer.util.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.prefs.Preferences;

public abstract class ConfigurationProperty<T extends Object>
{
    public interface DefaultValueReader<T>
    {
        T get();
    }

    /*
     * CONFIGURATION PROPERTIES
     */
    public final static ConfigurationPropertyString databasePath = new ConfigurationPropertyString(new DefaultValueReader<String>()
    {
        @Override
        public String get()
        {
            return (new File(System.getProperty("user.home"), "photoorganizer-database.xml")).getAbsolutePath();
        }
    });
    
    /*
     * ACTUAL CODE
     */
    static
    {
        for (Field field : ConfigurationProperty.class.getDeclaredFields())
        {
            if (ConfigurationProperty.class.isAssignableFrom(field.getType()))
            {
                try
                {
                    ConfigurationProperty<?> type = (ConfigurationProperty<?>) field.get(ConfigurationProperty.class);
                    type.name = field.getName();
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static ConfigurationProperty[] values()
    {
        Field[] fields = ConfigurationProperty.class.getDeclaredFields();
        ConfigurationProperty[] all = new ConfigurationProperty[fields.length];
        int i = 0;
        for (Field field : fields)
        {
            if (ConfigurationProperty.class.isAssignableFrom(field.getType()))
            {
                try
                {
                    all[i++] = (ConfigurationProperty) field.get(ConfigurationProperty.class);
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (i != fields.length)
        {
            all = Arrays.copyOf(all, i);
        }
        return all;
    }
    
    private String name = null;
    
    private T defaultValue = null;
    private DefaultValueReader<T> defaultValueReader = null;
    
    protected ConfigurationProperty(DefaultValueReader<T> defaultValueReader)
    {
        super();
        this.defaultValueReader = defaultValueReader;
    }

    protected ConfigurationProperty(T defaultValue)
    {
        super();
        this.defaultValue = defaultValue;
    }
    
    protected T getDefaultValue()
    {
        if (null != defaultValue)
        {
            return defaultValue;
        }
        else if (null != defaultValueReader)
        {
            return defaultValueReader.get();
        }
        else
        {
            return null;
        }
    }

    public abstract T get();
    
    protected Preferences getPrefs()
    {
        return ConfigurationManager.getInstance().getPreferences();
    }
    
    public String name()
    {
        return name;
    }
    
    public abstract void set(T value);
    
    @Override
    public String toString()
    {
        return get().toString();
    }
    
    public void reset()
    {
        getPrefs().remove(name);
    }
}
