package info.photoorganizer.util;

import java.util.UUID;

public class UUIDUtilities
{
    public static byte[] bytesFromUuid(UUID uuid)
    {
        byte[] res = new byte[16];

        long least = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++)
        {
            res[7 - i + 8] = (byte) (least >>> (i * 8));
        }
        long most = uuid.getMostSignificantBits();
        for (int i = 0; i < 8; i++)
        {
            res[7 - i] = (byte) (most >>> (i * 8));
        }

        return res;
    }

    public static String bytesToHexString(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
        {
            sb.append(Integer.toHexString((int) bytes[i] & 0xFF));
        }
        return sb.toString();
    }

    public static UUID generateUuid()
    {
        return UUID.randomUUID();
    }

    public static byte[] generateUuidBytes()
    {
        return bytesFromUuid(generateUuid());
    }

    public static String generateUuidString()
    {
        return bytesToHexString(generateUuidBytes());
    }
}
