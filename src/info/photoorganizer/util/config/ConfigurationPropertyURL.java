package info.photoorganizer.util.config;

import java.net.MalformedURLException;
import java.net.URL;



public class ConfigurationPropertyURL extends ConfigurationProperty<URL>
{
    protected ConfigurationPropertyURL(URL defaultValue)
    {
        super(defaultValue);
    }

    protected ConfigurationPropertyURL(DefaultValueReader<URL> defaultValueReader)
    {
        super(defaultValueReader);
    }

    @Override
    public URL get()
    {
        try
        {
            return new URL(getPrefs().get(name(), getDefaultValue().toString()));
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            return getDefaultValue();
        }
    }

    @Override
    public void set(URL value)
    {
        getPrefs().put(name(), value.toString());
    }
    
}
