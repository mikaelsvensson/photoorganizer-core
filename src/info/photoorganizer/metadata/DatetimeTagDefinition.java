package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageStrategy;

import java.util.UUID;

public class DatetimeTagDefinition extends ValueTagDefinition
{

    public DatetimeTagDefinition(DatabaseStorageStrategy storageContext)
    {
        super(storageContext);
    }
    
    public DatetimeTagDefinition(String name, DatabaseStorageStrategy storageContext)
    {
        super(name, storageContext);
    }
    
    public DatetimeTagDefinition(String name, UUID id, DatabaseStorageStrategy storageContext)
    {
        super(name, id, storageContext);
    }

}
