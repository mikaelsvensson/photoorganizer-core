package info.photoorganizer.metadata;

import info.photoorganizer.util.StringList;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;

public enum PhotoFileMetadataDatatype
{
    STRING {
        @Override
        public Object get(Directory directory, int tag)
        {
            return directory.getString(tag);
        }
    },
    STRING_LIST {
        @Override
        public Object get(Directory directory, int tag)
        {
            String[] values = directory.getStringArray(tag);
            StringList res = new StringList();
            if (values != null && values.length > 0)
            {
                for (String value : values)
                {
                    res.add(value);
                }
            }
            return res;
        }
    },
    RATIONAL {
        @Override
        public Object get(Directory directory, int tag)
        {
            return directory.getRational(tag);
        }
    },
    INTEGER {
        @Override
        public Object get(Directory directory, int tag)
        {
            return directory.getInteger(tag);
        }
    },
    DATETIME {
        @Override
        public Object get(Directory directory, int tag)
        {
            return directory.getDate(tag);
        }
    }
    ;
    
    public abstract Object get(Directory directory, int tag);
    
    public Object get(Metadata metadata, Class<? extends Directory> directoryClass, int tag)
    {
        Directory directory = metadata.getDirectory(directoryClass);
        if (null != directory)
        {
            return get(directory, tag);
        }
        return null;
    }
}
