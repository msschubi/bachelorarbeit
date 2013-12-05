/*
 * Serpent Implementierung mit Table-LookUp
 * 
 * @author Marcel Schubert
 * 
 * @version 03.12.2013
 */

public class Serpent {

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
    public static void copyArrayBit(int posIn, int posOut, int[] in, int[] out) {
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

        for (int i = 0; i < 128; i++) {
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

        for (int i = 0; i < 128; i++) {
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
     * Berechnung (da die Funktion rekursiv mit neg. Werten definiert wurde)
     */
    public static int getPKV(int i, int[] negativeW, int[] w) {
        return i < 0 ? negativeW[i + 8] : w[i];
    }

    /*
     * Die als PreKey bezeichneten Werte werden in dieser Methode berechnen
     * 
     * @param negativeW Die Anfangswerte fuer die rekursiv definierten PreKeys
     * 
     * @return 132 x 32 Bit Woerter im Array gespeichert
     */
    public static int[] getPreKey(int[] negativeW) {
        int[] w = new int[132];
        int phi = 0x9e3779b9;

        for (int i = 0; i < 132; i++) {
            w[i] = (getPKV(i - 8, negativeW, w) ^ getPKV(i - 5, negativeW, w) ^ getPKV(i - 3, negativeW, w) ^ getPKV(i - 1, negativeW, w) ^ phi ^ i);
            // Rotation um 11 (nach links)
            w[i] = (w[i] << 11) | (w[i] >>> 21);
        }
        return w;

    }

    /*
     * Hilfsmethode, die die benoetigte sBox fuer einen bestimmten Key berechnet
     * 
     * @param value Wert zwischen 0 und 32 (33 Keys gibt es)
     */
    public static byte calcKeySBox(int value) {
        return (byte) ((35 - value) % 8);
    }

    /*
     * Aus dem PreKey werden die Rundenschluessel berechnet
     * 
     * @param preKey PreKey zur Berechnung der Rundenschluessel (132 x 32 Bit)
     * als Array
     * 
     * @return 33 x 128 Bit SubKeys als doppeltes Array
     */
    public static int[][] getRoundKey(int[] preKey) {
        int[][] subKeys = new int[33][4];
        int concatValue = 0;
        byte bits;

        // ein RoundKey besteht aus 4 x 32 Bit PreKeys auf denen die sBoxen
        // angewendet wurde

        // alle k_i werden durchlaufen
        for (int k_i = 0; k_i < 132; k_i++) {
            concatValue = 0;
            bits = 0;

            // Schleife fuer ein k_i
            for (byte pos = 31; pos >= 3; pos -= 4) {
                bits = Utils.get4Bits(pos, preKey[k_i]);
                // sBox wird pro int-Wert 8x angewendet, also pro Key 32x
                concatValue = sBox(pos, concatValue, bits, calcKeySBox(k_i / 4));
            }
            subKeys[k_i / 4][k_i % 4] = concatValue;
        }
        return subKeys;
    }

    /*
     * Hilfsmethode zur anzeige der berechneten Keys
     * 
     * @param key String der in Serpentschluessel angezeigt werden soll
     */
    public static void showKeys(String key) {
        int counter = 0;
        for (int i[] : getRoundKey(getPreKey(getSerpentK(Utils.getKey(key))))) {
            System.out.println("--------------------------");
            System.out.println("key " + counter / 4 + ":");
            for (int a : i) {
                counter++;
                System.out.print(counter + "\t");
                System.out.println(a);
            }
        }
    }

    /*
     * Verschluesselt ein 132Bit Wort (4 x 32Bit Array)
     * 
     * @param p Plaintext, welcher verschluesselt wird
     * 
     * @return Chiffretext (4 x 32Bit Array)
     */
    public static int[] encrypt(int[] p, int[][] k) {
        int concatValue;
        byte bits;
        // initial Permutation auf Plaintext p anwenden
        int b_i[] = initialPermutation(p);

        // 31 Runden mit linearer Transformation
        for (int round = 0; round < 31; round++) {

            // nicht bitslice version benötigt IP
            k[round] = initialPermutation(k[round]);

            // XOR verknüpfung von B und K
            b_i[0] = b_i[0] ^ k[round][0];
            System.out.println("Schluessel: " + (round));
            b_i[1] = b_i[1] ^ k[round][1];
            b_i[2] = b_i[2] ^ k[round][2];
            b_i[3] = b_i[3] ^ k[round][3];

            // SBox (muss 8x pro Arrayeintrag; also 4x8 angewendet werden)
            for (int keyNr = 0; keyNr < 4; keyNr++) {
                // Anwendung der Sbox auf einen Arrayeintrag eines Keys
                concatValue = 0;
                bits = 0;
                for (byte pos = 31; pos >= 3; pos -= 4) {
                    bits = Utils.get4Bits(pos, b_i[keyNr]);
                    concatValue = sBox(pos, concatValue, bits, (byte) (round % 8));
                }
                b_i[keyNr] = concatValue;
            }

            // lineare Transformation
            int[] out = new int[4];
            linTransform(b_i, out);
            b_i = out;
        }// ende 31 runden

        // ******** Runde 31 anfang (32te Runde) ***********

        // nicht bitslice version benötigt IP
        k[31] = initialPermutation(k[31]);

        // XOR verknüpfung von B und K
        b_i[0] = b_i[0] ^ k[31][0];
        b_i[1] = b_i[1] ^ k[31][1];
        b_i[2] = b_i[2] ^ k[31][2];
        b_i[3] = b_i[3] ^ k[31][3];
        System.out.println("Schluessel: " + (31));

        for (int keyNr = 0; keyNr < 4; keyNr++) {
            // Anwendung der Sbox auf einen Arrayeintrag eines Keys
            concatValue = 0;
            bits = 0;
            for (byte pos = 31; pos >= 3; pos -= 4) {
                bits = Utils.get4Bits(pos, b_i[keyNr]);
                concatValue = sBox(pos, concatValue, bits, (byte) (7));
            }
            b_i[keyNr] = concatValue;
        }

        // nicht bitslice version benötigt IP
        k[31] = initialPermutation(k[32]);

        // XOR verknüpfung nach sBox
        b_i[0] = b_i[0] ^ k[32][0];
        b_i[1] = b_i[1] ^ k[32][1];
        b_i[2] = b_i[2] ^ k[32][2];
        b_i[3] = b_i[3] ^ k[32][3];
        System.out.println("Schluessel: " + (32));

        // ******** Runde 32 ende *************

        // Finale Permutation
        finalPermutation(b_i);

        return b_i;
    }

    public static void main(String[] args) {

        // showKeys("test");

        int[] value = { 1, 2, 3, 4 };
        int[][] key = getRoundKey(getPreKey(getSerpentK(Utils.getKey("test"))));

        int[] cipher = encrypt(value, key);

        System.out.println();
        for (int i : cipher) {
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
