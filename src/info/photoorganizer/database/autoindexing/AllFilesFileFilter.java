package info.photoorganizer.database.autoindexing;

import info.photoorganizer.util.I18n;

import java.io.File;
import java.io.Serializable;

public class AllFilesFileFilter implements POFileFilter, Serializable
{

    @Override
    public boolean accept(File pathname)
    {
        return true;
    }

    @Override
    public POFileFilter cloneDeep()
    {
        return new AllFilesFileFilter();
    }

    @Override
    public String toString()
    {
        return I18n.getInstance().getString(POFileFilter.class, "ALL_FILES_TOSTRING_PATTERN");
    }

}
