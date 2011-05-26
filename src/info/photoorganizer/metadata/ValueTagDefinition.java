package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageStrategy;

import java.util.UUID;

public abstract class ValueTagDefinition extends TagDefinition
{
    
    public ValueTagDefinition(DatabaseStorageStrategy storageContext)
    {
        super(storageContext);
    }

    public ValueTagDefinition(String name, DatabaseStorageStrategy storageContext)
    {
        super(name, storageContext);
    }

    public ValueTagDefinition(String name, UUID id, DatabaseStorageStrategy storageContext)
    {
        super(name, id, storageContext);
    }
    
}
