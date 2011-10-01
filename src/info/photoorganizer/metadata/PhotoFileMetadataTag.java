package info.photoorganizer.metadata;

import info.photoorganizer.util.I18n;

import com.drew.metadata.Directory;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.iptc.IptcDirectory;

public enum PhotoFileMetadataTag
{
    EXIF_DATE_ORIGINAL(PhotoFileMetadataDatatype.DATETIME, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL),
    EXIF_EXPOSURE_TIME(PhotoFileMetadataDatatype.RATIONAL, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_EXPOSURE_TIME),
    EXIF_FNUMBER(PhotoFileMetadataDatatype.RATIONAL, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_FNUMBER),
    EXIF_ORIENTATION(PhotoFileMetadataDatatype.INTEGER, ExifIFD0Directory.class, ExifIFD0Directory.TAG_ORIENTATION),
    
    IPTC_SUPPLEMENTAL_CATEGORIES(PhotoFileMetadataDatatype.STRING_LIST, IptcDirectory.class, IptcDirectory.TAG_SUPPLEMENTAL_CATEGORIES)
    ;

    @Override
    public String toString()
    {
        return I18n.getInstance().getString(getClass(), name());
    }

    private PhotoFileMetadataDatatype _datatype = null;
    private Class<? extends Directory> _fileMetadataDirectory = null;

    private int _fileMetadataDirectoryTag = 0;

    private PhotoFileMetadataTag(PhotoFileMetadataDatatype datatype,
            Class<? extends Directory> fileMetadataDirectory,
            int fileMetadataDirectoryTag)
    {
        _datatype = datatype;
        _fileMetadataDirectory = fileMetadataDirectory;
        _fileMetadataDirectoryTag = fileMetadataDirectoryTag;
    }
    public PhotoFileMetadataDatatype getDatatype()
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
