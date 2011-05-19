/**
 * 
 */
package info.photoorganizer.util;

public class WordInfo
{
    private int positionOfFirstCharacter = 0;
    private int positionOfLastCharacter = 0;
    
    private String word = null;
    
    public WordInfo(String word, int positionOfFirstCharacter, int positionOfLastCharacter)
    {
        super();
        this.word = word;
        this.positionOfFirstCharacter = positionOfFirstCharacter;
        this.positionOfLastCharacter = positionOfLastCharacter;
    }
    
    public int getPositionOfFirstCharacter()
    {
        return positionOfFirstCharacter;
    }
    
    public int getPositionOfLastCharacter()
    {
        return positionOfLastCharacter;
    }
    
    public String getWord()
    {
        return word;
    }

    @Override
    public String toString()
    {
        return word;
    }
}