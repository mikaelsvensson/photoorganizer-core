package info.photoorganizer.database;

import info.photoorganizer.metadata.AutoIndexTagDefinition;
import info.photoorganizer.metadata.DatabaseException;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.DatetimeTag;
import info.photoorganizer.metadata.DatetimeTagDefinition;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.ImageFileMetadataTag;
import info.photoorganizer.metadata.IndexingConfiguration;
import info.photoorganizer.metadata.IntegerNumberTag;
import info.photoorganizer.metadata.IntegerNumberTagDefinition;
import info.photoorganizer.metadata.KeywordExtractor;
import info.photoorganizer.metadata.KeywordTag;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.MetadataMappingConfiguration;
import info.photoorganizer.metadata.RationalNumberTag;
import info.photoorganizer.metadata.RationalNumberTagDefinition;
import info.photoorganizer.metadata.RealNumberTag;
import info.photoorganizer.metadata.RealNumberTagDefinition;
import info.photoorganizer.metadata.StringList;
import info.photoorganizer.metadata.Tag;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.metadata.TextTag;
import info.photoorganizer.metadata.TextTagDefinition;
import info.photoorganizer.metadata.ValueTag;
import info.photoorganizer.metadata.ValueTagDefinition;
import info.photoorganizer.util.FileIdentifier;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;

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
    
    public Image indexImage(File f)
    {
        Image img = getImage(f);
        if (null == img)
        {
            img = createImage();
            img.setFile(f);
        }
        
        try
        {
            Metadata metadata = ImageMetadataReader.readMetadata(f);
            
//            addDefaultTags(img, metadata);
            
            addTags(img, metadata);
            
            img.store();
        }
        catch (ImageProcessingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }

    private void addTags(Image img, Metadata metadata) throws DatabaseStorageException
    {
        Iterator<IndexingConfiguration> cfgs = getIndexingConfigurations();
        while (cfgs.hasNext())
        {
            IndexingConfiguration cfg = cfgs.next();
            FileFilter fileFilter = cfg.getFileFilter();
            if (null == fileFilter || fileFilter.accept(img.getFile()))
            {
                for (MetadataMappingConfiguration mapper : cfg.getMetadataMappers())
                {
                    ImageFileMetadataTag source = mapper.getSource();
                    Object value = getSourceData(metadata, mapper, source);
                    
                    List<Tag<? extends TagDefinition>> tags;
                    try
                    {
                        tags = createTargetTagsFromSourceData(mapper, value);
                        for (Tag<? extends TagDefinition> tag2 : tags)
                        {
                            if (null != tag2)
                            {
                                try
                                {
                                    img.addTag(tag2);
                                }
                                catch (DatabaseException e)
                                {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        img.store();
                    }
                    catch (DatabaseException e1)
                    {
                        e1.printStackTrace();
                    }
                    
                }
            }
        }
        img.store();
    }

    private List<Tag<? extends TagDefinition>> createTargetTagsFromSourceData(MetadataMappingConfiguration mapper, Object value) throws DatabaseStorageException, DatabaseException
    {
        List<Tag<? extends TagDefinition>> tags = new LinkedList<Tag<? extends TagDefinition>>();
        TagDefinition target = mapper.getTarget();
        if (target instanceof KeywordTagDefinition)
        {
            KeywordTagDefinition targetDef = (KeywordTagDefinition) target;

            if (!(value instanceof StringList))
            {
                value = new StringList(value.toString());
            }
            for (String v : (StringList)value)
            {
                KeywordTagDefinition keywordDef = targetDef.getChildByName(v, true);
                if (null == keywordDef)
                {
                    keywordDef = new KeywordTagDefinition(v, null, targetDef, getStorageStrategy());
                    keywordDef.store();
                }
                tags.add(new KeywordTag(keywordDef));
            }
        }
        else if (target instanceof ValueTagDefinition)
        {
            if (target instanceof TextTagDefinition)
            {
                if (!(value instanceof StringList))
                {
                    value = new StringList(value.toString());
                }
                for (String v : (StringList)value)
                {
                    TextTag valueTag = new TextTag((TextTagDefinition) target);
                    valueTag.setValue(v);
                    tags.add(valueTag);
                }
            }
            else if (target instanceof DatetimeTagDefinition && value instanceof Date)
            {
                DatetimeTag valueTag = new DatetimeTag((DatetimeTagDefinition) target);
                valueTag.setValue((Date) value);
                tags.add(valueTag);
            }
            else if (target instanceof IntegerNumberTagDefinition && value instanceof Integer)
            {
                IntegerNumberTag valueTag = new IntegerNumberTag((IntegerNumberTagDefinition) target);
                valueTag.setValue((Integer) value);
                tags.add(valueTag);
            }
            else if (target instanceof RationalNumberTagDefinition && value instanceof Rational)
            {
                RationalNumberTag valueTag = new RationalNumberTag((RationalNumberTagDefinition) target);
                valueTag.setValue((Rational) value);
                tags.add(valueTag);
            }
            else if (target instanceof RealNumberTagDefinition && value instanceof Double)
            {
                RealNumberTag valueTag = new RealNumberTag((RealNumberTagDefinition) target);
                valueTag.setValue((Double) value);
                tags.add(valueTag);
            }
            else
            {
                throw new DatabaseException("Cannot fit " + value.getClass().getName() + " into " + target.getClass().getSimpleName());
            }
        }
        return tags;
    }

    private Object getSourceData(Metadata metadata,
            MetadataMappingConfiguration mapper,
            ImageFileMetadataTag source)
    {
        Object value = source.getDatatype().get(metadata, source.getFileMetadataDirectory(), source.getFileMetadataDirectoryTag());
        if (value instanceof String)
        {
            value = mapper.applySourceTextTransformations((String) value);
        }
        else if (value instanceof StringList)
        {
            StringList newValue = new StringList();
            for (String v : (StringList)value)
            {
                newValue.add(mapper.applySourceTextTransformations(v));
            }
            value = newValue;
        }
        return value;
    }

    /*
    private void addKeywordToImage(Image img,
            IndexingConfiguration translator,
            String keyword)
    {
        String translated = translator.applySourceTextTransformations(keyword.trim());
        TagDefinition tagDefinition = getTagDefinition(translated);
        if (tagDefinition == null)
        {
            try
            {
                tagDefinition = createRootKeyword(translated);
                tagDefinition.store();
                System.out.println("Creating keyword '" + translated + "'.");
            }
            catch (DatabaseStorageException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.err.println("Could not create keyword '" + translated + "'.");
            }
        }
        
        if (tagDefinition instanceof KeywordTagDefinition)
        {
            try
            {
                KeywordTag tag = new KeywordTag((KeywordTagDefinition) tagDefinition);
                img.addTag(tag);
                System.out.println("Tagging image with '" + translated + "'.");
            }
            catch (DatabaseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    */
    private void addDefaultTags(Image img, Metadata metadata)
    {
        for (AutoIndexTagDefinition tagDef : AutoIndexTagDefinition.values())
        {
            try
            {
                ValueTag<? extends Object, ValueTagDefinition> tag = tagDef.createTagFromMetadata(metadata, this);
                img.addTag(tag);
            }
            catch (DatabaseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
    
    public Iterator<IndexingConfiguration> getIndexingConfigurations()
    {
        return getStorageStrategy().getIndexingConfigurations();
    }
    
    public void addIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        getStorageStrategy().addIndexingConfiguration(translator);
    }
    
    public void storeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        getStorageStrategy().storeIndexingConfiguration(translator);
    }
    
    public void replaceKeywordTagDefinition(KeywordTagDefinition old, KeywordTagDefinition replacement, boolean removeOld) throws DatabaseStorageException
    {
        Iterator<Image> iterator = getStorageStrategy().getImagesWithTag(old);
        while (iterator.hasNext())
        {
            Image image = iterator.next();
            image.removeKeywordTagsOfType(old);
            try
            {
                image.addTag(new KeywordTag(replacement));
            }
            catch (DatabaseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
    
    public IndexingConfiguration createIndexingConfiguration()
    {
        return new IndexingConfiguration(getStorageStrategy());
    }

    public void close() throws DatabaseStorageException
    {
        getStorageStrategy().close();
    }
 }
