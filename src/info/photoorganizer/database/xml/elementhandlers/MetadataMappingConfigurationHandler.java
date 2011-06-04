package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.ImageFileMetadataTag;
import info.photoorganizer.metadata.ImageRegion;
import info.photoorganizer.metadata.MetadataMappingConfiguration;
import info.photoorganizer.metadata.Tag;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;
import info.photoorganizer.util.transform.SingleParameterFunction;
import info.photoorganizer.util.transform.SingleParameterTextTransformer;
import info.photoorganizer.util.transform.TextTransformer;

import java.util.Iterator;
import java.util.UUID;

import org.w3c.dom.Element;

public class MetadataMappingConfigurationHandler extends ElementHandler<MetadataMappingConfiguration>
{
    private static final String ATTRIBUTENAME_SOURCE = "source";
    private static final String ATTRIBUTENAME_TARGET = "target";

    public MetadataMappingConfigurationHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(MetadataMappingConfiguration.class, storageStrategy);
    }
    
    @Override
    public void readElement(MetadataMappingConfiguration o, Element el)
    {
        o.setSource(ImageFileMetadataTag.valueOf(el.getAttribute(ATTRIBUTENAME_SOURCE)));
        
        Iterator<TextTransformer> textTransformers = _storageStrategy.fromElementChildren(el, TextTransformer.class).iterator();
        while (textTransformers.hasNext())
        {
            o.getSourceTextTransformers().add(textTransformers.next());
        }
        
        o.setTarget(_storageStrategy.getTagDefinition(el, ATTRIBUTENAME_TARGET));
        
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(MetadataMappingConfiguration o, Element el)
    {
        el.setAttribute(ATTRIBUTENAME_SOURCE, o.getSource().name());
        
        XMLUtilities.appendChildren(el, _storageStrategy.toElements(el.getOwnerDocument(), o.getSourceTextTransformers()));
        
        _storageStrategy.setUUIDAttribute(el, ATTRIBUTENAME_TARGET, o.getTarget().getId());
        
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
        return new MetadataMappingConfiguration();
    }

}
