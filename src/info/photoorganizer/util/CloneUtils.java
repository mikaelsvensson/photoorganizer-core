package info.photoorganizer.util;

import java.util.ArrayList;

public class CloneUtils
{
    public static <T extends Object> ArrayList<T> cloneArrayList(ArrayList<T> source)
    {
        if (source != null)
        {
            ArrayList<T> clone = new ArrayList<T>();
            for (T t : source)
            {
                if (t instanceof Clonable<?>)
                {
                    clone.add((T) ((Clonable<T>)t).cloneDeep());
                }
                else
                {
                    clone.add(t);
                }
            }
            return clone;
        }
        else
        {
            return null;
        }
    }
}
