package info.photoorganizer.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class L
{
    public static Logger UTIL = Logger.getLogger("info.photoorganizer.util");
    public static Logger METADATA = Logger.getLogger("info.photoorganizer.metadata");
    public static Logger DATABASE = Logger.getLogger("info.photoorganizer.database");
    
    static
    {
        ConsoleHandler regularConsole = new ConsoleHandler();
        regularConsole.setLevel(Level.ALL);
        regularConsole.setFormatter(new ConsoleFormatter());

        UTIL.setLevel(Level.INFO);
        UTIL.addHandler(regularConsole);
        
        METADATA.setLevel(Level.ALL);
        METADATA.addHandler(regularConsole);
        
        METADATA.setLevel(Level.ALL);
    }
    
    private static class ConsoleFormatter extends Formatter
    {
        @Override
        public String format(LogRecord record)
        {
            return record.getLevel().getName() + ": " + record.getMessage() + "\n";
        }
    }
}
