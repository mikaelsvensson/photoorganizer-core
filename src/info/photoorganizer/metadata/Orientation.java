package info.photoorganizer.metadata;

import java.awt.geom.AffineTransform;

/**
 * Indicates how an image is <em>stored</em>. If the image is to be shown
 * correctly it must be transformed using a "reversing transformation", e.g. if
 * the image is <em>rotated to the right in the image file</em> it must be
 * <em>rotated to the left before shown</em> to the user
 * 
 * @see http://sylvana.net/jpegcrop/exif_orientation.html
 */
public enum Orientation
{
    FLIPPED_BOTH(3, true, true, Rotation.NONE),
    FLIPPED_HORIZONTALLY(4, true, false, Rotation.NONE),
    FLIPPED_VERTICALLY(2, false, true, Rotation.NONE),
    NORMAL(1, false, false, Rotation.NONE),
    ROTATED_LEFT(6, false, false, Rotation.LEFT),
    ROTATED_LEFT_AND_FLIPPED(5, false, true, Rotation.LEFT),
    ROTATED_RIGHT(8, false, false, Rotation.RIGHT),
    ROTATED_RIGHT_AND_FLIPPED(7, false, true, Rotation.RIGHT),
    ;
    
    private boolean flippedRightLeft = false;

    private boolean flippedUpDown = false;

    private Rotation rotation = Rotation.NONE;

    private int exifValue = 0;

    private Orientation(int exifValue, boolean flippedUpDown, boolean flippedRightLeft, Rotation rotation)
    {
        this.exifValue = exifValue;
        this.flippedUpDown = flippedUpDown;
        this.flippedRightLeft = flippedRightLeft;
        this.rotation = rotation;
    }
    public Rotation getRotation()
    {
        return rotation;
    }
    
    public boolean isFlippedRightLeft()
    {
        return flippedRightLeft;
    } 

    public boolean isFlippedUpDown()
    {
        return flippedUpDown;
    }
    
    public static Orientation fromExifValue(int exifOrientationValue)
    {
        for (Orientation o : values())
        {
            if (exifOrientationValue == o.exifValue)
            {
                return o;
            }
        }
        return null;
    }
}
