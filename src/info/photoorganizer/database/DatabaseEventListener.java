package info.photoorganizer.database;

import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.Photo;
import info.photoorganizer.metadata.TagDefinition;

import java.io.File;
import java.util.EventListener;

public interface DatabaseEventListener extends EventListener
{
    void fileIndexed(File file);
    void keywordAdded(KeywordTagDefinition keyword);
    void keywordRemove(KeywordTagDefinition keyword);
    void keywordChanged(KeywordTagDefinition keyword);
    void tagAdded(TagDefinition keyword);
    void tagRemove(TagDefinition keyword);
    void tagChanged(TagDefinition keyword);
    void photoAdded(Photo photo);
    void photoRemoved(Photo photo);
    void photoChanged(Photo photo);
}
