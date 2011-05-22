package info.photoorganizer.metadata;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Image extends DatabaseObject
{
    
    private List<Tag<? extends TagDefinition>> _tags = new LinkedList<Tag<? extends TagDefinition>>();
    private URL _url = null;

    public Image()
    {
        super(null);
    }
    
    public Image(UUID id)
    {
        super(id);
    }

    public List<Tag<? extends TagDefinition>> getTags()
    {
        return _tags;
    }

    public URL getUrl()
    {
        return _url;
    }

    public void setUrl(URL url)
    {
        _url = url;
    }

}
