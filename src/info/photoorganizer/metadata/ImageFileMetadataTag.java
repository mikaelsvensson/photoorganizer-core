package info.photoorganizer.metadata;

import java.util.Date;

import com.drew.lang.Rational;
import com.drew.metadata.Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.iptc.IptcDirectory;

public enum ImageFileMetadataTag
{
    EXIF_DATE_ORIGINAL(ImageFileMetadataDatatype.DATETIME, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL),
    EXIF_EXPOSURE_TIME(ImageFileMetadataDatatype.RATIONAL, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_EXPOSURE_TIME),
    EXIF_FNUMBER(ImageFileMetadataDatatype.RATIONAL, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_FNUMBER),
    
    IPTC_SUPPLEMENTAL_CATEGORIES(ImageFileMetadataDatatype.STRING_LIST, IptcDirectory.class, IptcDirectory.TAG_SUPPLEMENTAL_CATEGORIES)
    ;

    private ImageFileMetadataDatatype _datatype = null;
    private Class<? extends Directory> _fileMetadataDirectory = null;

    private int _fileMetadataDirectoryTag = 0;

    private ImageFileMetadataTag(ImageFileMetadataDatatype datatype,
            Class<? extends Directory> fileMetadataDirectory,
            int fileMetadataDirectoryTag)
    {
        _datatype = datatype;
        _fileMetadataDirectory = fileMetadataDirectory;
        _fileMetadataDirectoryTag = fileMetadataDirectoryTag;
    }
    public ImageFileMetadataDatatype getDatatype()
    {
        return _datatype;
    }

    public Class<? extends Directory> getFileMetadataDirectory()
    {
        return _fileMetadataDirectory;
    }
    
    public int getFileMetadataDirectoryTag()
    {
        return _fileMetadataDirectoryTag;
    }
    
}
