import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    /**
     * Hilfsmethode welche die Wertigkeit der Bits anzeigt
     */
    public static void printLittleEndian() {
        for (int i = 0; i < 128; i++) {
            System.out.printf("%-4d", i);
        }
        System.out.println();
        for (int i = 0; i < 4 * 128; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Hilfsmethode zum Ausgeben eines int-Wertes als Binaerzahl
     * 
     * @param decimalValue Dezimalzahl
     */
    public static void printBinary(int decimalValue) {
        for (int i = 0; i < 32; i++) {
            System.out.printf("%-4d", getBit(i, decimalValue));
        }
    }

    /**
     * Hilfsmethode zum Ausgeben eines int-Array-Wertes als Binaerzahl
     * 
     * @param decimalValue Dezimalzahl-Array
     */
    public static void printBinary(int[] decimalValue) {
        for (int k = 0; k < decimalValue.length; k++) {
            printBinary(decimalValue[k]);
        }
    }

    /**
     * Setzt/entfernt ein Bit an der gewuenschten Stelle einer Zahl in
     * littleEndian
     * 
     * @param pos Position die manipuliert werden soll in littleEndian
     * 
     * @param value Wert der manipuliert werden soll
     * 
     * @param bit 1 oder 0, je nachdem welches Bit gesetzt werden soll
     * 
     * @return manipulierter Wert
     */
    public static int setBit(int pos, int value, int bit) {
        int flag = 1 << (31 - pos); // little Endian
        return bit == 1 ? (value | flag) : (value & (~flag));
    }

    /**
     * Gibt zurueck ob Bit an abgefragter Stelle gesetzt ist in littleEndian
     * 
     * @param pos Position, welche ausgelesen werden soll in littleEndian
     * 
     * @param value Wert, an dem eine Position ausgelesen werden soll
     * 
     * @return 1 oder 0, je nachdem welches Bit gesetzt ist
     */
    public static int getBit(int pos, int value) {
        int offset = 1 << (31 - pos); // little Endian
        if ((value & offset) != 0)
            return 1;
        else
            return 0;
    }

    /**
     * Setzt 4 Bits an eine gewuenschte Stelle einer Zahl in littleEndian
     * 
     * @param pos Endposition der 4 Bits (Referenz ist MSB) in littleEndian
     * 
     * @param value Zahl welche mit 4 Bits manipuliert werden soll
     * 
     * @param bits Zahl 0-15 die in value eingefuegt wird
     * 
     * @return Manipulierte value Variable mit eingefuegten bits
     */
    public static int set4Bits(int pos, int value, int bits) {
        int pattern = -1 & ~(1 << (31 - pos)) & ~(1 << (31 - pos - 1)) & ~(1 << (31 - pos - 2)) & ~(1 << (31 - pos - 3));
        value &= pattern; // an die 4 Stellen werden 0en gesetzt
        // 0en werden hier mit den 4 Bits ueberschrieben
        value |= ((8 & bits) << (31 - pos - 3)) | ((4 & bits) << (31 - pos - 3)) | ((2 & bits) << (31 - pos - 3)) | ((1 & bits) << (31 - pos - 3));
        return value;
    }

    /**
     * Gibt 4 Bits zurueck, die beginnend einer Position (littleEndian) stehen
     * 
     * @param pos littleEndian, aber der beginnend 4 Bits Richtung MSB
     * zurueckgegeben werden soll
     * 
     * @param value Wert, aus dem Bits ausgelesen werden sollen
     * 
     * @return Wert zwischen 0-15
     */
    public static int get4Bits(int pos, int value) {
        int ones = 15 << (31 - pos - 3);
        return ((value & ones) >>> (31 - pos - 3));
    }

    /**
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
}
