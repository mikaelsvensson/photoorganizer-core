package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.Image;
import info.photoorganizer.metadata.Tag;
import info.photoorganizer.util.XMLUtilities;

import java.util.Iterator;

import org.w3c.dom.Element;

public class ImageHandler extends DatabaseObjectHandler<Image>
{
    public ImageHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(Image.class, storageStrategy);
    }

    private static String ATTRIBUTENAME_URL = "url";
    
    @Override
    public void readElement(Image o, Element el)
    {
        o.setUrl(XMLUtilities.getURLAttribute(el, ATTRIBUTENAME_URL, null));
        
        Iterator<Tag> i = _storageStrategy.fromElementChildren(el, Tag.class).iterator();
        while (i.hasNext())
        {
            o.addTag(i.next());
        }
        
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(Image o, Element el)
    {
        XMLUtilities.setURLAttribute(el, ATTRIBUTENAME_URL, o.getUrl());
        
        XMLUtilities.appendChildren(el, _storageStrategy.toElements(el.getOwnerDocument(), o.getTags()));

        super.writeElement(o, el);
    }

    @Override
    public Image createObject(Element el)
    {
        return new Image(_storageStrategy);
    }

    @Override
    public void storeElement(Image o) throws DatabaseStorageException
    {
        storeElementInRoot(o, DatabaseHandler.ELEMENTNAME_IMAGES);
//        Element rootKeywordContainerEl = XMLUtilities.getNamedChild(_storageStrategy.getDocument().getDocumentElement(), DatabaseHandler.ELEMENTNAME_IMAGES);
//        
//        Element newElement = createElement();
//        Element currentEl = _storageStrategy.getDatabaseObjectElement(o);
//        if (currentEl != null)
//        {
//            rootKeywordContainerEl.replaceChild(newElement, currentEl);
//        }
//        else
//        {
//            rootKeywordContainerEl.appendChild(newElement);
//        }
//        writeElement(o, newElement);
    }

}
