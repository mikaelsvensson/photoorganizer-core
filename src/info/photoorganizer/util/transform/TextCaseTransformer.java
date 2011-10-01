package info.photoorganizer.util.transform;

import info.photoorganizer.util.I18n;

public class TextCaseTransformer extends SingleParameterTextTransformer
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public TextCaseTransformer cloneDeep()
    {
        TextCaseTransformer clone = new TextCaseTransformer();
        clone.setParam(getParam());
        return clone;
    }

    public TextCaseTransformer()
    {
        super();
    }

    public TextCaseTransformer(String param)
    {
        super(param);
    }

    public enum Transformation
    {
        LOWERCASE
        {
            @Override
            public String transform(String input)
            {
                return input.toLowerCase();
            }
        },
        UPPERCASE
        {
            @Override
            public String transform(String input)
            {
                return input.toUpperCase();
            }
        },
        CAPITALIZE
        {
            @Override
            public String transform(String input)
            {
                StringBuilder sb = new StringBuilder(input.length());
                boolean lastWasWhitespace = false;
                for (char c : input.toCharArray())
                {
                    sb.append(lastWasWhitespace ? Character.toUpperCase(c) : c);
                    
                    lastWasWhitespace = Character.isWhitespace(c);
                }
                return sb.toString();
            }
        };
        protected abstract String transform(String input);
        
        public String getI18nString(String key, Object... parameters)
        {
            return I18n.getInstance().getString(TextCaseTransformer.class,
                    Transformation.class.getSimpleName() + "." + key, parameters);
        }
        
        @Override
        public String toString()
        {
            return getI18nString(name());
        }
    }

    @Override
    public String transform(String input)
    {
        Transformation trans = Transformation.valueOf(getParam().toUpperCase());
        if (null != trans)
        {
            return trans.transform(input);
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
        Transformation trans = null;
        if (null != getParam())
        {
            trans = Transformation.valueOf(getParam().toUpperCase());
        }
        return i18n.getString(getClass(), "TOSTRING_PATTERN", trans/*.toString()*/);

    }

}
