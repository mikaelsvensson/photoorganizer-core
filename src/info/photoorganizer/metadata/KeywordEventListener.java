package info.photoorganizer.metadata;


public interface KeywordEventListener extends TagDefinitionEventListener
{
    void keywordInserted(KeywordEvent event);
    void keywordDeleted(KeywordEvent event);
    void keywordStructureChanged(KeywordEvent event);
}
