package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.Database;
import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.w3c.dom.Element;

public class KeywordTagDefinitionHandler extends DatabaseObjectHandler<KeywordTagDefinition>
{
    public KeywordTagDefinitionHandler(XMLDatabaseConverter converter)
    {
        super(KeywordTagDefinition.class, converter);
    }

    private static String ATTRIBUTENAME_NAME = "name";
    private static String ATTRIBUTENAME_SYNONYMS = "synonyms";
    
    private Map<KeywordTagDefinition, UUID[]> _synonyms = null;

    @Override
    public void readElement(KeywordTagDefinition o, Element el)
    {
        o.setName(XMLUtilities.getTextAttribute(el, ATTRIBUTENAME_NAME, "untitled"));
        
        _synonyms.put(o, XMLUtilities.getUUIDsAttribute(el, ATTRIBUTENAME_SYNONYMS));
        
        o.addChildren(_converter.fromElementChildren(el, KeywordTagDefinition.class));
        
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
                    k.addSynonym(synonym);
                }
            }
        }
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
    }

    @Override
    public void writeElement(KeywordTagDefinition o, Element el)
    {
        XMLUtilities.setTextAttribute(el, ATTRIBUTENAME_NAME, o.getName());
       
        XMLUtilities.setUUIDsAttribute(el, ATTRIBUTENAME_SYNONYMS, o.getSynonymIds());
        
        XMLUtilities.appendChildren(el, _converter.toElements(el.getOwnerDocument(), o.getChildren()));
        
        super.writeElement(o, el);
    }

}
