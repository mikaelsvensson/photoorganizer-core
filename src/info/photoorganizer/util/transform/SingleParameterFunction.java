package info.photoorganizer.util.transform;

import java.io.Serializable;

public abstract class SingleParameterFunction implements Serializable
{
    private String _param = null;

    public String getParam()
    {
        return _param;
    }

    protected SingleParameterFunction(String param)
    {
        super();
        _param = param;
    }
    
    protected SingleParameterFunction()
    {
        super();
    }

    public void setParam(String value)
    {
        _param = value;
    }

}
