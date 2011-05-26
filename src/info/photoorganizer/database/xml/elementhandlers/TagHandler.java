package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.ImageRegion;
import info.photoorganizer.metadata.Tag;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import java.util.UUID;

import org.w3c.dom.Element;

public abstract class TagHandler<T extends Tag> extends ElementHandler<T>
{
    public static String ATTRIBUTENAME_DEFINITION = "tagdefinition";

    private static final String ATTRIBUTENAME_REGION = "region";

    public TagHandler(Class<T> cls, XMLDatabaseStorageStrategy storageStrategy)
    {
        super(cls, storageStrategy);
    }
    
    @Override
    public void readElement(T o, Element el)
    {
        
        TagDefinition tagDefinition = _storageStrategy.getTagDefinition(el, ATTRIBUTENAME_DEFINITION);
        o.setDefinition(tagDefinition);
        if (tagDefinition.isApplicableToImageRegion())
        {
            o.setRegion(ImageRegion.valueOf(el.getAttribute(ATTRIBUTENAME_REGION)));
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
        XMLUtilities.setUUIDAttribute(el, ATTRIBUTENAME_DEFINITION, o.getDefinition().getId());
        
        ImageRegion region = o.getRegion();
        if (o.getDefinition().isApplicableToImageRegion() && region != null)
        {
            XMLUtilities.setTextAttribute(el, ATTRIBUTENAME_REGION, region.toString());
        }
        
        super.writeElement(o, el);
    }

}
