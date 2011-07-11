package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.DatabaseException;
import info.photoorganizer.metadata.Photo;
import info.photoorganizer.metadata.Tag;
import info.photoorganizer.util.XMLUtilities;

import java.util.Iterator;

import org.w3c.dom.Element;

public class ImageHandler extends DatabaseObjectHandler<Photo>
{
    public ImageHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(Photo.class, storageStrategy);
    }

    private static String ATTRIBUTENAME_URI = "uri";
    
    @Override
    public void readElement(Photo o, Element el)
    {
        o.setURI(XMLUtilities.getURIAttribute(el, ATTRIBUTENAME_URI, null));
        
        Iterator<Tag> i = _storageStrategy.fromElementChildren(el, Tag.class).iterator();
        while (i.hasNext())
        {
            try
            {
                o.addTag(i.next());
            }
            catch (DatabaseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        super.readElement(o, el);
    }
    
    @Override
    public void writeElement(Photo o, Element el)
    {
        XMLUtilities.setURIAttribute(el, ATTRIBUTENAME_URI, o.getURI());
        
        XMLUtilities.appendChildren(el, _storageStrategy.toElements(el.getOwnerDocument(), o.getTags()));

        super.writeElement(o, el);
    }

    @Override
    public Photo createObject(Element el)
    {
        return new Photo(_storageStrategy);
    }

    @Override
    public void storeElement(Photo o) throws DatabaseStorageException
    {
        storeElementInRoot(o, DatabaseHandler.ELEMENTNAME_PHOTOS);
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
