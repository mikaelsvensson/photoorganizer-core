package info.photoorganizer.util.config;

import junit.framework.Assert;

import org.junit.Test;

public class ConfigurationPropertyTest
{
    private static final String VALUE = "database.xml";

    @Test
    public void getDatabasePath()
    {
        ConfigurationProperty.databasePath.set(VALUE);
        String actual = ConfigurationProperty.databasePath.get();
        
        Assert.assertEquals(VALUE, actual);
    }
}
