import java.math.BigInteger;

/**
 * Serpent Implementierung mit Table-LookUp
 * 
 * @author Marcel Schubert
 * 
 * @version 03.12.2013
 */

public class Serpent {

    /**
     * Liest Array an bestimmter Position aus in Bezug auf maximale Laenge von
     * 32 Bit pro int
     * 
     * @param pos Position (0-127)
     * 
     * @param array Array aus dem ausgelesen wird
     * 
     * @return 1 oder 0
     */
    public static int getArrayBit(int pos, int[] array) {
        return Utils.getBit((pos % 32), array[pos / 32]);
    }

    /**
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

    /**
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

    /**
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

    /**
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

    /**
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

    /**
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

    /**
     * Wendet die sBox auf bestimme 4 Bits eines Wertes an und ersetzt leere
     * Bits einer anderen Variable mit diesen 4 Bits
     * 
     * @param pos Position (LSB) von der beginnend die sBox angewendet werden
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
    public static int sBox(int pos, int concatValue, int bits, int sBoxNr) {
        return (SerpentTables.Sbox[sBoxNr][bits] << (31 - pos - 3)) | concatValue;
    }

    /**
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
    public static int invSBox(int pos, int concatValue, int bits, int invSBoxNr) {
        return (SerpentTables.SboxInverse[invSBoxNr][bits] << (31 - pos - 3)) | concatValue;
    }

    // TODO testen
    /**
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

    // TODO testen
    /**
     * Hilfsmethode getPreKeyValue liefert die Werte fuer die XOR PreKey
     * Berechnung (da die Funktion rekursiv mit neg. Werten definiert wurde)
     */
    public static int getPKV(int i, int[] negativeW, int[] w) {
        return i < 0 ? negativeW[i + 8] : w[i];
    }

    // TODO testen
    /**
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

    // TODO testen
    /**
     * Hilfsmethode, die die benoetigte sBox fuer einen bestimmten Key berechnet
     * 
     * @param value Wert zwischen 0 und 32 (33 Keys gibt es)
     */
    public static byte calcKeySBox(int value) {
        return (byte) ((35 - value) % 8);
    }

    // TODO testen
    /**
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
        int bits;

        // ein RoundKey besteht aus 4 x 32 Bit PreKeys auf denen die sBoxen
        // angewendet wurde

        // alle k_i werden durchlaufen
        for (int k_i = 0; k_i < 132; k_i++) {
            concatValue = 0;
            bits = 0;

            // Schleife fuer ein k_i
            for (int pos = 31; pos >= 3; pos -= 4) {
                bits = Utils.get4Bits(pos, preKey[k_i]);
                // sBox wird pro int-Wert 8x angewendet, also pro Key 32x
                concatValue = sBox(pos, concatValue, bits, calcKeySBox(k_i / 4));
            }
            subKeys[k_i / 4][k_i % 4] = concatValue;
        }
        return subKeys;
    }

    // TODO testen
    /**
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

    // TODO testen
    /**
     * Verschluesselt ein 132Bit Wort (4 x 32Bit Array)
     * 
     * @param p Plaintext, welcher verschluesselt wird
     * 
     * @return Chiffretext (4 x 32Bit Array)
     */
    public static int[] encrypt(int[] p, int[][] k) {

        int[] cv = new int[4]; // concatVector Sbox
        int[] out = new int[4]; // Fuer linTrans
        int[][] b = new int[33][4]; // 33 Bs gibt es (letzte Runde 2)

        int[] b_0 = initialPermutation(p);

        // ******************RUNDE 0**************

        for (int m = 0; m <= 30; m++) {

            b[m][0] ^= k[0][0];
            b[m][1] ^= k[0][1];
            b[m][2] ^= k[0][2];
            b[m][3] ^= k[0][3];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 32; j += 4) {
                    cv[i] = sBox(j, cv[i], Utils.get4Bits(j, b[m][i]), 0);
                }
            }

            b[m][0] = cv[0];
            b[m][1] = cv[1];
            b[m][2] = cv[2];
            b[m][3] = cv[3];

            out = new int[4];
            cv = new int[4];
            linTransform(b[m], out);
            b[m + 1] = out;
        }

        Utils.printBinary(b[31]);

        // *****************Test Rueckgaengig machen*****
        for (int m = 0; m <= 30; m++) {

            b[m][0] ^= k[0][0];
            b[m][1] ^= k[0][1];
            b[m][2] ^= k[0][2];
            b[m][3] ^= k[0][3];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 32; j += 4) {
                    cv[i] = sBox(j, cv[i], Utils.get4Bits(j, b[m][i]), 0);
                }
            }

            b[m][0] = cv[0];
            b[m][1] = cv[1];
            b[m][2] = cv[2];
            b[m][3] = cv[3];

            out = new int[4];
            cv = new int[4];
            linTransform(b[m], out);
            b[m + 1] = out;
        }

        return null;
    }

    // TODO testen
    /**
     * Entschluesselt ein 132Bit Wort (4 x 32Bit Array)
     * 
     * @param c Ciphertext, welcher entschluesselt wird
     * 
     * @return Plaintext (4 x 32Bit Array)
     */
    public static int[] decrypt(int[] c, int[][] k) {
        return null;
    }

    public static void main(String[] args) {

        // showKeys("test");
        Utils.printLittleEndian();

        int t0 = Utils.setBit(0, 0, 1);
        t0 = Utils.setBit(1, t0, 1);
        int t1 = Utils.setBit(0, 0, 0);
        int t2 = Utils.setBit(0, 0, 0);
        int t3 = Utils.setBit(127, 0, 1);

        // int[] value = { t0, t1, t2, t3 };
        int[] value = { 5, 323, 2, 424 };
        // setArrayBit((byte)2, value, (byte)1);
        // setArrayBit((byte)14, value, (byte)1);
        // setArrayBit((byte)15, value, (byte)1);
        // setArrayBit((byte)55, value, (byte)1);

        // Utils.printBinary(value);

        int concatValue = 0;
        Utils.printBinary(concatValue);
        // concatValue = sBox(0, concatValue, 0, 0);
        // concatValue = sBox(4, concatValue, 1, 0);
        // concatValue = sBox(0, concatValue, 0, 0);
        // concatValue = sBox(0, concatValue, 0, 0);

        System.out.println();
        for (int i = 0; i < 31; i += 4) {
            concatValue = sBox(i, concatValue, i / 4, 8);
            // Utils.printBinary(concatValue);
            // System.out.println();
            // System.out.print(i+"    ");
            // System.out.println(i/4);
        }
        Utils.printBinary(concatValue);

        int concatValue2 = 0;

        for (int i = 0; i < 31; i += 4) {
            concatValue2 = invSBox(i, concatValue2, Utils.get4Bits(i, concatValue), 8);
            // Utils.printBinary(concatValue);
            // System.out.println();
            // System.out.print(i+"    ");
            // System.out.println(i/4);
        }
        System.out.println();
        Utils.printBinary(concatValue2);
        System.out.println();

        for (int i = 0; i < 31; i += 4) {
            System.out.print(Utils.get4Bits(i, concatValue2) + " ");
        }

        // int[] out = new int[4];
        // int[] decode = new int[4];
        // linTransform(value, out);
        //
        // invLinTransform(out, decode);
        //
        // System.out.println();
        // Utils.printBinary(out);
        // System.out.println();
        // Utils.printBinary(decode);
        //
        // System.out.println("\n");
        // for(int i:value) {
        // System.out.print(i+" ");
        // }
        // System.out.println();
        // for(int i:decode) {
        // System.out.print(i+" ");
        // }
        // setArrayBit((byte)3, value, (byte)0);
        // System.out.println();
        // Utils.printBinary(value);
        //
        // System.out.println();

        // int[] value = { 10, 23, 323, 312 };

        // Utils.printBinary(value);

        // int[] out = new int[4];
        // int[] invOut = new int[4];
        // int concatValue = 0;
        // // int value = 23482;
        // int[] ip = initialPermutation(value);

        // int[] fp = finalPermutation(ip);
        //
        // Utils.printLittleEndian();
        // Utils.printBinary(value);
        // System.out.println();
        // Utils.printBinary(ip);
        // System.out.println();
        // Utils.printBinary(fp);
        //
        // System.out.println();
        // for (int i : value) {
        // System.out.print(i + " ");
        // }
        // System.out.println();
        // for (int i : fp) {
        // System.out.print(i + " ");
        // }

        int[][] key = getRoundKey(getPreKey(getSerpentK(Utils.getKey("test"))));
        System.out.println();
        for (int[] i : key) {
            for (int j : i) {
                System.out.print(j + "\t");
            }
            System.out.println();
        }
        encrypt(value, key);

        //
        // for (int[] k : key) {
        // for (int l : k) {
        // System.out.print(l+"\t");
        // }
        // System.out.println();
        // }
        //
        //
        // encrypt(value, key);

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
