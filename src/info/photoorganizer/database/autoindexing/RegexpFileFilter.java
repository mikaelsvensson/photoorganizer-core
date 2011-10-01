package info.photoorganizer.database.autoindexing;

import info.photoorganizer.util.I18n;
import info.photoorganizer.util.transform.SingleParameterFunction;

import java.io.File;

public class RegexpFileFilter extends SingleParameterFunction implements POFileFilter
{

    @Override
    public RegexpFileFilter cloneDeep()
    {
        return new RegexpFileFilter(getParam());
    }

    public RegexpFileFilter()
    {
        super();
    }
    
    public RegexpFileFilter(String pattern)
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

    @Override
    public String toString()
    {
        return I18n.getInstance().getString(POFileFilter.class, "REGEXP_TOSTRING_PATTERN", getParam());
    }
}
