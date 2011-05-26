package info.photoorganizer.metadata;

import java.util.UUID;

public enum DefaultTagDefinition
{
    APERTURE(RealNumberTagDefinition.class, UUID.fromString("36d86990-728a-4b3d-ab6c-ab6daadb7d3e")),
    CAMERA(TextTagDefinition.class, UUID.fromString("1622289b-f013-4243-960d-110b4da681c6")),
    COMMENT(TextTagDefinition.class, UUID.fromString("646e958d-1d34-4d4d-be88-40f1680c6005")),
    
    DATE_TAKEN(TextTagDefinition.class, UUID.fromString("ee43022f-aa37-4b29-b5a2-27d07aa388a5")),

    RATING(IntegerNumberTagDefinition.class, UUID.fromString("26be7a9d-1ef8-4d5c-b32b-2258cf761a8b")), 
    ROOT_KEYWORD(KeywordTagDefinition.class, UUID.fromString("f4840997-b116-43e6-8256-6fbaefb63b3d")),
    
    SHUTTER_SPEED(RealNumberTagDefinition.class, UUID.fromString("e54d8495-81b9-48b4-a106-0f8d6d4780ca"))
    ;
    
    private Class<? extends TagDefinition> _definitionClass = null;
    private UUID _id = null;
    
    private DefaultTagDefinition(Class<? extends TagDefinition> definitionClass, UUID id)
    {
        _definitionClass = definitionClass;
        _id = id;
    }
    
    public Class<? extends TagDefinition> getDefinitionClass()
    {
        return _definitionClass;
    }
    
    public UUID getId()
    {
        return _id;
    }
}
