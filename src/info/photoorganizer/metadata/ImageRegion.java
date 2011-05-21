package info.photoorganizer.metadata;

public class ImageRegion
{
    private double _bottom = 1;
    private double _left = 0;
    private double _right = 1;
    private double _top = 0;

    public ImageRegion(double top, double left, double right, double bottom)
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
}
