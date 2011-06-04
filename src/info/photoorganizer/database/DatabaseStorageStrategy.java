package info.photoorganizer.database;

import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.IndexingConfiguration;
import info.photoorganizer.metadata.TagDefinition;

import java.util.Iterator;
import java.util.UUID;

public interface DatabaseStorageStrategy
{
//	public TagDefinition getRootTag();
    public Iterator<TagDefinition> getTagDefinitions();
	public TagDefinition getTagDefinition(UUID id);
	public TagDefinition getTagDefinition(String name);
	public <T extends TagDefinition> T getTagDefinition(UUID id, Class<T> type);
	public <T extends TagDefinition> T getTagDefinition(String name, Class<T> type);
	
	public void addTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException;
	public void removeTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException;
	public void storeTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException;
	
	public Iterator<Image> getImages();
	public Iterator<Image> getImagesWithTag(TagDefinition tagDefinition);
	public void addImage(Image img) throws DatabaseStorageException;
	public void removeImage(Image img) throws DatabaseStorageException;
	public void storeImage(Image img) throws DatabaseStorageException;
	
	public Iterator<IndexingConfiguration> getIndexingConfigurations();
	public void addIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException;
	public void removeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException;
	public void storeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException;
	
	public void close() throws DatabaseStorageException;
}
