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
    public static void setArrayBit(int pos, int[] array, int bit) {
        array[pos / 32] = Utils.setBit((pos % 32), array[pos / 32], bit);
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
        out[posOutArr] = Utils.setBit((posOut % 32), out[posOutArr], Utils.getBit((posIn % 32), in[posIn / 32]));
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
        int tmp;
        for (int posOut = 0; posOut <= 127; posOut++) {
            tmp = 0;
            // XOR verknuepfung der verschiedenen Werte
            for (int posIn : SerpentTables.LTtable[posOut]) {
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
        int tmp;
        for (int posOut = 0; posOut <= 127; posOut++) {
            tmp = 0;
            // XOR verknuepfung der verschiedenen Werte
            for (int posIn : SerpentTables.LTtableInverse[posOut]) {
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
    public static int[] getSerpentK(int[] rawKey) {
        int[] serpentKey = new int[8];

        // jeweils 4 Bit aus einem Byte auslesen und in serpentKey schreiben
        // pro Schleifendurchlauf werden aus rawKey 4 Eintraege ausgelesen
        // und ein Eintrag in serpentKey belegt
        for (int i = 0; i < 8; i++) {
            serpentKey[i] = Utils.set4Bits(31, serpentKey[i], Utils.get4Bits(7, rawKey[0 + i * 4]));
            serpentKey[i] = Utils.set4Bits(27, serpentKey[i], Utils.get4Bits(3, rawKey[0 + i * 4]));

            serpentKey[i] = Utils.set4Bits(23, serpentKey[i], Utils.get4Bits(7, rawKey[1 + i * 4]));
            serpentKey[i] = Utils.set4Bits(19, serpentKey[i], Utils.get4Bits(3, rawKey[1 + i * 4]));

            serpentKey[i] = Utils.set4Bits(15, serpentKey[i], Utils.get4Bits(7, rawKey[2 + i * 4]));
            serpentKey[i] = Utils.set4Bits(11, serpentKey[i], Utils.get4Bits(3, rawKey[2 + i * 4]));

            serpentKey[i] = Utils.set4Bits(7, serpentKey[i], Utils.get4Bits(7, rawKey[3 + i * 4]));
            serpentKey[i] = Utils.set4Bits(3, serpentKey[i], Utils.get4Bits(3, rawKey[3 + i * 4]));
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
     * @param k 33 Rundenschluessel (33x4 array)
     * 
     * @return Chiffretext (4 x 32Bit Array)
     */
    public static int[] encrypt(int[] p, int[][] k) {

        int[] cv = new int[4]; // concatVector Sbox
        int[] out = new int[4]; // Fuer linTrans
        int[][] b = new int[33][4]; // 33 Bs gibt es (letzte Runde 2)

        b[0] = initialPermutation(p);

        // Round 0 - Round 30
        // B0 bis B31
        for (int r = 0; r <= 30; r++) {

            // Auf 0 setzen, damit sBox richtig arbeitet
            cv[0] = 0;
            cv[1] = 0;
            cv[2] = 0;
            cv[3] = 0;

            b[r][0] ^= k[r][0];
            b[r][1] ^= k[r][1];
            b[r][2] ^= k[r][2];
            b[r][3] ^= k[r][3];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 32; j += 4) {
                    cv[i] = sBox(j, cv[i], Utils.get4Bits(j, b[r][i]), r);
                }
            }

            // damit linTransform richtig arbeitet
            out[0] = 0;
            out[1] = 0;
            out[2] = 0;
            out[3] = 0;

            linTransform(cv, out);
            b[r + 1] = out;

        }

        // s.o.
        cv[0] = 0;
        cv[1] = 0;
        cv[2] = 0;
        cv[3] = 0;

        b[31][0] ^= k[31][0];
        b[31][1] ^= k[31][1];
        b[31][2] ^= k[31][2];
        b[31][3] ^= k[31][3];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 32; j += 4) {
                cv[i] = sBox(j, cv[i], Utils.get4Bits(j, b[31][i]), 7);
            }
        }
        b[32][0] = k[32][0] ^ cv[0];
        b[32][1] = k[32][1] ^ cv[1];
        b[32][2] = k[32][2] ^ cv[2];
        b[32][3] = k[32][3] ^ cv[3];

        return finalPermutation(b[32]);
    }

    // TODO testen
    /**
     * Entschluesselt ein 132Bit Wort (4 x 32Bit Array)
     * 
     * @param c Ciphertext, welcher entschluesselt wird
     * 
     * @param k 33 Rundenschluessel (33x4 array)
     * 
     * @return Plaintext (4 x 32Bit Array)
     */
    public static int[] decrypt(int[] c, int[][] k) {
        int[] cv = new int[4]; // concatVector Sbox
        int[] out = new int[4]; // Fuer linTrans
        int[][] b = new int[33][4]; // 33 Bs gibt es (letzte Runde 2)

        b[32] = initialPermutation(c);

        // Auf 0 setzen, damit sBox richtig arbeitet
        cv[0] = 0;
        cv[1] = 0;
        cv[2] = 0;
        cv[3] = 0;

        b[32][0] ^= k[32][0];
        b[32][1] ^= k[32][1];
        b[32][2] ^= k[32][2];
        b[32][3] ^= k[32][3];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 32; j += 4) {
                cv[i] = invSBox(j, cv[i], Utils.get4Bits(j, b[32][i]), 7);
            }
        }

        b[31][0] = k[31][0] ^ cv[0];
        b[31][1] = k[31][1] ^ cv[1];
        b[31][2] = k[31][2] ^ cv[2];
        b[31][3] = k[31][3] ^ cv[3];

        // B31 bis B0
        for (int r = 31; r >= 1; r--) {

            // damit linTransform richtig arbeitet
            out[0] = 0;
            out[1] = 0;
            out[2] = 0;
            out[3] = 0;

            // Auf 0 setzen, damit sBox richtig arbeitet
            cv[0] = 0;
            cv[1] = 0;
            cv[2] = 0;
            cv[3] = 0;

            invLinTransform(b[r], out);

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 32; j += 4) {
                    cv[i] = invSBox(j, cv[i], Utils.get4Bits(j, out[i]), r - 1);
                }
            }

            b[r - 1][0] = k[r - 1][0] ^ cv[0];
            b[r - 1][1] = k[r - 1][1] ^ cv[1];
            b[r - 1][2] = k[r - 1][2] ^ cv[2];
            b[r - 1][3] = k[r - 1][3] ^ cv[3];

        }

        return finalPermutation(b[0]);
    }

    public static void main(String[] args) {

        int[] value = { 52, 3323, 2, 424 };
        int[][] key = getRoundKey(getPreKey(getSerpentK(Utils.getKey("test"))));

        int[] enc = new int[4];
        enc = encrypt(value, key);
        System.out.println(enc[0] + " " + enc[1] + " " + enc[2] + " " + enc[3]);

        int[] dec = new int[4];
        dec = decrypt(enc, key);
        System.out.println(dec[0] + " " + dec[1] + " " + dec[2] + " " + dec[3]);

        System.out.println();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 64000; i++) {
            // decrypt(encrypt(value, key), key);
            encrypt(value, key);
        }
        long t2 = System.currentTimeMillis();
        System.out.println((t2 - t1) / 1000);

        long t3 = System.currentTimeMillis();
        int[] a = new int[4];
        for (int i = 0; i < 3840000; i++) {
            // a[0] = 0;
            // a[1] = 0;
            // a[2] = 0;
            // a[3] = 0;
            a = new int[4];
        }

        long t4 = System.currentTimeMillis();
        System.out.println((t4 - t3));

    }
}
