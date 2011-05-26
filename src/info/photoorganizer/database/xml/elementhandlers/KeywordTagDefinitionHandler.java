package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.util.XMLUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.w3c.dom.Element;

public class KeywordTagDefinitionHandler extends DatabaseObjectHandler<KeywordTagDefinition>
{
    public KeywordTagDefinitionHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(KeywordTagDefinition.class, storageStrategy);
    }

    private static String ATTRIBUTENAME_NAME = "name";
    private static String ATTRIBUTENAME_SYNONYMS = "synonyms";
    
    private Map<KeywordTagDefinition, UUID[]> _synonyms = null;

    @Override
    public void readElement(KeywordTagDefinition o, Element el)
    {
        o.setName(XMLUtilities.getTextAttribute(el, ATTRIBUTENAME_NAME, "untitled"));
        
        
        for (UUID uuid : XMLUtilities.getUUIDsAttribute(el, ATTRIBUTENAME_SYNONYMS))
        {
            o.addSynonym(uuid);
        }
        //_synonyms.put(o, null);
        
        o.addChildren(_storageStrategy.fromElementChildren(el, KeywordTagDefinition.class));
        
        super.readElement(o, el);
    }
    
    @Override
    public void postProcess(Database db)
    {
        for (Entry<KeywordTagDefinition, UUID[]> entry : _synonyms.entrySet())
        {
            KeywordTagDefinition k = entry.getKey();
            for (UUID synonymId : entry.getValue())
            {
                KeywordTagDefinition synonym = getById(synonymId);
                if (null != synonym)
                {
                    k.addSynonym(synonymId);
                }
            }
        }
        
        _synonyms = null;
        
        super.postProcess(db);
    }
    
    private KeywordTagDefinition getById(UUID id)
    {
        for (KeywordTagDefinition def : _synonyms.keySet())
        {
            if (def.getId().equals(id))
            {
                return def;
            }
        }
        return null;
    }

    @Override
    public void preProcess()
    {
        _synonyms = new HashMap<KeywordTagDefinition, UUID[]>();
        
        super.preProcess();
    }

    @Override
    public void writeElement(KeywordTagDefinition o, Element el)
    {
        XMLUtilities.setTextAttribute(el, ATTRIBUTENAME_NAME, o.getName());
       
        XMLUtilities.setUUIDsAttribute(el, ATTRIBUTENAME_SYNONYMS, o.getSynonymIds());
        
        XMLUtilities.appendChildren(el, _storageStrategy.toElements(el.getOwnerDocument(), o.getChildren()));
        
        super.writeElement(o, el);
    }

    @Override
    public KeywordTagDefinition createObject(Element el)
    {
        return new KeywordTagDefinition(_storageStrategy);
    }

}
