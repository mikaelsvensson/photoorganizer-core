package info.photoorganizer.metadata;

import java.util.List;

import info.photoorganizer.util.StringUtils;
import info.photoorganizer.util.WordInfo;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.iptc.IptcDirectory;

public enum KeywordExtractor
{
    IPTC_CAPTION(IptcDirectory.class, IptcDirectory.TAG_CAPTION, false),
    IPTC_KEYWORDS(IptcDirectory.class, IptcDirectory.TAG_KEYWORDS, true),
    IPTC_SUPPLEMENTAL_CATEGORIES(IptcDirectory.class, IptcDirectory.TAG_SUPPLEMENTAL_CATEGORIES, true),
    EXIF_IMAGE_DESCRIPTION(ExifIFD0Directory.class, ExifIFD0Directory.TAG_IMAGE_DESCRIPTION, false)
    ;
    
    private Class<? extends Directory> _metadataDirectory = null;
    
    private int _metadataDirectoryTag = 0;
    
    private boolean _isList = false;
    
    private KeywordExtractor(Class<? extends Directory> metadataDirectory, int metadataDirectoryTag, boolean isList)
    {
        _metadataDirectory = metadataDirectory;
        _metadataDirectoryTag = metadataDirectoryTag;
        _isList = isList;
    }
    
    public String[] getKeywords(Metadata metadata, char separator, char quotation)
    {
        Directory directory = metadata.getDirectory(_metadataDirectory);
        if (null != directory)
        {
            if (_isList)
            {
                return directory.getStringArray(_metadataDirectoryTag);
            }
            else
            {
                String value = directory.getString(_metadataDirectoryTag);
                if (null != value && value.length() > 0)
                {
                    List<WordInfo> values = StringUtils.split(value, separator, quotation, false);
                    
                    int i = 0;
                    String[] res = new String[values.size()];
                    for (WordInfo wordInfo : values)
                    {
                        res[i++] = wordInfo.getWord();
                    }
                    return res;
                }
            }
        }
        return new String[0];
    }
}
