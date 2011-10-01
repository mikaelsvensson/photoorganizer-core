package info.photoorganizer.database.autoindexing;

import info.photoorganizer.util.Clonable;

import java.io.FileFilter;
import java.io.Serializable;
import java.util.List;

public interface IndexingConfigurationInterface extends Serializable, Clonable<IndexingConfigurationInterface>
{

    public abstract POFileFilter getFileFilter();
    
    public abstract void setFileFilter(POFileFilter filter);

    public abstract List<MetadataMappingConfigurationInterface> getMetadataMappers();

}