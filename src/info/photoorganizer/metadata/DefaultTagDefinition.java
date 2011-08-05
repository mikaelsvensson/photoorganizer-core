package info.photoorganizer.metadata;

import info.photoorganizer.util.I18n;

import java.util.UUID;

public enum DefaultTagDefinition
{
    F_NUMBER(RationalNumberTagDefinition.class, UUID.fromString("36d86990-728a-4b3d-ab6c-ab6daadb7d3e"), false, false),
    CAMERA(TextTagDefinition.class, UUID.fromString("1622289b-f013-4243-960d-110b4da681c6"), false, false),
    COMMENT(TextTagDefinition.class, UUID.fromString("646e958d-1d34-4d4d-be88-40f1680c6005"), true, true),
    
    DATE_TAKEN(DatetimeTagDefinition.class, UUID.fromString("ee43022f-aa37-4b29-b5a2-27d07aa388a5"), false, false),

    RATING(IntegerNumberTagDefinition.class, UUID.fromString("26be7a9d-1ef8-4d5c-b32b-2258cf761a8b"), true, true), 
    ROOT_KEYWORD(KeywordTagDefinition.class, UUID.fromString("f4840997-b116-43e6-8256-6fbaefb63b3d"), false, true),
    
    EXPOSURE_TIME(RationalNumberTagDefinition.class, UUID.fromString("e54d8495-81b9-48b4-a106-0f8d6d4780ca"), false, false),
    ORIENTATION(IntegerNumberTagDefinition.class, UUID.fromString("2628b93f-e6fd-4a6c-adb4-1c29ffda7447"), false, false)
    
    /*
     * 5380469c-eefd-4f2c-bdc8-4a28807da371 
     * 1bc6e716-8120-4cb8-9e93-c0f939b27af9
     * 24782749-5c23-42fd-a1db-626394bb55c5 
     * 911e80f5-442d-4a86-962e-87f640035e10
     * de1119f4-5810-479b-b664-0ceab98d95b6 
     * 79c13113-3f35-4c6c-afe5-736a06f3d151
     * e2604f04-105c-48bc-b226-fd663d06709a 
     * f5959acf-7994-44f5-bd7c-fb69e08bff31
     */
    ;
    
    @Override
    public String toString()
    {
        return I18n.getInstance().getString(DefaultTagDefinition.class, name());
    }

    private Class<? extends TagDefinition> _definitionClass = null;
    private boolean _userAllowedToEditTags = false;
    private boolean _userAllowedToCreateTags = false;
    private UUID _id = null;

    public boolean isUserAllowedToEditTags()
    {
        return _userAllowedToEditTags;
    }

    public boolean isUserAllowedToCreateTags()
    {
        return _userAllowedToCreateTags;
    }

    private DefaultTagDefinition(
            Class<? extends TagDefinition> definitionClass, UUID id,
            boolean userAllowedToCreateTags, boolean userAllowedToEditTags)
    {
        _definitionClass = definitionClass;
        _id = id;
        _userAllowedToCreateTags = userAllowedToCreateTags;
        _userAllowedToEditTags = userAllowedToEditTags;
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
