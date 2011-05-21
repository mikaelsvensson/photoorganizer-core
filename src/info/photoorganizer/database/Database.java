package info.photoorganizer.database;

import info.photoorganizer.metadata.CoreTagDefinition;
import info.photoorganizer.metadata.DatabaseObject;
import info.photoorganizer.metadata.KeywordTagDefinition;
import info.photoorganizer.metadata.TagDefinition;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Database extends DatabaseObject
{
    public Database()
    {
        this(null);
    }
    
    public Database(UUID id)
    {
        super(id);
//        setRootKeyword(null);
        initTagDefinitions();
    }
    
    private void initTagDefinitions()
    {
        _tagDefinitions.addAll(_coreTagDefinitions);
    }

//    private KeywordTagDefinition rootKeyword = null;
    
    private List<TagDefinition> _tagDefinitions = new LinkedList<TagDefinition>();
    private static List<TagDefinition> _coreTagDefinitions = new LinkedList<TagDefinition>();
    
    static
    {
        for (CoreTagDefinition def : CoreTagDefinition.values())
        {
            _coreTagDefinitions.add(def.getDefinition());
        }
    }
    
    public List<TagDefinition> getTagDefinitions()
    {
        return getTagDefinitions(false);
    }
    
    public List<TagDefinition> getTagDefinitions(boolean excludeCoreTags)
    {
        if (excludeCoreTags)
        {
            List<TagDefinition> res = new LinkedList<TagDefinition>(_tagDefinitions);
            res.removeAll(_coreTagDefinitions);
            return res;
        }
        else
        {
            return _tagDefinitions;
        }
    }
    
    public TagDefinition getTagDefinitionById(UUID id)
    {
        for (TagDefinition def : _tagDefinitions)
        {
            if (def.getId().equals(id))
            {
                return def;
            }
            else if (def instanceof KeywordTagDefinition)
            {
                for (KeywordTagDefinition descendant : ((KeywordTagDefinition)def).getDescendants())
                {
                    if (descendant.getId().equals(id))
                    {
                        return descendant;
                    }
                }
            }
        }
        return null;
    }
    
    public TagDefinition getTagDefinitionByName(String name)
    {
        for (TagDefinition def : _tagDefinitions)
        {
            if (def.getName().equals(name))
            {
                return def;
            }
            else if (def instanceof KeywordTagDefinition)
            {
                for (KeywordTagDefinition descendant : ((KeywordTagDefinition)def).getDescendants())
                {
                    if (descendant.getName().equals(name))
                    {
                        return descendant;
                    }
                }
            }
        }
        return null;
    }
    
    public void removeTagDefinition(TagDefinition toBeRemoved)
    {
        if (_tagDefinitions.contains(toBeRemoved))
        {
            _tagDefinitions.remove(toBeRemoved);
        }
        else if (toBeRemoved instanceof KeywordTagDefinition)
        {
            ((KeywordTagDefinition)toBeRemoved).remove();
        }
    }
    
    private String name = null;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

//    public KeywordTagDefinition getRootKeyword()
//    {
//        return rootKeyword;
//    }
//    
//    public void setRootKeyword(KeywordTagDefinition rootKeyword)
//    {
//        this.rootKeyword = rootKeyword;
//    }
//    
//    public KeywordTagDefinition getCoreKeyword(CoreTagDefinition tag)
//    {
//        if (null != rootKeyword)
//        {
//            return rootKeyword.getChildById(tag.getUuid(), true);
//        }
//        return null;
//    }
    
 }
