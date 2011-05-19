package info.photoorganizer.util.config;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationManagerTest
{
    private static final String CONFIG_NAME = "test";
    private static final String CONFIG_VALUE = "test value";

    @Test
    public void putAndGet()
    {
        ConfigurationManager manager = ConfigurationManager.getInstance();
        
        manager.setUserString(CONFIG_NAME, CONFIG_VALUE);
        String returned = manager.getUserString(CONFIG_NAME, null);
        
        Assert.assertEquals(CONFIG_VALUE, returned);
    }
}
