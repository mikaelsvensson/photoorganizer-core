package info.photoorganizer.metadata;

import java.util.ArrayList;
import java.util.Collection;

public class StringList extends ArrayList<String>
{

    /**
     * 
     */
    private static final long serialVersionUID = 2561426878459316036L;

    public StringList()
    {
        super();
    }

    public StringList(Collection<? extends String> c)
    {
        super(c);
    }

    public StringList(int initialCapacity)
    {
        super(initialCapacity);
    }
    
    public StringList(String... initialObjects)
    {
        super(initialObjects.length);
        for (String string : initialObjects)
        {
            add(string);
        }
    }

}
