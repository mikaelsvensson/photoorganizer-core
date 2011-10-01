package info.photoorganizer.util.transform;

import info.photoorganizer.util.I18n;

public class ReplaceTransformer extends MultiParameterTextTransformer
{

    @Override
    public ReplaceTransformer cloneDeep()
    {
        return new ReplaceTransformer(getParam(PARAM_OLD), getParam(PARAM_REPLACEMENT));
    }

    public static final String PARAM_OLD = "target";
    public static final String PARAM_REPLACEMENT = "replacement";

    public ReplaceTransformer()
    {
        super();
    }

    public ReplaceTransformer(String target, String replacement)
    {
        setParam(PARAM_OLD, target);
        setParam(PARAM_REPLACEMENT, replacement);
    }

    @Override
    public String transform(String input)
    {
        String old = getParam(PARAM_OLD);
        String replacement = getParam(PARAM_REPLACEMENT);

        if (null != old && null != replacement && old.length() > 0)
        {
            return input.replace(old, replacement);
        }
        else
        {
            return input;
        }
    }

    @Override
    public String toString()
    {
        I18n i18n = I18n.getInstance();
        String old = getParam(PARAM_OLD);
        String replacement = getParam(PARAM_REPLACEMENT);
        return i18n.getString(getClass(), "TOSTRING_PATTERN", old, replacement);
    }

}
