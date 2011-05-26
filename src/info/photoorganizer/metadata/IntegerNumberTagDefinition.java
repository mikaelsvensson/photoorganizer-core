package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageStrategy;

import java.util.UUID;

public class IntegerNumberTagDefinition extends ValueTagDefinition
{
//    public static final IntegerNumberTagDefinition RATING = new IntegerNumberTagDefinition("rating", UUID.fromString("26be7a9d-1ef8-4d5c-b32b-2258cf761a8b"));

    public IntegerNumberTagDefinition(DatabaseStorageStrategy storageContext)
    {
        super(storageContext);
        // TODO Auto-generated constructor stub
    }

    public IntegerNumberTagDefinition(String name,
            DatabaseStorageStrategy storageContext)
    {
        super(name, storageContext);
        // TODO Auto-generated constructor stub
    }

    public IntegerNumberTagDefinition(String name, UUID id,
            DatabaseStorageStrategy storageContext)
    {
        super(name, id, storageContext);
        // TODO Auto-generated constructor stub
    }

}
