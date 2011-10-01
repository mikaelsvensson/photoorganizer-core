package info.photoorganizer.util;

import info.photoorganizer.database.Database;
import info.photoorganizer.metadata.Tag;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log
{
    public static Logger UTIL = getPackageLogger(Log.class);
    public static Logger METADATA = getPackageLogger(Tag.class);
    public static Logger DATABASE = getPackageLogger(Database.class);
    
    static
    {
        ConsoleHandler regularConsole = new ConsoleHandler();
        regularConsole.setLevel(Level.ALL);
        regularConsole.setFormatter(new ConsoleFormatter());

//        UTIL.setLevel(Level.INFO);
//        UTIL.addHandler(regularConsole);
//        
//        METADATA.setLevel(Level.ALL);
//        METADATA.addHandler(regularConsole);
//        
//        METADATA.setLevel(Level.ALL);
    }
    
    static
    {
        Logger rootLogger = Logger.getLogger("");
        //rootLogger.setLevel(Level.ALL);
        
        for (Handler handler : rootLogger.getHandlers())
        {
            handler.setLevel(Level.ALL);
            if (handler instanceof ConsoleHandler)
            {
                ConsoleHandler consoleHandler = (ConsoleHandler) handler;
                consoleHandler.setFormatter(new Formatter() {
                    
                    @Override
                    public String format(LogRecord record)
                    {
                        String loggerLabel = record.getLoggerName();
                        loggerLabel = loggerLabel.substring(loggerLabel.lastIndexOf('.')+1);
                        return String.format("%5d %-30s %s\n", record.getSequenceNumber(), loggerLabel, record.getMessage());
                    }
                    
                });
            }
        }
        Logger poLogger = Logger.getLogger("info.photoorganizer");
        poLogger.setLevel(Level.ALL);
    }
    
    private static class ConsoleFormatter extends Formatter
    {
        @Override
        public String format(LogRecord record)
        {
            return record.getLevel().getName() + ": " + record.getMessage() + "\n";
        }
    }

    public synchronized static Logger getLogger(Class<?> c)
    {
        return Logger.getLogger(c.getName());
    }

    public synchronized static Logger getPackageLogger(Class<?> c)
    {
        return Logger.getLogger(c.getPackage().getName());
    }
}
