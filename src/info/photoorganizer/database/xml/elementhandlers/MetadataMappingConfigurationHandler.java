package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.autoindexing.MetadataMappingConfiguration;
import info.photoorganizer.database.autoindexing.MetadataMappingConfigurationInterface;
import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.PhotoFileMetadataTag;
import info.photoorganizer.util.XMLUtilities;
import info.photoorganizer.util.transform.TextTransformer;

import java.util.Iterator;

import org.w3c.dom.Element;

public class MetadataMappingConfigurationHandler extends ElementHandler<MetadataMappingConfiguration>
{
    private static final String ATTRIBUTENAME_SOURCE = "source";
    private static final String ATTRIBUTENAME_TARGET = "target";

    public MetadataMappingConfigurationHandler(StorageContext context)
    {
        super(MetadataMappingConfiguration.class, context);
    }
    
    @Override
    public void readElement(MetadataMappingConfiguration o, Element el)
    {
        o.setSource(PhotoFileMetadataTag.valueOf(el.getAttribute(ATTRIBUTENAME_SOURCE)));
        
        Iterator<TextTransformer> textTransformers = fromElementChildren(el, TextTransformer.class).iterator();
        while (textTransformers.hasNext())
        {
            o.getSourceTextTransformers().add(textTransformers.next());
        }
        
        o.setTarget(getTagDefinition(el, ATTRIBUTENAME_TARGET));
        
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(MetadataMappingConfiguration o, Element el)
    {
        el.setAttribute(ATTRIBUTENAME_SOURCE, o.getSource().name());
        
        XMLUtilities.appendChildren(el, toElements(o.getSourceTextTransformers()));
        
        XMLDatabaseStorageStrategy.setUUIDAttribute(el, ATTRIBUTENAME_TARGET, o.getTarget(null).getId());
        
        super.writeElement(o, el);
    }

    @Override
    public void storeElement(MetadataMappingConfiguration o) throws DatabaseStorageException
    {
        throw new DatabaseStorageException("One can not invoke storeElement directly for text transformers. Invoke storeElement for the keyword translator instead.");
    }

    @Override
    public MetadataMappingConfiguration createObject(Element el)
    {
        return new MetadataMappingConfiguration(/*null*/);
    }

}
