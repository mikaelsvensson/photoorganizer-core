package info.photoorganizer.metadata;

import java.util.EventListener;

public interface TagDefinitionEventListener extends EventListener
{
    void tagChanged(TagDefinitionEvent event);
}
