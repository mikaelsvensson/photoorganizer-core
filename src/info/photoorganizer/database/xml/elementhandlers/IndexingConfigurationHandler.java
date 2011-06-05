package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.IndexingConfiguration;
import info.photoorganizer.metadata.KeywordTranslatorFileFilter;
import info.photoorganizer.metadata.MetadataMappingConfiguration;
import info.photoorganizer.util.XMLUtilities;

import java.util.Iterator;

import org.w3c.dom.Element;

public class IndexingConfigurationHandler extends DatabaseObjectHandler<IndexingConfiguration>
{
    private static final String ELEMENTNAME_METADATAMAPPINGCONFIGURATIONS = "MetadataMappingConfigurations";

    public IndexingConfigurationHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(IndexingConfiguration.class, storageStrategy);
    }

    @Override
    public void readElement(IndexingConfiguration o, Element el)
    {
        Iterator<KeywordTranslatorFileFilter> fileFilters = _storageStrategy.fromElementChildren(el, KeywordTranslatorFileFilter.class).iterator();
        if (fileFilters.hasNext())
        {
            o.setFileFilter(fileFilters.next());
        }
        
        Iterator<MetadataMappingConfiguration> i = _storageStrategy.fromElementChildren(XMLUtilities.getNamedChild(el, ELEMENTNAME_METADATAMAPPINGCONFIGURATIONS), MetadataMappingConfiguration.class).iterator();
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
        el.appendChild(_storageStrategy.toElement(el.getOwnerDocument(), o.getFileFilter()));
        
        Element mappersEl = createElement(ELEMENTNAME_METADATAMAPPINGCONFIGURATIONS, el.getOwnerDocument());
        el.appendChild(mappersEl);
        XMLUtilities.appendChildren(mappersEl, _storageStrategy.toElements(el.getOwnerDocument(), o.getMetadataMappers()));

        super.writeElement(o, el);
    }

    @Override
    public IndexingConfiguration createObject(Element el)
    {
        return new IndexingConfiguration(_storageStrategy);
    }

    @Override
    public void storeElement(IndexingConfiguration o) throws DatabaseStorageException
    {
        storeElementInRoot(o, DatabaseHandler.ELEMENTNAME_INDEXINGCONFIGURATIONS);
    }

    public void remove(IndexingConfiguration translator)
    {
        Element element = _storageStrategy.getDatabaseObjectElement(translator);
        element.getParentNode().removeChild(element);
    }

}
