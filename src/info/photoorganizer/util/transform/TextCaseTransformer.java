package info.photoorganizer.util.transform;

public class TextCaseTransformer extends SingleParameterTextTransformer
{
    public enum Transformation implements TextTransformer
    {
        LOWERCASE()
        {
            @Override
            public String transform(String input)
            {
                return input.toLowerCase();
            }
        },
        UPPERCASE()
        {
            @Override
            public String transform(String input)
            {
                return input.toLowerCase();
            }
        },
        CAPITALIZE()
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

}
