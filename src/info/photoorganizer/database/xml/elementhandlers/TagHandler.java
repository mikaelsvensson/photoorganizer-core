package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.PhotoRegion;
import info.photoorganizer.metadata.Tag;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Element;

public abstract class TagHandler<T extends Tag> extends ElementHandler<T>
{
    public static String ATTRIBUTENAME_DEFINITION = "tagdefinition";

    private static final String ATTRIBUTENAME_REGION = "region";

    public TagHandler(Class<T> cls, StorageContext context)
    {
        super(cls, context);
    }
    
    @Override
    public void readElement(T o, Element el)
    {
        
        TagDefinition tagDefinition = getTagDefinition(el, ATTRIBUTENAME_DEFINITION);
        o.setDefinition(tagDefinition);
        if (tagDefinition.isApplicableToPhotoRegion())
        {
            o.setRegion(PhotoRegion.valueOf(el.getAttribute(ATTRIBUTENAME_REGION)));
        }
        
        /*
        UUID uuid = XMLUtilities.getUUIDAttribute(el, ATTRIBUTENAME_DEFINITION);
        if (null != uuid)
        {
            DatabaseObject databaseObject = _processedObjects.get(uuid);
            if (databaseObject instanceof TagDefinition)
            {
                TagDefinition def = (TagDefinition)databaseObject;
                o.setDefinition(def);
                
                if (def.isApplicableToImageRegion())
                {
                    o.setRegion(ImageRegion.valueOf(el.getAttribute(ATTRIBUTENAME_REGION)));
                }
            }
        }
        */
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(T o, Element el)
    {
        XMLDatabaseStorageStrategy.setUUIDAttribute(el, ATTRIBUTENAME_DEFINITION, o.getDefinition().getId());
        
        PhotoRegion region = o.getRegion();
        if (o.getDefinition().isApplicableToPhotoRegion() && region != null)
        {
            XMLUtilities.setTextAttribute(el, ATTRIBUTENAME_REGION, region.toString());
        }
        
        super.writeElement(o, el);
    }

    @Override
    public void storeElement(T o) throws DatabaseStorageException
    {
        throw new DatabaseStorageException("One can not invoke storeElement directly for tags. Invoke storeElement for the tag's image instead.");
    }

}
