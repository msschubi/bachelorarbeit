/*
 * Serpent Implementierung mit Table-LookUp
 * 
 * @author Marcel Schubert
 * 
 * @version 03.12.2013
 */

import java.math.BigInteger;

public class TestSBoxes {

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

    public static byte getArrayBit(byte pos, int[] array) {
        return getBit((byte) (pos % 32), array[pos / 32]);
    }

    public static void setArrayBit(byte pos, int[] array, byte bit) {
        array[pos / 32] = setBit((byte) (pos % 32), array[pos / 32], bit);
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
        out[posOutArr] = setBit((byte) (posOut % 32), out[posOutArr], getBit((byte) (posIn % 32), in[posIn / 32]));
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

    public static void main(String[] args) {

        // int i = 0xFFFFFFFF;
        // int i = 0;

        // int a = 1 << 31;
        // int b = 1 << 30;
        // int c = 1 << 29;
        // int d = 1 << 28;
        // int konkat = a | b | c | d;
        // System.out.println("konkat: " + konkat);
        // // testschleife sbox
        // int temp = 0;
        // for (int i = 3; i <= 31; i += 4) {
        // temp = sBox(i, temp, get4Bits(i, konkat), 0);
        // }
        // System.out.println(temp);
        //
        // int temp2 = 0;
        // for (int i = 3; i <= 31; i += 4) {
        // temp2 = invSBox(i, temp2, get4Bits(i, temp), 0);
        // }
        // System.out.println(temp2);

        // int temp = sBox(31,concatValue,get4Bits(31, konkat),0);

        // System.out.println(a);
        // System.out.println(b);
        // System.out.println(c);
        // System.out.println(d);
        //
        // System.out.println(i|a|b|c|d);
        // System.out.println(set4Bits(31, i, 15));

        // System.out.println(i);
        // System.out.println(set4Bits(3, i, 10));

        // int i = 10212;

        // -1 000 00000 00000 00000 00000 00000

        int[] in = new int[4];
        int[] out = new int[4];
        int[] out2 = new int[4];
        in[0] = 0;
        in[1] = 2000;
        in[2] = 30;
        in[3] = (1 << 31) - 1;
        System.out.println(in[3]);

        linTransform(in, out);

        for (int i : out) {
            System.out.print(i + " ");
        }
        System.out.println();

        invLinTransform(out, out2);

        for (int i : out2) {
            System.out.print(i + " ");
        }
        //
        // int [] out = initialPermutation(in);
        // for(int i:out) {
        // System.out.println(i);
        // }
        //
        //
        // System.out.println();
        // for(int a:finalPermutation(out)) {
        // System.out.println(a);
        // }

    }
}
