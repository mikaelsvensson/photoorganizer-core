package info.photoorganizer.database;

import info.photoorganizer.metadata.DatabaseException;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.DatetimeTag;
import info.photoorganizer.metadata.DatetimeTagDefinition;
import info.photoorganizer.metadata.DefaultTagDefinition;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.ImageFileMetadataTag;
import info.photoorganizer.metadata.IndexingConfiguration;
import info.photoorganizer.metadata.IntegerNumberTag;
import info.photoorganizer.metadata.IntegerNumberTagDefinition;
import info.photoorganizer.metadata.KeywordTag;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.MetadataMappingConfiguration;
import info.photoorganizer.metadata.RationalNumberTag;
import info.photoorganizer.metadata.RationalNumberTagDefinition;
import info.photoorganizer.metadata.RealNumberTag;
import info.photoorganizer.metadata.RealNumberTagDefinition;
import info.photoorganizer.metadata.Tag;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.metadata.TextTag;
import info.photoorganizer.metadata.TextTagDefinition;
import info.photoorganizer.metadata.ValueTagDefinition;
import info.photoorganizer.util.FileIdentifier;
import info.photoorganizer.util.StringList;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.Metadata;

public class Database extends DatabaseObject
{
    private static final double FILE_EQUALITY_PROBABLITY_MATCH_THRESHOLD = 1.0;

    private String name = null;
    
    public Database(DatabaseStorageStrategy storageContext)
    {
        this(null, storageContext);
    }

    public Database(UUID id, DatabaseStorageStrategy storageContext)
    {
        super(id, storageContext);
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
     */
    
    public void addIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        getStorageStrategy().addIndexingConfiguration(translator);
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
                    if (null != value)
                    {
                        setTargetData(img, mapper, value);
                    }
                    else
                    {
                        System.err.println("getSourceData returned 'null' for " + mapper.getTarget().getName());
                    }
                }
            }
            else
            {
                System.err.println("File filter did not allow tags for " + img.getFile() + " to be added to database.");
            }
        }
        img.store();
    }

    public void close() throws DatabaseStorageException
    {
        getStorageStrategy().close();
    }

    public Image createImage()
    {
        return new Image(getStorageStrategy());
    }

    public IndexingConfiguration createIndexingConfiguration()
    {
        return new IndexingConfiguration(getStorageStrategy());
    }

    public KeywordTagDefinition addRootKeyword(String name)
    {
        return getRootKeyword().addChild(name);
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
    
    public Iterator<Image> getImages()
    {
        return getStorageStrategy().getImages();
    }
    
    public Iterator<IndexingConfiguration> getIndexingConfigurations()
    {
        return getStorageStrategy().getIndexingConfigurations();
    }
    
    public String getName()
    {
        return name;
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
    
    public TagDefinition getTagDefinition(String name)
    {
        TagDefinition tag = getStorageStrategy().getTagDefinition(name);
        if (tag instanceof KeywordTagDefinition)
        {
            return getKeywordTagDefinition(tag.getId());
        }
        return tag;
    }
    
    public TagDefinition getTagDefinition(UUID id)
    {
        TagDefinition tag = getStorageStrategy().getTagDefinition(id);
        if (tag instanceof KeywordTagDefinition)
        {
            return getKeywordTagDefinition(tag.getId());
        }
        return tag;
    }

    public <T extends TagDefinition> T getTagDefinition(UUID id, Class<T> type)
    {
        T tag = getStorageStrategy().getTagDefinition(id, type);
        if (tag instanceof KeywordTagDefinition)
        {
            return (T) getKeywordTagDefinition(tag.getId());
        }
        return tag;
    }

    public Iterator<TagDefinition> getTagDefinitions()
    {
        return getStorageStrategy().getTagDefinitions();
    }
    
    private TagDefinition getKeywordTagDefinition(UUID id)
    {
        if (getRootKeyword().getId().equals(id))
        {
            return getRootKeyword();
        }
        else
        {
            return getRootKeyword().getChildById(id, true);
        }
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
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(f);
                
    //            addDefaultTags(img, metadata);
                
                addTags(img, metadata);
                
            }
            catch (ImageProcessingException e)
            {
                System.err.println("indexImage(" + f.getName() + "): " + e.getMessage());
                //e.printStackTrace();
            }
            catch (IOException e)
            {
                System.err.println("indexImage(" + f.getName() + "): " + e.getMessage());
                //e.printStackTrace();
            }
            img.store();
        }
        catch (DatabaseStorageException e)
        {
            System.err.println("indexImage(" + f.getName() + "): " + e.getMessage());
            //e.printStackTrace();
        }
        return img;
    }
    
    /**
     * Returns the keywords assigned to all the specified images, i.e. the
     * keywords that are shared amongst all the specified images.
     * 
     * @param images
     * @return
     */
    public Set<KeywordTagDefinition> keywordIntersection(Image... images)
    {
        Set<KeywordTagDefinition> res = new HashSet<KeywordTagDefinition>();
        if (images.length > 0)
        {
            Image ref = images[0];
            Iterator<Tag<? extends TagDefinition>> tags = ref.getTags();
            while (tags.hasNext())
            {
                TagDefinition def = tags.next().getDefinition();
                
                if (def instanceof KeywordTagDefinition)
                {
                    boolean isInAll = true;
                    for (int i=1; i < images.length; i++)
                    {
                        if (!images[i].hasTag(def))
                        {
                            isInAll = false;
                            break;
                        }
                    }
                    if (isInAll)
                    {
                        res.add((KeywordTagDefinition) def);
                    }
                }
            }
        }
        return res;
    }
    
    /**
     * Returns the keywords assigned to at least one of the specified images.
     * 
     * @param images
     * @return
     */
    public Set<KeywordTagDefinition> keywordUnion(Image... images)
    {
        Set<KeywordTagDefinition> res = new HashSet<KeywordTagDefinition>();
        for (Image image : images)
        {
            Iterator<Tag<? extends TagDefinition>> tags = image.getTags();
            while (tags.hasNext())
            {
                TagDefinition def = tags.next().getDefinition();
                
                if (def instanceof KeywordTagDefinition)
                {
                    res.add((KeywordTagDefinition) def);
                }
            }
        }
        return res;
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

    public void setName(String name)
    {
        if (equals(this.name, name)) return;
        this.name = name;
        fireChangedEvent();
    }

    private void setTargetData(Image img,
            MetadataMappingConfiguration mapper,
            Object value) throws DatabaseStorageException
    {
        try
        {
            List<Tag<? extends TagDefinition>> tags = createTargetTagsFromSourceData(mapper, value);
            for (Tag<? extends TagDefinition> tag : tags)
            {
                if (null != tag)
                {
                    try
                    {
                        img.addTag(tag);
                    }
                    catch (DatabaseException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (DatabaseException e1)
        {
            e1.printStackTrace();
        }
    }

    public void storeIndexingConfiguration(IndexingConfiguration translator) throws DatabaseStorageException
    {
        getStorageStrategy().storeIndexingConfiguration(translator);
    }

    private KeywordTagDefinition rootKeyword = null;
    
    public KeywordTagDefinition getRootKeyword()
    {
        if (rootKeyword == null)
        {
            rootKeyword = getStorageStrategy().getTagDefinition(DefaultTagDefinition.ROOT_KEYWORD.getId(), KeywordTagDefinition.class);
        }
        return rootKeyword;
    }
}
