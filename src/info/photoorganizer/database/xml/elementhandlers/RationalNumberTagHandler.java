package info.photoorganizer.database.xml.elementhandlers;

import info.photoorganizer.database.DatabaseStorageException;
import info.photoorganizer.database.xml.XMLDatabaseStorageStrategy;
import info.photoorganizer.metadata.RationalNumberTag;
import info.photoorganizer.metadata.RationalNumberTagDefinition;
import info.photoorganizer.metadata.TagDefinition;
import info.photoorganizer.util.XMLUtilities;

import org.w3c.dom.Element;

import com.drew.lang.Rational;

public class RationalNumberTagHandler extends TagHandler<RationalNumberTag>
{

    private static final String ATTRIBUTENAME_NUMERATOR = "numerator";
    private static final String ATTRIBUTENAME_DENOMINATOR = "denominator";

    public RationalNumberTagHandler(XMLDatabaseStorageStrategy storageStrategy)
    {
        super(RationalNumberTag.class, storageStrategy);
    }

    @Override
    public void readElement(RationalNumberTag o, Element el)
    {
        try
        {
            long numerator = XMLUtilities.getLongAttribute(el, ATTRIBUTENAME_NUMERATOR, 1l);
            long denominator = XMLUtilities.getLongAttribute(el, ATTRIBUTENAME_DENOMINATOR, 1l);
            Rational value = new Rational(numerator, denominator);
            o.setValue(value);
        }
        catch (DatabaseStorageException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        super.readElement(o, el);
    }

    @Override
    public void writeElement(RationalNumberTag o, Element el)
    {
        Rational value = o.getValue();
        if (null != value)
        {
            XMLUtilities.setLongAttribute(el, ATTRIBUTENAME_NUMERATOR, value.getNumerator());
            XMLUtilities.setLongAttribute(el, ATTRIBUTENAME_DENOMINATOR, value.getDenominator());
        }

        super.writeElement(o, el);
    }

    @Override
    public RationalNumberTag createObject(Element el)
    {
        TagDefinition tagDefinition = _storageStrategy.getTagDefinition(el, TagHandler.ATTRIBUTENAME_DEFINITION);
        return new RationalNumberTag((RationalNumberTagDefinition) tagDefinition);
    }

}
