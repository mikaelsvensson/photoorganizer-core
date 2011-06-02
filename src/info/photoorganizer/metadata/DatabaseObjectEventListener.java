package info.photoorganizer.metadata;

import java.util.EventListener;

public interface DatabaseObjectEventListener extends EventListener
{
    void databaseObjectChanged(DatabaseObjectEvent event);
}
