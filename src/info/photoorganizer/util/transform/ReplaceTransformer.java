package info.photoorganizer.util.transform;

public class ReplaceTransformer extends MultiParameterTextTransformer
{

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

}
