package info.photoorganizer.database;

import info.photoorganizer.metadata.TagDefinition;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public interface DatabaseStorageStrategy
{
    public boolean handles(URL databaseUrl);
    
	public TagDefinition getRootTag();
	public TagDefinition getTag(UUID id);
	public TagDefinition getTag(String name);
	
	public void storeTag(TagDefinition tag) throws DatabaseStorageException;
    public void store(Database db) throws IOException;
    public Database load();
}
