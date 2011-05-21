package info.photoorganizer.metadata;

import java.util.UUID;

import org.w3c.dom.Element;

public class IntegerNumberTagDefinition extends ValueTagDefinition
{
    public static final IntegerNumberTagDefinition RATING = new IntegerNumberTagDefinition("rating", UUID.fromString("26be7a9d-1ef8-4d5c-b32b-2258cf761a8b"));

    public IntegerNumberTagDefinition()
    {
        super();
    }

    public IntegerNumberTagDefinition(String name, UUID id)
    {
        super(name, id);
    }

    public IntegerNumberTagDefinition(String name)
    {
        super(name);
    }

}
