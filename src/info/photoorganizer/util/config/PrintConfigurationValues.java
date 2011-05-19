package info.photoorganizer.util.config;

public class PrintConfigurationValues
{
    
    public static void main(String[] args)
    {
        for (ConfigurationProperty<?> prop : ConfigurationProperty.values())
        {
            System.out.println(prop.name() + ": " + prop.get());
        }
    }
    
}
