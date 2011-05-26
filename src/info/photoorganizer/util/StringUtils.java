package info.photoorganizer.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils
{
    static final byte[] HEX_CHAR_TABLE =
            { 
                (byte) '0', 
                (byte) '1', 
                (byte) '2', 
                (byte) '3', 
                (byte) '4', 
                (byte) '5', 
                (byte) '6', 
                (byte) '7', 
                (byte) '8',
                (byte) '9', 
                (byte) 'a', 
                (byte) 'b', 
                (byte) 'c', 
                (byte) 'd', 
                (byte) 'e', 
                (byte) 'f' 
            };
    
    public static String capitalize(String sentence)
    {
        StringBuilder sb = new StringBuilder(sentence.length());
        String[] words = sentence.split(" ");
        for (String word : words)
        {
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return sb.toString();
    }
    
    public static boolean equals(String a, String b)
    {
        if (a == null && b == null)
        {
            return true;
        }
        else if ((a == null && b != null) || (a != null && b == null))
        {
            return false;
        }
        else
        {
            return a.equals(b);
        }
    }
    
    /**
     * http://rgagnon.com/javadetails/java-0596.html
     */
    public static String getHexString(byte[] raw)
    {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;
        
        for (byte b : raw)
        {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        String res = "";
        try
        {
            res = new String(hex, "ASCII");
        }
        catch (UnsupportedEncodingException e)
        {
        } 
        return res;
    }
    
    public static String join(Collection<? extends Object> list, boolean ignoreEmpty)
    {
        return join(list.toArray(), null, ignoreEmpty, Character.MIN_VALUE);
    }
    
    public static String join(Collection<? extends Object> list, char glue, char quotationCharacter)
    {
        return join(list.toArray(), String.valueOf(glue), true, quotationCharacter);
    }
    
    public static String join(Collection<? extends Object> list, String glue, char quotationCharacter)
    {
        return join(list.toArray(), glue, true, quotationCharacter);
    }
    
    public static String join(Collection<? extends Object> list, String glue)
    {
        return join(list.toArray(), glue, true, Character.MIN_VALUE);
    }
    
    public static String join(Object[] list, boolean ignoreEmpty)
    {
        return join(list, null, ignoreEmpty, Character.MIN_VALUE);
    }
    
    public static String join(Object[] list, char glue, boolean ignoreEmpty)
    {
        return join(list, glue, ignoreEmpty, Character.MIN_VALUE);
    }
    public static String join(Object[] list, char glue)
    {
        return join(list, glue, true, Character.MIN_VALUE);
    }
    
    public static String join(Object[] list, String glue, boolean ignoreEmpty)
    {
        return join(list, glue, ignoreEmpty, Character.MIN_VALUE);
    }
    
    public static String join(Object[] list, String glue)
    {
        return join(list, glue, true, Character.MIN_VALUE);
    }
    
    public static String join(Object[] list, char glue, boolean ignoreEmpty, char quotationCharacter)
    {
        return join(list, String.valueOf(glue), ignoreEmpty, quotationCharacter);
    }
    
    public static String join(Object[] list, char glue, char quotationCharacter)
    {
        return join(list, String.valueOf(glue), true, quotationCharacter);
    }
    
    public static String join(Object[] list, String glue, boolean ignoreEmpty, char quotationCharacter)
    {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object o : list)
        {
            if (null != o)
            {
                String value = o.toString();
                if ((ignoreEmpty && value.length() > 0) || !ignoreEmpty)
                {
                    if (!first && glue != null)
                    {
                        sb.append(glue);
                    }
                    if (value.indexOf(glue) >= 0 && quotationCharacter > 0)
                    {
                        sb.append(quotationCharacter).append(value).append(quotationCharacter);
                    }
                    else
                    {
                        sb.append(value);
                    }
                    first = false;
                }
            }
        }
        return sb.toString();
    }
    
    public static String repeatChar(char c, int num)
    {
        StringBuilder sb = new StringBuilder(num);
        repeatChar(c, num, sb);
        return sb.toString();
    }
    
    public static void repeatChar(char c, int num, StringBuilder sb)
    {
        for (int i=0; i < num; i++, sb.append(c));
    }
    
    public static Boolean toBoolean(String value)
    {
        String inputLower = value.toLowerCase();
        if (inputLower.equals("yes") || inputLower.equals("y") || inputLower.equals("true") || inputLower.equals("t"))
        {
            return Boolean.TRUE;
        }
        else if (inputLower.equals("no") || inputLower.equals("n") || inputLower.equals("false") || inputLower.equals("f"))
        {
            return Boolean.FALSE;
        }
        return null;
    }

    public static Calendar toDate(String value)
    {
        DateFormat[] formats = new DateFormat[]
        {
                DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT),
                DateFormat.getDateInstance(DateFormat.SHORT),
                new SimpleDateFormat("yyyy-MM")
        };

        Calendar c = null;
        Date d = null;
        for (DateFormat formatter : formats)
        {
            try
            {
                d = formatter.parse(value);
                break;
            }
            catch (ParseException e)
            {
            }
        }
        if (null != d)
        {
            c = Calendar.getInstance();
            c.setTime(d);
        }
        else if (value.startsWith("+") && value.length() >= 2)
        {
            int field = Calendar.DAY_OF_MONTH;
            boolean fieldSpecified = true;
            switch (value.charAt(value.length()-1))
            {
            case 'h': field = Calendar.HOUR_OF_DAY; break;
            case 'd': field = Calendar.DAY_OF_MONTH; break;
            case 'w': field = Calendar.WEEK_OF_YEAR; break;
            case 'm': field = Calendar.MONTH; break;
            case 'y': field = Calendar.YEAR; break;
            default: fieldSpecified = false;
            }
            
            try
            {
                int number = Integer.parseInt(value.substring(1, value.length() - (fieldSpecified ? 1 : 0)));
                c = Calendar.getInstance();
                c.add(field, number);
            }
            catch (NumberFormatException e)
            {
            }
        }
        return c;
    }
    
    public static String[] split(String sentence, char separator)
    {
        StringTokenizer tokenizer = new StringTokenizer(sentence, String.valueOf(separator));
        String[] res = new String[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens())
        {
            res[i++] = tokenizer.nextToken();
        }
        return res;
    }
    
    public static List<WordInfo> split(String sentence, char wordSeparatorCharacter, char quotationCharacter, boolean includeQuotationCharacter)
    {
        List<WordInfo> res = new ArrayList<WordInfo>();
        boolean inQuote = false;
        boolean wordIsQuoted = false;
        int wordStart = 0;
        for (int i = 0; i < sentence.length(); i++)
        {
            char c = sentence.charAt(i);
            boolean addWord = false;
            if (c == quotationCharacter)
            {
                if (inQuote)
                {
                    addWord = true;
                    wordIsQuoted = true;
                }
                else
                {
                    if (!includeQuotationCharacter)
                    {
                        wordStart++;
                    }
                }
                inQuote = !inQuote;
            }
            else if (c == wordSeparatorCharacter)
            {
                if (!inQuote)
                {
                    addWord = true;
                }
            }
            if (addWord)
            {
                if (wordStart != i)
                {
                    int wordEnd = i;
                    if (includeQuotationCharacter)
                    {
                        if (wordIsQuoted)
                        {
                            wordEnd++;
                        }
                    }
                    else
                    {
                    }
                    res.add(new WordInfo(sentence.substring(wordStart, wordEnd), wordStart, wordEnd - 1));
                }
                wordStart = i + 1;
                wordIsQuoted = false;
            }
        }
        if (wordStart != sentence.length())
        {
            res.add(new WordInfo(sentence.substring(wordStart), wordStart, sentence.length() - 1));
        }
        return res;
    }
}
