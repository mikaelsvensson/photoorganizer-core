package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageStrategy;

import java.util.UUID;

public class RationalNumberTagDefinition extends ValueTagDefinition
{

    public RationalNumberTagDefinition(DatabaseStorageStrategy storageContext)
    {
        super(storageContext);
    }
    
    public RationalNumberTagDefinition(String name, DatabaseStorageStrategy storageContext)
    {
        super(name, storageContext);
    }
    
    public RationalNumberTagDefinition(String name, UUID id, DatabaseStorageStrategy storageContext)
    {
        super(name, id, storageContext);
    }

}
