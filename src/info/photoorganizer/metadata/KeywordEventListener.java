package info.photoorganizer.metadata;


public interface KeywordEventListener extends TagEventListener
{
    void keywordInserted(KeywordEvent event);
    void keywordDeleted(KeywordEvent event);
    void keywordStructureChanged(KeywordEvent event);
}
