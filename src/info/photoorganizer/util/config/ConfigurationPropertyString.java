package info.photoorganizer.util.config;



public class ConfigurationPropertyString extends ConfigurationProperty<String>
{
    protected ConfigurationPropertyString(String defaultValue)
    {
        super(defaultValue);
    }

    protected ConfigurationPropertyString(DefaultValueReader<String> defaultValueReader)
    {
        super(defaultValueReader);
    }

    @Override
    public String get()
    {
        return getPrefs().get(name(), getDefaultValue());
    }

    @Override
    public void set(String value)
    {
        getPrefs().put(name(), value);
    }
    
}
