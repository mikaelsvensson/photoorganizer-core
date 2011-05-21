package info.photoorganizer.metadata;

import java.util.EventListener;

public interface TagEventListener extends EventListener
{
    void tagChanged(TagEvent event);
}
