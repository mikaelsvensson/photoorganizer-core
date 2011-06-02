package info.photoorganizer.database;

import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.KeywordTag;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.Tag;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.FileIdentifier;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.UUID;

public class Database extends DatabaseObject
{
    private static final double FILE_EQUALITY_PROBABLITY_MATCH_THRESHOLD = 1.0;

    public Database(DatabaseStorageStrategy storageContext)
    {
        this(null, storageContext);
    }
    
    public Database(UUID id, DatabaseStorageStrategy storageContext)
    {
        super(id, storageContext);
    }

    public Iterator<Image> getImages()
    {
        return getStorageStrategy().getImages();
    }
    
    public Image getImage(File f)
    {
        double bestMatchPoints = 0;
        Image bestMatch = null;
        
        Iterator<Image> i = getImages();
        while (i.hasNext())
        {
            Image image = i.next();
            double probability = FileIdentifier.equalityProbability(f, image, FILE_EQUALITY_PROBABLITY_MATCH_THRESHOLD);
            if (probability > bestMatchPoints)
            {
                bestMatchPoints = probability;
                bestMatch = image;
            }
            if (bestMatchPoints >= FILE_EQUALITY_PROBABLITY_MATCH_THRESHOLD)
            {
                return bestMatch;
            }
        }
        return null;
    }
    
    public Iterator<TagDefinition> getTagDefinitions()
    {
        return getStorageStrategy().getTagDefinitions();
    }
    
    public TagDefinition getTagDefinition(String name)
    {
        return getStorageStrategy().getTagDefinition(name);
    }
    
    public TagDefinition getTagDefinition(UUID id)
    {
        return getStorageStrategy().getTagDefinition(id);
    }
    
    public <T extends TagDefinition> T getTagDefinition(UUID id, Class<T> type)
    {
        return getStorageStrategy().getTagDefinition(id, type);
    }
    
    public void replaceKeywordTagDefinition(KeywordTagDefinition old, KeywordTagDefinition replacement, boolean removeOld) throws DatabaseStorageException
    {
        Iterator<Image> iterator = getStorageStrategy().getImagesWithTag(old);
        while (iterator.hasNext())
        {
            Image image = iterator.next();
            image.removeKeywordTagsOfType(old);
            image.addTag(new KeywordTag(replacement));
            image.store();
        }
        if (removeOld)
        {
            old.remove();
        }
    }
    
    private String name = null;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if (equals(this.name, name)) return;
        this.name = name;
        fireChangedEvent();
    }
    
    public KeywordTagDefinition createRootKeyword(String name)
    {
        return new KeywordTagDefinition(name, getStorageStrategy());
    }
    
    public <T extends TagDefinition> T createTagDefinition(Class<T> cls, String name)
    {
        try
        {
            Constructor<T> constructor = cls.getDeclaredConstructor(String.class, DatabaseStorageStrategy.class);
            return constructor.newInstance(name, getStorageStrategy());
        }
        catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public Image createImage()
    {
        return new Image(getStorageStrategy());
    }

    public void close() throws DatabaseStorageException
    {
        getStorageStrategy().close();
    }
 }
