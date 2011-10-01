package info.photoorganizer.database.autoindexing;

import info.photoorganizer.database.Database;
import info.photoorganizer.metadata.PhotoFileMetadataTag;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.Clonable;
import info.photoorganizer.util.transform.TextTransformer;

import java.io.Serializable;
import java.util.List;

public interface MetadataMappingConfigurationInterface extends Serializable,
        Clonable<MetadataMappingConfigurationInterface>
{

    public abstract String applySourceTextTransformations(String input);

//    @Deprecated
    public abstract PhotoFileMetadataTag getSource();

//    public abstract UUID getSourceId();

    public abstract List<TextTransformer> getSourceTextTransformers();

//    @Deprecated
    public abstract TagDefinition getTarget(Database database);

//    public abstract UUID getTargetId();

    public abstract void setSource(PhotoFileMetadataTag value);

//    public abstract void setSourceId(UUID id);

    public abstract void setTarget(TagDefinition value);

//    public abstract void setTargetId(UUID id);

}