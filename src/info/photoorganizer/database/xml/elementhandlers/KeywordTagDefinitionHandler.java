package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.StorageContext;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KeywordTagDefinitionHandler extends TagDefinitionHandler<KeywordTagDefinition>
{
    public KeywordTagDefinitionHandler(StorageContext context)
    {
        super(KeywordTagDefinition.class, context);
    }

    private static String ATTRIBUTENAME_NAME = "name";
    private static String ATTRIBUTENAME_SYNONYMS = "synonyms";
    
//    private Map<KeywordTagDefinition, UUID[]> _synonyms = null;

    @Override
    public void readElement(KeywordTagDefinition o, Element el)
    {
        o.setName(XMLUtilities.getTextAttribute(el, ATTRIBUTENAME_NAME, "untitled"));
        
        o.setSynonymIds(XMLDatabaseStorageStrategy.getUUIDsAttribute(el, ATTRIBUTENAME_SYNONYMS));
        
        o.addChildren(fromElementChildren(el, KeywordTagDefinition.class));
        
        super.readElement(o, el);
    }
    
//    @Override
//    public void postProcess(Database db)
//    {
//        for (Entry<KeywordTagDefinition, UUID[]> entry : _synonyms.entrySet())
//        {
//            KeywordTagDefinition k = entry.getKey();
//            for (UUID synonymId : entry.getValue())
//            {
//                KeywordTagDefinition synonym = getById(synonymId);
//                if (null != synonym)
//                {
//                    k.addSynonym(synonymId);
//                }
//            }
//        }
//        
//        _synonyms = null;
//        
//        super.postProcess(db);
//    }
//    
//    private KeywordTagDefinition getById(UUID id)
//    {
//        for (KeywordTagDefinition def : _synonyms.keySet())
//        {
//            if (def.getId().equals(id))
//            {
//                return def;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void preProcess()
//    {
//        _synonyms = new HashMap<KeywordTagDefinition, UUID[]>();
//        
//        super.preProcess();
//    }

    @Override
    public void storeElement(KeywordTagDefinition o) throws DatabaseStorageException
    {
        /*
         * if (keyword has parent)
         *   if (parent keyword is in dom tree)
         *     create element for this keyword
         *     append element to parent
         *     fill element with data, including child keyword data
         *   else
         *     store parent keyword
         * else
         *   create element for this keyword
         *   if (keyword is in dom tree)
         *     replace element in document
         *   else
         *     add element to document as root keyword
         *   fill element with data, including child keyword data
         */
        
        KeywordTagDefinition parent = o.getParent();
        if (parent != null)
        {
            Element parentEl = getDatabaseObjectElement(parent);
            if (parentEl != null)
            {
                Element element = createElement();
                parentEl.appendChild(element);
                writeElement(o, element);
            }
            else
            {
                storeElement(parent);
            }
        }
        else
        {
            super.storeElement(o);
        }
        /*
        if (parent.isDirty())
        {
            storeElement(parent);
        }
        else
        {
            Element element = createElement();
            writeElement(o, element);
            
            Element parent = null;
            if (parent == null)
            {
                // Store as root keyword.
                parent = XMLUtilities.getNamedChild(_storageStrategy.getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_TAGDEFINITIONS);
            }
            else
            {
                // Store as child keyword.
                parent = _storageStrategy.getDatabaseObjectElement(parent);
            }
            parent.appendChild(element);
        }
        super.storeElement(o);
        */
    }

    @Override
    public void writeElement(KeywordTagDefinition o, Element el)
    {
        XMLUtilities.setTextAttribute(el, ATTRIBUTENAME_NAME, o.getName());
       
        XMLDatabaseStorageStrategy.setUUIDsAttribute(el, ATTRIBUTENAME_SYNONYMS, o.getSynonymIds());
        
        XMLUtilities.appendChildren(el, toElements(o.getChildren()));
        
        super.writeElement(o, el);
    }

    @Override
    public KeywordTagDefinition createObject(Element el)
    {
        return new KeywordTagDefinition(_context.getStrategy());
    }

    @Override
    public void remove(KeywordTagDefinition o) throws DatabaseStorageException
    {
        for (KeywordTagDefinition child : o.getChildren())
        {
            child.remove();
        }
        super.remove(o);
    }

}
