package info.photoorganizer.util.transform;

import java.util.HashMap;
import java.util.Map;

public abstract class MultiParameterFunction
{
    private Map<String, String> _params = new HashMap<String, String>();
//    private List<String> _paramNames = new ArrayList<String>();
//    
//    protected MultiParameterFunction(String... paramNames)
//    {
//        _paramNames.addAll(_paramNames);
//    }

    public String getParam(String name)
    {
        return _params.get(name);
    }
    
//    public Map<String, String> getParams()
//    {
//        return _params;
//    }

    public Iterable<String> getParamNames()
    {
        return _params.keySet();
//        return (String[]) _paramNames.toArray();
    }

    public void setParam(String name, String value)
    {
        _params.put(name, value);
    }

}
