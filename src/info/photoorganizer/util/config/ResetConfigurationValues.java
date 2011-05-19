package info.photoorganizer.util.config;

public class ResetConfigurationValues
{
    public static void main(String[] args)
    {
        for (ConfigurationProperty<?> prop : ConfigurationProperty.values())
        {
            System.out.println(prop.name() + " was: " + prop.get());
            prop.reset();
            System.out.println(prop.name() + " is now: " + prop.get());
        }
    }
}
