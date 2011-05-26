package info.photoorganizer.database;

import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.TagDefinition;

import java.util.Iterator;
import java.util.UUID;

public interface DatabaseStorageStrategy
{
//	public TagDefinition getRootTag();
    public Iterator<TagDefinition> getTagDefinitions();
	public TagDefinition getTagDefinition(UUID id);
	public TagDefinition getTagDefinition(String name);
	
	public void addTagDefinition(TagDefinition tag) throws DatabaseStorageException;
	public void removeTagDefinition(TagDefinition tag) throws DatabaseStorageException;
	
	public void addImage(Image img) throws DatabaseStorageException;
	public void removeImage(Image img) throws DatabaseStorageException;
	
	public void storeTagDefinition(TagDefinition tag) throws DatabaseStorageException;
	public void storeImage(Image img) throws DatabaseStorageException;
	
	public Iterator<Image> getImages();
	
	public void close();
//    public void store(Database db) throws IOException;
//    public Database load();
}
