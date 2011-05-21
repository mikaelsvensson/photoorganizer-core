package info.photoorganizer.metadata;

import info.photoorganizer.util.I18n;

import java.util.UUID;

public enum CoreTagDefinition
{
    APERTURE(RealNumberTagDefinition.APERTURE),
    CAMERA(TextTagDefinition.CAMERA),
    COMMENT(TextTagDefinition.COMMENT),
    DATE_TAKEN(TextTagDefinition.DATE_TAKEN),
    RATING(IntegerNumberTagDefinition.RATING),
    SHUTTER_SPEED(RealNumberTagDefinition.SHUTTER_SPEED), 
    ROOT_KEYWORD(KeywordTagDefinition.ROOT_KEYWORD);

    public TagDefinition getDefinition()
    {
        return _definition;
    }

    private TagDefinition _definition = null;

    private CoreTagDefinition(TagDefinition definition)
    {
        this._definition = definition;
    }
    
    @Override
    public String toString()
    {
        return I18n.getInstance().getString(getClass(), name());
    }
    
//    public static boolean isCoreTag(TagDefinition def)
//    {
//        for (CoreTagDefinition ctd : values())
//        {
//            if (def == ctd.getDefinition())
//            {
//                return true;
//            }
//        }
//        return false;
//    }
}
