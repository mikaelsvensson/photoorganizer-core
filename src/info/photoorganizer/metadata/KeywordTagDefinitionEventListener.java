package info.photoorganizer.metadata;


public interface KeywordTagDefinitionEventListener extends TagDefinitionEventListener
{
    void keywordInserted(KeywordTagDefinitionEvent event);
    void keywordStructureChanged(KeywordTagDefinitionEvent event);
}
