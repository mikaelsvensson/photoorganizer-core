package info.photoorganizer.metadata;

import info.photoorganizer.util.transform.SingleParameterFunction;

import java.io.File;
import java.io.FileFilter;

public class KeywordTranslatorFileFilter extends SingleParameterFunction implements FileFilter
{

    public KeywordTranslatorFileFilter()
    {
        super();
    }
    
    public KeywordTranslatorFileFilter(String pattern)
    {
        super();
        setParam(pattern);
    }

    @Override
    public boolean accept(File pathname)
    {
        String name = pathname.getName();
        String regexp = getParam();
        boolean accepted = name.matches(regexp);
        return accepted;
    }

}
