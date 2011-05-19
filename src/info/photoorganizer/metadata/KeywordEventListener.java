package info.photoorganizer.metadata;

import java.util.EventListener;

public interface KeywordEventListener extends EventListener
{
    void keywordChanged(KeywordEvent event);
    void keywordInserted(KeywordEvent event);
    void keywordDeleted(KeywordEvent event);
    void keywordStructureChanged(KeywordEvent event);
}
