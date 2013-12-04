import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Serpent Implementierung mit Table-LookUp
 * 
 * @author Marcel Schubert
 * 
 * @version 03.12.2013
 */

public class TestSBoxes {

    /*
     * Liest Array an bestimmter Position aus in Bezug auf maximale Laenge von
     * 32 Bit pro int
     * 
     * @param pos Position (0-127)
     * 
     * @param array Array aus dem ausgelesen wird
     * 
     * @return 1 oder 0
     */
    public static byte getArrayBit(byte pos, int[] array) {
        return Utils.getBit((byte) (pos % 32), array[pos / 32]);
    }

    /*
     * Setzt Bit an bestimmter Position in Array in Bezug auf maximale Laenge
     * von 32 Bit pro int
     * 
     * @param pos Position (0-127)
     * 
     * @param array Array aus dem ausgelesen wird
     * 
     * @param bit 1 oder 0
     */
    public static void setArrayBit(byte pos, int[] array, byte bit) {
        array[pos / 32] = Utils.setBit((byte) (pos % 32), array[pos / 32], bit);
    }

    /*
     * Liest ein Bit aus einem Array der Groesse 4 und schreibt dieses Bit in
     * ein neues Array
     * 
     * @param posIn Position (0-127) des Bits, welches ausgelesen werden soll
     * 
     * @param posOut Position (0-127), an der das ausgelesene Bit geschrieben
     * werden soll
     * 
     * @param in Array aus dem ausgelesen werden soll
     * 
     * @param out Array in das das Bit geschrieben werden soll
     * 
     * @return Variable out mit dem geaendertem Bit an posOut
     */
    public static void copyArrayBit(byte posIn, byte posOut, int[] in, int[] out) {
        int posOutArr = posOut / 32;
        out[posOutArr] = Utils.setBit((byte) (posOut % 32), out[posOutArr], Utils.getBit((byte) (posIn % 32), in[posIn / 32]));
    }

    /*
     * Anwendung der linearen Transformation (bzw. IP(LT(FP(x))) ) auf einen
     * Array
     * 
     * @param in Array der Groesse 4 auf den die lin.Trans. angewendet werden
     * soll
     * 
     * @param out Array der Groesse 4 in welchem die lin.Trans. gespeichert wird
     */
    public static void linTransform(int[] in, int[] out) {
        byte tmp;
        for (byte posOut = 0; posOut <= 127 && posOut >= 0; posOut++) {
            tmp = 0;
            // XOR verknuepfung der verschiedenen Werte
            for (byte posIn : SerpentTables.LTtable[posOut]) {
                tmp ^= getArrayBit(posIn, in);
            }
            // Verknuepften Werte in Array sichern
            setArrayBit(posOut, out, tmp);
        }
    }

    /*
     * Anwendung der inversen linearen Transformation (bzw. IP(invLT(FP(x))) )
     * auf einen Array
     * 
     * @param in Array der Groesse 4 auf den die inv. lin.Trans. angewendet
     * werden soll
     * 
     * @param out Array der Groesse 4 in welchem die inv. lin.Trans. gespeichert
     * wird
     */
    public static void invLinTransform(int[] in, int[] out) {
        byte tmp;
        for (byte posOut = 0; posOut <= 127 && posOut >= 0; posOut++) {
            tmp = 0;
            // XOR verknuepfung der verschiedenen Werte
            for (byte posIn : SerpentTables.LTtableInverse[posOut]) {
                tmp ^= getArrayBit(posIn, in);
            }
            // Verknuepften Werte in Array sichern
            setArrayBit(posOut, out, tmp);
        }
    }

    /*
     * Wendet die initiale Permutation auf ein int-Array der Groeße 4 an
     * 
     * @param in Array der Größe 4
     * 
     * @return Initial permutiertes Array der Groeße 4
     */
    public static int[] initialPermutation(int[] in) {
        int[] out = new int[4];

        for (byte i = 0; i < 128; i++) {
            copyArrayBit(SerpentTables.IPtable[i], i, in, out);
        }
        return out;
    }

    /*
     * Wendet die finale Permutation auf ein int-Array der Groeße 4 an
     * 
     * @param in Array der Größe 4
     * 
     * @return Final permutiertes Array der Groeße 4
     */
    public static int[] finalPermutation(int[] in) {
        int[] out = new int[4];

        for (byte i = 0; i < 128; i++) {
            copyArrayBit(SerpentTables.FPtable[i], i, in, out);
        }
        return out;
    }

    /*
     * Wendet die sBox auf bestimme 4 Bits eines Wertes an und ersetzt leere
     * Bits einer anderen Variable mit diesen 4 Bits
     * 
     * @param pos Position (MSB) von der beginnend die sBox angewendet werden
     * soll
     * 
     * @param concatValue Wert, an denen die sBox-Bits eingefuegt werden (die
     * Stelle an der eingefuegt werden soll muss mit 0en besetzt sein!)
     * 
     * @param bits Einzufuegende Bits (auf die die sBox angewendet werden soll)
     * 
     * @param sBoxNr Nummer der sBox, die angewendet werden soll auf bits
     * 
     * @return Manipulierte Variable concatValue, in die an pos (MSB) der sBox
     * Wert gesetzt wurde
     */
    public static int sBox(byte pos, int concatValue, byte bits, byte sBoxNr) {
        return (SerpentTables.Sbox[sBoxNr][bits] << (pos - 3)) | concatValue;
    }

    /*
     * Wendet die invSBox auf bestimme 4 Bits eines Wertes an und ersetzt leere
     * Bits einer anderen Variable mit diesen 4 Bits
     * 
     * @param pos Position (MSB) von der beginnend die invSBox angewendet werden
     * soll
     * 
     * @param concatValue Wert, an denen die invSBox-Bits eingefuegt werden (die
     * Stelle an der eingefuegt werden soll muss mit 0en besetzt sein!)
     * 
     * @param bits Einzufuegende Bits (auf die die invSBox angewendet werden
     * soll)
     * 
     * @param sBoxNr Nummer der sBox, die angewendet werden soll auf bits
     * 
     * @return Manipulierte Variable concatValue, in die an pos (MSB) der
     * invSBox Wert gesetzt wurde
     */
    public static int invSBox(byte pos, int concatValue, byte bits, byte invSBoxNr) {
        return (SerpentTables.SboxInverse[invSBoxNr][bits] << (pos - 3)) | concatValue;
    }

    /*
     * Macht aus dem durch getKey erzeugten 32*8Bit Schluessel einen 8*32Bit
     * Schluessel
     * 
     * @param rawKey Array der Laenge 32
     * 
     * @return int-Array der Laenge 8 jeweils gefuellt mit 32 Bit
     */
    public static int[] getSerpentK(byte[] rawKey) {
        int[] serpentKey = new int[8];

        // jeweils 4 Bit aus einem Byte auslesen und in serpentKey schreiben
        // pro Schleifendurchlauf werden aus rawKey 4 Eintraege ausgelesen
        // und ein Eintrag in serpentKey belegt
        for (int i = 0; i < 8; i++) {
            serpentKey[i] = Utils.set4Bits((byte) 31, serpentKey[i], Utils.get4Bits((byte) 7, (int) rawKey[0 + i * 4]));
            serpentKey[i] = Utils.set4Bits((byte) 27, serpentKey[i], Utils.get4Bits((byte) 3, (int) rawKey[0 + i * 4]));

            serpentKey[i] = Utils.set4Bits((byte) 23, serpentKey[i], Utils.get4Bits((byte) 7, (int) rawKey[1 + i * 4]));
            serpentKey[i] = Utils.set4Bits((byte) 19, serpentKey[i], Utils.get4Bits((byte) 3, (int) rawKey[1 + i * 4]));

            serpentKey[i] = Utils.set4Bits((byte) 15, serpentKey[i], Utils.get4Bits((byte) 7, (int) rawKey[2 + i * 4]));
            serpentKey[i] = Utils.set4Bits((byte) 11, serpentKey[i], Utils.get4Bits((byte) 3, (int) rawKey[2 + i * 4]));

            serpentKey[i] = Utils.set4Bits((byte) 7, serpentKey[i], Utils.get4Bits((byte) 7, (int) rawKey[3 + i * 4]));
            serpentKey[i] = Utils.set4Bits((byte) 3, serpentKey[i], Utils.get4Bits((byte) 3, (int) rawKey[3 + i * 4]));
        }
        return serpentKey;
    }

    /*
     * Hilfsmethode getPreKeyValue liefert die Werte fuer die XOR PreKey
     * Berechnung (da die Funktion rekursiv mit neg. Werten berechnet wird
     */
    public static int getPKV(int i, int[] negativeW, int[] w) {
        return i < 0 ? negativeW[i+8] : w[i];
    }

    public static int[] getPreKey(int[] negativeW) {
        int[] w = new int[132];
        int phi = 0x9e3779b9;
        
        for(int i=0; i<132; i++) {
            w[i] = (getPKV(i-8, negativeW, w) ^ getPKV(i-5, negativeW, w) ^ 
                   getPKV(i-3, negativeW, w) ^ getPKV(i-1, negativeW, w) ^ 
                   phi ^ i);
            //Rotation um 11 (nach links)
            w[i] = (w[i] << 11) | (w[i]>>>21);
        }
        return w;
        
    }

    public static void main(String[] args) {
        
        for(int i:getPreKey(getSerpentK(Utils.getKey("test")))) {
            System.out.println(i);
        }
        
        // int[] key = new int[2];
        // key[1] = 200;
        // key[0] = 2;
        //
        // padKey(key);
        //
        // System.out.println(key[1]);
        // System.out.println(key[0]);
        // System.out.println(get4Bits((byte) 31, key[0]));

        // int[] in = new int[4];
        // int[] out = new int[4];
        // int[] out2 = new int[4];
        // in[0] = 0;
        // in[1] = 2000;
        // in[2] = 30;
        // in[3] = (1 << 31) - 1;
        // System.out.println(in[3]);
        //
        // linTransform(in, out);
        //
        // for (int i : out) {
        // System.out.print(i + " ");
        // }
        // System.out.println();
        //
        // invLinTransform(out, out2);
        //
        // for (int i : out2) {
        // System.out.print(i + " ");
        // }

    }
}
