package info.photoorganizer.metadata;

import java.util.Date;

import com.drew.lang.Rational;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.iptc.IptcDirectory;


public abstract class ValueTag<V extends Object, T extends ValueTagDefinition> extends Tag<T>
{
    
    public ValueTag(T definition)
    {
        super(definition);
    }

    private V _value = null;

    public V getValue()
    {
        return _value;
    }

    public void setValue(V value)
    {
        _value = value;
    }
    
    @Override
    public String toString()
    {
        return getDefinition().getName() + ": " + getValue();
    }

    public static <T extends ValueTagDefinition> ValueTag<? extends Object, T> createFromMetadata(T def, Metadata metadata, Class<? extends Directory> metadataDirectory, int metadataTagId)
    {
        Directory directory = metadata.getDirectory(metadataDirectory);
        if (def instanceof IntegerNumberTagDefinition)
        {
            IntegerNumberTag tag = new IntegerNumberTag((IntegerNumberTagDefinition) def);
            tag.setValue(directory.getInteger(metadataTagId));
            return (ValueTag<? extends Object, T>) tag;
        }
        else if (def instanceof RationalNumberTagDefinition)
        {
            RationalNumberTag tag = new RationalNumberTag((RationalNumberTagDefinition) def);
            Rational rational = directory.getRational(metadataTagId);
            tag.setValue(rational);
            return (ValueTag<? extends Object, T>) tag;
        }
        else if (def instanceof DatetimeTagDefinition)
        {
            DatetimeTag tag = new DatetimeTag((DatetimeTagDefinition) def);
            Date date = directory.getDate(metadataTagId);
            tag.setValue(date);
            return (ValueTag<? extends Object, T>) tag;
        }
        else if (def instanceof RealNumberTagDefinition)
        {
            try
            {
                RealNumberTag tag = new RealNumberTag((RealNumberTagDefinition) def);
                tag.setValue(directory.getDouble(metadataTagId));
                return (ValueTag<? extends Object, T>) tag;
            }
            catch (MetadataException e)
            {
                e.printStackTrace();
            }
        }
        else if (def instanceof TextTagDefinition)
        {
            TextTag tag = new TextTag((TextTagDefinition) def);
            tag.setValue(directory.getString(metadataTagId));
            return (ValueTag<? extends Object, T>) tag;
        }
        return null;
    }
    
}
