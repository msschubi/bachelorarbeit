import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    /*
     * Setzt/entfernt ein Bit an der gewuenschten Stelle einer Zahl
     * 
     * @param pos Position die manipuliert werden soll
     * 
     * @param value Wert der manipuliert werden soll
     * 
     * @param bit 1 oder 0, je nachdem welches Bit gesetzt werden soll
     * 
     * @return manipulierter Wert
     */
    public static int setBit(byte pos, int value, byte bit) {
        int flag = 1 << pos;
        return bit == 1 ? (value | flag) : (value & (~flag));
    }

    /*
     * Gibt zurueck ob Bit an abgefragter Stelle gesetzt ist
     * 
     * @param pos Position, welche ausgelesen werden soll
     * 
     * @param value Wert, an dem eine Position ausgelesen werden soll
     * 
     * @return 1 oder 0, je nachdem welches Bit gesetzt ist
     */
    public static byte getBit(byte pos, int value) {
        int offset = 1 << pos;
        if ((value & offset) != 0)
            return 1;
        else
            return 0;
    }

    /*
     * Setzt 4 Bits an eine gewuenschte Stelle einer Zahl
     * 
     * @param pos Endposition der 4 Bits (Referenz ist MSB)
     * 
     * @param value Zahl welche mit 4 Bits manipuliert werden soll
     * 
     * @param bits Zahl 0-15 die in value eingefuegt wird
     * 
     * @return Manipulierte value Variable mit eingefuegten bits
     */
    public static int set4Bits(byte pos, int value, byte bits) {
        int pattern = -1 & ~(1 << pos) & ~(1 << (pos - 1)) & ~(1 << (pos - 2)) & ~(1 << (pos - 3));
        value &= pattern; // an die 4 Stellen werden 0en gesetzt
        // 0en werden hier mit den 4 Bits ueberschrieben
        value |= ((8 & bits) << pos - 3) | ((4 & bits) << (pos - 3)) | ((2 & bits) << (pos - 3)) | ((1 & bits) << (pos - 3));
        return value;
    }

    /*
     * Gibt 4 Bits zurueck, die beginnend einer Position (MSB) stehen
     * 
     * @param pos MSB, aber der beginnend 4 Bits Richtung LSB zurueckgegeben
     * werden soll
     * 
     * @param value Wert, aus dem Bits ausgelesen werden sollen
     * 
     * @return Wert zwischen 0-15
     */
    public static byte get4Bits(byte pos, int value) {
        int ones = 15 << pos - 3;
        return (byte) ((value & ones) >>> pos - 3);
    }

    /*
     * Keystring wird zu 256 Bit gehasht
     * 
     * @param key String der Laenge >=2
     * 
     * @return 32 * 8Bit Woerter in Bytearray der Laenge 32
     */
    public static byte[] getKey(String key) {

        assert key.length() > 1 : "Key muss länger als 1 sein";

        String part1 = key.substring(0, (key.length() / 2));
        String part2 = key.substring((key.length() / 2), key.length());

        // Salting (unnötig denke ich hier)
        // TODO Salting nachrechnen

        part1 += key.length();
        part2 += key.length();

        byte[] hash1, hash2;
        byte[] hash = new byte[32];

        try {

            hash1 = MessageDigest.getInstance("md5").digest(part1.getBytes());
            for (int i = 0; i < hash1.length; i++) {
                hash[i] = hash1[i];
            }

            hash2 = MessageDigest.getInstance("md5").digest(part2.getBytes());
            for (int i = 0; i < hash2.length; i++) {
                hash[i + 16] = hash1[i];
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    /*
     * Macht aus dem durch getKey erzeugten 32*8Bit Schluessel einen 8*32Bit
     * Schluessel
     * 
     * @param rawKey Array der Laenge 32
     * 
     * @return Array der Laenge 8 mit jeweils ints gefuellt mit 32 Bit
     */
    public static int[] getSerpentKey(byte[] rawKey) {
        int[] serpentKey = new int[8];

        serpentKey[0] = (rawKey[0]) << 24;
        serpentKey[0] |= ((byte) -52) << 16;
        // serpentKey[0] |= (rawKey[2]) << 8;
        // serpentKey[0] |= (rawKey[3]);

        return serpentKey;
    }

    public static void main(String[] args) {
        for (byte b : getKey("test")) {
            System.out.println(b);
        }
        System.out.println();
        int[] test = getSerpentKey(getKey("test"));
        System.out.println(test[0]);

    }
}
