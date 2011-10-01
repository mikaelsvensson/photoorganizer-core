package info.photoorganizer.util.transform;

import info.photoorganizer.util.Clonable;

import java.io.Serializable;

public interface TextTransformer extends Clonable<TextTransformer>
{
    String transform(String input);
}
