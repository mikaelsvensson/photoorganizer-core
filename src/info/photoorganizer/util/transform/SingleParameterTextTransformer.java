package info.photoorganizer.util.transform;

public abstract class SingleParameterTextTransformer extends SingleParameterFunction implements TextTransformer
{

    protected SingleParameterTextTransformer()
    {
        super();
    }
    
    protected SingleParameterTextTransformer(String param)
    {
        super(param);
    }
}
