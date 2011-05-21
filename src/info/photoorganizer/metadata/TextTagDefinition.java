package info.photoorganizer.metadata;

import java.util.UUID;

public class TextTagDefinition extends ValueTagDefinition
{

    public static final TextTagDefinition CAMERA = new TextTagDefinition("camera", UUID.fromString("1622289b-f013-4243-960d-110b4da681c6"));
    public static final TextTagDefinition COMMENT = new TextTagDefinition("comment", UUID.fromString("646e958d-1d34-4d4d-be88-40f1680c6005"));
    public static final TextTagDefinition DATE_TAKEN = new TextTagDefinition("date taken", UUID.fromString("ee43022f-aa37-4b29-b5a2-27d07aa388a5"));

    public TextTagDefinition()
    {
        super();
    }

    public TextTagDefinition(String name, UUID id)
    {
        super(name, id);
    }

    public TextTagDefinition(String name)
    {
        super(name);
    }

}
