package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageStrategy;

import java.util.UUID;

public class RealNumberTagDefinition extends ValueTagDefinition
{

//    public static final RealNumberTagDefinition SHUTTER_SPEED = new RealNumberTagDefinition("shutter speed", UUID.fromString("e54d8495-81b9-48b4-a106-0f8d6d4780ca")); 
//    public static final RealNumberTagDefinition APERTURE = new RealNumberTagDefinition("aperture", UUID.fromString("36d86990-728a-4b3d-ab6c-ab6daadb7d3e"));
    public RealNumberTagDefinition(DatabaseStorageStrategy storageContext)
    {
        super(storageContext);
        // TODO Auto-generated constructor stub
    }
    public RealNumberTagDefinition(String name,
            DatabaseStorageStrategy storageContext)
    {
        super(name, storageContext);
        // TODO Auto-generated constructor stub
    }
    public RealNumberTagDefinition(String name, UUID id,
            DatabaseStorageStrategy storageContext)
    {
        super(name, id, storageContext);
        // TODO Auto-generated constructor stub
    }


}
