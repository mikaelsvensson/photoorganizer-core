package info.photoorganizer.metadata;

import info.photoorganizer.util.I18n;

public enum KeywordExtension
{
    LOCATION;

    @Override
    public String toString()
    {
        return I18n.getInstance().getString(KeywordExtension.class, name());
    }
}
