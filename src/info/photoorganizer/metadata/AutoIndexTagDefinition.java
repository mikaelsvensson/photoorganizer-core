package info.photoorganizer.metadata;

import info.photoorganizer.database.Database;

import java.util.UUID;

import com.drew.metadata.Metadata;

public enum AutoIndexTagDefinition
{
    F_NUMBER(DefaultTagDefinition.F_NUMBER.getId(), ImageFileMetadataTag.EXIF_FNUMBER),
    DATE_TAKEN(DefaultTagDefinition.DATE_TAKEN.getId(), ImageFileMetadataTag.EXIF_DATE_ORIGINAL),
    EXPOSURE_TIME(DefaultTagDefinition.EXPOSURE_TIME.getId(), ImageFileMetadataTag.EXIF_EXPOSURE_TIME)
    ;

    private ImageFileMetadataTag _fileTag = null;
    private UUID _targetTagDefinitionId = null;

    public UUID getTargetTagDefinitionId()
    {
        return _targetTagDefinitionId;
    }

    private AutoIndexTagDefinition(UUID targetTagDefinitionId,
            ImageFileMetadataTag fileTag)
    {
        _targetTagDefinitionId = targetTagDefinitionId;
        _fileTag = fileTag;
    }

    public ImageFileMetadataTag getFileTag()
    {
        return _fileTag;
    }
    
    public ValueTag<? extends Object, ValueTagDefinition> createTagFromMetadata(Metadata metadata, Database database)
    {
        if (null != _fileTag && _fileTag.getFileMetadataDirectory() != null && _fileTag.getFileMetadataDirectoryTag() > 0)
        {
            ValueTagDefinition def = database.getTagDefinition(_targetTagDefinitionId, ValueTagDefinition.class);
            if (null != def)
            {
                return ValueTag.createFromMetadata(def, metadata, _fileTag.getFileMetadataDirectory(), _fileTag.getFileMetadataDirectoryTag());
            }
        }
        return null;
    }

}
