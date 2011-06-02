package info.photoorganizer.util;

import info.photoorganizer.metadata.Image;

import java.io.File;
import java.net.URI;

public class FileIdentifier
{
    
    public static double equalityProbability(File f, Image i, double exactMatchThreshold)
    {
        double points = 0;
        URI fileURL = f.toURI();
        URI imageURL = i.getURI();
        
        points += compareURLs(imageURL, fileURL);
        if (points < exactMatchThreshold)
        {
            if (i.isFile())
            {
                File imageFile = i.getFile();
                points += compareFilenames(imageFile, f);
                if (points < exactMatchThreshold)
                {
                    points += compareFolderName(imageFile, f);
                    if (points < exactMatchThreshold)
                    {
    //                    Metadata metadata = ImageMetadataReader.readMetadata(f);
                        
    //                    TagDefinition definition = database.getTagDefinition(DefaultTagDefinition.APERTURE.getId());
    //                    if (definition instanceof ValueTagDefinition)
    //                    {
    //                        ValueTagDefinition valueTagDefinition = (ValueTagDefinition) definition;
    //                        ValueTag<? extends Object, ValueTagDefinition> tag = ValueTag.createFromMetadata(valueTagDefinition, metadata, ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_FNUMBER);
    //                        System.out.println(valueTagDefinition.getName() + " = " + tag.getValue());
    //                    }
                        
    //                    ExifIFD0Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
    //                    for (Directory dir : metadata.getDirectories())
    //                    {
    //                        System.out.println("  Directory " + dir.getName());
    //                        for (Tag tag : dir.getTags())
    //                        {
    //                            System.out.println("    " + tag.getTagName() + " : " + dir.getObject(tag.getTagType()));
    //                        }
    //                    }
                        
                    }
                }
            }
        }
        return points;
    }
    
    private static double compareURLs(URI imageURI, URI fileURI)
    {
        return imageURI.equals(fileURI) ? 1.0 : 0;
    }
    
    private static double compareFilenames(File image, File file)
    {
        return image.getName().equalsIgnoreCase(file.getName()) ? 0.7 : 0.0;
    }
    
    private static double compareFolderName(File image, File file)
    {
        return image.getParentFile().getName().equalsIgnoreCase(file.getParentFile().getName()) ? 0.2 : 0.0;
    }

}
