package info.photoorganizer.database;

import info.photoorganizer.metadata.IndexingConfiguration;
import info.photoorganizer.metadata.Photo;
import info.photoorganizer.metadata.TagDefinition;

import java.util.Collection;
import java.util.UUID;

public interface DatabaseStorageStrategy
{
    public Collection<TagDefinition> getTagDefinitions();
	public TagDefinition getTagDefinition(UUID id);
	public TagDefinition getTagDefinition(String name);
	public <T extends TagDefinition> T getTagDefinition(UUID id, Class<T> type);
	public <T extends TagDefinition> T getTagDefinition(String name, Class<T> type);
	
	public void addTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException;
	public void removeTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException;
	public void storeTagDefinition(TagDefinition tagDefinition) throws DatabaseStorageException;
	
	public Collection<Photo> getPhotos();
	public Collection<Photo> getPhotosWithTag(TagDefinition tagDefinition);
	public void addPhoto(Photo img) throws DatabaseStorageException;
	public void removePhoto(Photo img) throws DatabaseStorageException;
	public void storePhoto(Photo img) throws DatabaseStorageException;
	
	public Collection<IndexingConfiguration> getIndexingConfigurations();
	public void addIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException;
	public void removeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException;
	public void storeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException;
	
	public void close() throws DatabaseStorageException;
}
