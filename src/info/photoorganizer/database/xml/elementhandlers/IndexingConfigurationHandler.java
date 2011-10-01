package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.autoindexing.IndexingConfigurationInterface;
import info.photoorganizer.database.autoindexing.MetadataMappingConfiguration;
import info.photoorganizer.database.autoindexing.RegexpFileFilter;
import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.metadata.IndexingConfiguration;
import info.photoorganizer.util.XMLUtilities;

import java.util.Iterator;

import org.w3c.dom.Element;

public class IndexingConfigurationHandler extends DatabaseObjectHandler<IndexingConfiguration>
{
    private static final String ELEMENTNAME_METADATAMAPPINGCONFIGURATIONS = "MetadataMappingConfigurations";

    public IndexingConfigurationHandler(StorageContext context)
    {
        super(IndexingConfiguration.class, context);
    }

    @Override
    public void readElement(IndexingConfiguration o, Element el)
    {
        Iterator<RegexpFileFilter> fileFilters = fromElementChildren(el, RegexpFileFilter.class).iterator();
        if (fileFilters.hasNext())
        {
            o.setFileFilter(fileFilters.next());
        }
        
        Iterator<MetadataMappingConfiguration> i = fromElementChildren(XMLUtilities.getNamedChild(el, ELEMENTNAME_METADATAMAPPINGCONFIGURATIONS), MetadataMappingConfiguration.class).iterator();
        while (i.hasNext())
        {
            o.getMetadataMappers().add(i.next());
        }
        
        super.readElement(o, el);
    }

//    private void addKeywordExtractors(IndexingConfiguration o, Element el)
//    {
//        List<KeywordExtractor> extractors = o.getExtractors();
//        extractors.clear();
//        String extractorsAttr = el.getAttribute(ATTRIBUTENAME_EXTRACTORS);
//        if (extractorsAttr.length() > 0)
//        {
//            for (String extractorName : StringUtils.split(extractorsAttr, EXTRACTOR_NAME_GLUE))
//            {
//                KeywordExtractor extractor = KeywordExtractor.valueOf(extractorName);
//                if (null != extractor)
//                {
//                    extractors.add(extractor);
//                }
//            }
//        }
//        else
//        {
//            for (KeywordExtractor extractor : KeywordExtractor.values())
//            {
//                extractors.add(extractor);
//            }
//        }
//    }
    
    @Override
    public void writeElement(IndexingConfiguration o, Element el)
    {
        el.appendChild(toElement(o.getFileFilter()));
        
        Element mappersEl = createElement(ELEMENTNAME_METADATAMAPPINGCONFIGURATIONS, el.getOwnerDocument());
        el.appendChild(mappersEl);
        XMLUtilities.appendChildren(mappersEl, toElements(o.getMetadataMappers()));

        super.writeElement(o, el);
    }

    @Override
    public IndexingConfiguration createObject(Element el)
    {
        return new IndexingConfiguration(_context.getStrategy());
    }

    @Override
    public void storeElement(IndexingConfiguration o) throws DatabaseStorageException
    {
        storeElementInRoot(o, DatabaseHandler.ELEMENTNAME_INDEXINGCONFIGURATIONS);
    }

    public void remove(IndexingConfiguration translator)
    {
        Element element = getDatabaseObjectElement(translator);
        element.getParentNode().removeChild(element);
    }

}
