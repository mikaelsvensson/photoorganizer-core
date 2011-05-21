package info.photoorganizer.metadata;

import java.util.UUID;

import org.w3c.dom.Element;

public class RealNumberTagDefinition extends ValueTagDefinition
{

    public static final RealNumberTagDefinition SHUTTER_SPEED = new RealNumberTagDefinition("shutter speed", UUID.fromString("e54d8495-81b9-48b4-a106-0f8d6d4780ca")); 
    public static final RealNumberTagDefinition APERTURE = new RealNumberTagDefinition("aperture", UUID.fromString("36d86990-728a-4b3d-ab6c-ab6daadb7d3e"));

    public RealNumberTagDefinition()
    {
        super();
    }

    public RealNumberTagDefinition(String name, UUID id)
    {
        super(name, id);
    }

    public RealNumberTagDefinition(String name)
    {
        super(name);
    }

}
