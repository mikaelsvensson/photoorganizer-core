package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.xml.XMLDatabaseConverter;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.Tag;
import info.photoorganizer.util.XMLUtilities;

import java.util.Iterator;

import org.w3c.dom.Element;

public class ImageHandler extends ElementHandler<Image>
{
    public ImageHandler(XMLDatabaseConverter converter)
    {
        super(Image.class, converter);
    }

    private static String ATTRIBUTENAME_URL = "url";
    
    @Override
    public void readElement(Image o, Element el)
    {
        o.setUrl(XMLUtilities.getURLAttribute(el, ATTRIBUTENAME_URL, null));
        
        Iterator<Tag> i = _converter.fromElementChildren(el, Tag.class).iterator();
        while (i.hasNext())
        {
            o.getTags().add(i.next());
        }
        
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(Image o, Element el)
    {
        XMLUtilities.setURLAttribute(el, ATTRIBUTENAME_URL, o.getUrl());
        
        XMLUtilities.appendChildren(el, _converter.toElements(el.getOwnerDocument(), o.getTags()));

        super.writeElement(o, el);
    }

}
