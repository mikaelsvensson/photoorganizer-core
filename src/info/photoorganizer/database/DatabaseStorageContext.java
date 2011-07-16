package info.photoorganizer.database;

import info.photoorganizer.metadata.Photo;
import info.photoorganizer.metadata.TagDefinition;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DatabaseStorageContext
{
    private DatabaseStorageStrategy strategy = null;

    public void close() throws DatabaseStorageException
    {
        strategy.close();
    }

    public void storePhoto(Photo photo) throws DatabaseStorageException
    {
        strategy.storePhoto(photo);
    }

    public Collection<TagDefinition> getTagDefinitions()
    {
        return strategy.getTagDefinitions();
    }

    public void addTagDefinition(TagDefinition tag) throws DatabaseStorageException
    {
        strategy.addTagDefinition(tag);
    }

    public void removeTagDefinition(TagDefinition tag) throws DatabaseStorageException
    {
        strategy.removeTagDefinition(tag);
    }

    public void addPhoto(Photo photo) throws DatabaseStorageException
    {
        strategy.addPhoto(photo);
    }

    public void removePhoto(Photo photo) throws DatabaseStorageException
    {
        strategy.removePhoto(photo);
    }

    public TagDefinition getTagDefinition(UUID id)
    {
        return strategy.getTagDefinition(id);
    }

    public TagDefinition getTagDefinition(String name)
    {
        return strategy.getTagDefinition(name);
    }

    public void storeTagDefinition(TagDefinition tag) throws DatabaseStorageException
    {
        strategy.storeTagDefinition(tag);
    }

    public DatabaseStorageContext(DatabaseStorageStrategy strategy)
    {
        super();
        this.strategy = strategy;
    }

//    public void store(Database db) throws IOException
//    {
//        strategy.store(db);
//    }
//    
//    public Database load()
//    {
//        return strategy.load();
//    }
}
