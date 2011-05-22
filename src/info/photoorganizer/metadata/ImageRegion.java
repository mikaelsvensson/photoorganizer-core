package info.photoorganizer.metadata;

import info.photoorganizer.util.StringUtils;

public class ImageRegion
{
    private double _bottom = 1;
    private double _left = 0;
    private double _right = 1;
    private double _top = 0;

    public ImageRegion(double top, double right, double bottom, double left)
    {
        super();
        _top = top;
        _left = left;
        _right = right;
        _bottom = bottom;
    }

    public double getBottom()
    {
        return _bottom;
    }

    public double getLeft()
    {
        return _left;
    }

    public double getRight()
    {
        return _right;
    }

    public double getTop()
    {
        return _top;
    }

    @Override
    public String toString()
    {
        return _top + " " + _right + " " + _bottom + " " + _left; 
    }
    
    public static ImageRegion valueOf(String str) throws NumberFormatException
    {
        String[] values = StringUtils.split(str, ' ');
        if (values.length == 4)
        {
            double top = Double.parseDouble(values[0]);
            double right = Double.parseDouble(values[1]);
            double bottom = Double.parseDouble(values[2]);
            double left = Double.parseDouble(values[3]);
            
            return new ImageRegion(top, right, bottom, left);
        }
        throw new NumberFormatException("Input string does not have 4 values.");
    }
   
}
