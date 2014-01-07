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
        // ~6 schritte
        array[pos / 32] = Utils.setBit((pos % 32), array[pos / 32], bit);
    }

    // TODO TEST AENDERN
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
     * Anwendung der linearen Transformation auf ein Array (BitSlice Modus)
     * 
     * @param cv Variable auf die lin.Trans. angewendet wird
     */
    public static void linTransformBS(int[] cv) {
        cv[0] = (cv[0] << 13) | (cv[0] >>> 19); // Rotation um 13
        cv[2] = (cv[2] << 3) | (cv[2] >>> 29); // Rotation um 3
        cv[1] = cv[1] ^ cv[0] ^ cv[2];
        cv[3] = cv[3] ^ cv[2] ^ (cv[0] << 3);
        cv[1] = (cv[1] << 1) | (cv[1] >>> 31); // Rotation um 1
        cv[3] = (cv[3] << 7) | (cv[3] >>> 25); // Rotation um 7
        cv[0] = cv[0] ^ cv[1] ^ cv[3];
        cv[2] = cv[2] ^ cv[3] ^ (cv[1] << 7);
        cv[0] = (cv[0] << 5) | (cv[0] >>> 27); // Rotation um 5
        cv[2] = (cv[2] << 22) | (cv[2] >>> 10); // Rotation um 22
    }

    /**
     * Anwendung der inversen linearen Transformation auf ein Array (BitSlice
     * Modus)
     * 
     * @param cv Variable auf die inv. lin.Trans. angewendet wird
     */
    public static void invLinTransformBS(int[] cv) {
        cv[2] = (cv[2] << 10) | (cv[2] >>> 22);
        cv[0] = (cv[0] << 27) | (cv[0] >>> 5);
        cv[2] = cv[2] ^ cv[3] ^ (cv[1] << 7);
        cv[0] = cv[0] ^ cv[1] ^ cv[3];
        cv[3] = (cv[3] << 25) | (cv[3] >>> 7);
        cv[1] = (cv[1] << 31) | (cv[1] >>> 1);
        cv[3] = cv[3] ^ cv[2] ^ (cv[0] << 3);
        cv[1] = cv[1] ^ cv[0] ^ cv[2];
        cv[2] = (cv[2] << 29) | (cv[2] >>> 3);
        cv[0] = (cv[0] << 19) | (cv[0] >>> 13);
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
        // 610 schritte + 768 schritte = 1378
        for (int posOut = 0; posOut <= 127; posOut++) {
            tmp = 0;
            // XOR verknuepfung der verschiedenen Werte
            for (int posIn : SerpentTables.LTtable[posOut]) {
                tmp ^= getArrayBit(posIn, in);
            }
            // Verknuepften Werte in Array sichern
            setArrayBit(posOut, out, tmp); // 6 Schritte
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

    
    //TODO ZURUECK AENDERN
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

    /**
     * Verschluesselt ein 132Bit Wort (4 x 32Bit Array) im BitSlice Modus
     * 
     * @param p Plaintext, welcher verschluesselt wird
     * 
     * @param k 33 Rundenschluessel (33x4 array)
     * 
     * @return Chiffretext (4 x 32Bit Array)
     */
    public static int[] encryptBitSlice(int[] p, int[][] k) {

        int[] cv = new int[4]; // concatVector Sbox
        int[][] b = new int[33][4]; // 33 Bs gibt es (letzte Runde 2)
        b[0] = p.clone();

        // RUNDE 0 - 30
        for (int r = 0; r <= 30; r++) {
            b[r][0] ^= k[r][0];
            b[r][1] ^= k[r][1];
            b[r][2] ^= k[r][2];
            b[r][3] ^= k[r][3];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 32; j += 4) {
                    cv[i] = sBox(j, cv[i], Utils.get4Bits(j, b[r][i]), r);
                }
            }

            linTransformBS(cv);
            b[r + 1][0] = cv[0];
            b[r + 1][1] = cv[1];
            b[r + 1][2] = cv[2];
            b[r + 1][3] = cv[3];

            cv[0] = 0;
            cv[1] = 0;
            cv[2] = 0;
            cv[3] = 0;
        }

        // RUNDE 31
        b[31][0] ^= k[31][0];
        b[31][1] ^= k[31][1];
        b[31][2] ^= k[31][2];
        b[31][3] ^= k[31][3];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 32; j += 4) {
                cv[i] = sBox(j, cv[i], Utils.get4Bits(j, b[31][i]), 31);
            }
        }

        b[32][0] = cv[0] ^ k[32][0];
        b[32][1] = cv[1] ^ k[32][1];
        b[32][2] = cv[2] ^ k[32][2];
        b[32][3] = cv[3] ^ k[32][2];

        return b[32];
    }

    /**
     * Entschluesselt ein 132Bit Wort (4 x 32Bit Array) im BitSlice Modus
     * 
     * @param c Ciphertext, welcher entschluesselt wird
     * 
     * @param k 33 Rundenschluessel (33x4 array)
     * 
     * @return Plaintext (4 x 32Bit Array)
     */
    public static int[] decryptBitSlice(int[] c, int[][] k) {

        int[] cv = new int[4]; // concatVector Sbox
        int[][] b = new int[33][4]; // 33 Bs gibt es (letzte Runde 2)
        b[32] = c.clone();

        // RUNDE 31
        b[32][0] ^= k[32][0];
        b[32][1] ^= k[32][1];
        b[32][2] ^= k[32][2];
        b[32][3] ^= k[32][2];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 32; j += 4) {
                cv[i] = invSBox(j, cv[i], Utils.get4Bits(j, b[32][i]), 31);
            }
        }

        b[31][0] = cv[0] ^ k[31][0];
        b[31][1] = cv[1] ^ k[31][1];
        b[31][2] = cv[2] ^ k[31][2];
        b[31][3] = cv[3] ^ k[31][3];

        cv[0] = 0;
        cv[1] = 0;
        cv[2] = 0;
        cv[3] = 0;

        // RUNDE 30-0
        for (int r = 31; r > 0; r--) {
            invLinTransformBS(b[r]);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 32; j += 4) {
                    cv[i] = invSBox(j, cv[i], Utils.get4Bits(j, b[r][i]), r - 1);
                }
            }

            b[r - 1][0] = cv[0] ^ k[r - 1][0];
            b[r - 1][1] = cv[1] ^ k[r - 1][1];
            b[r - 1][2] = cv[2] ^ k[r - 1][2];
            b[r - 1][3] = cv[3] ^ k[r - 1][3];
            cv[0] = 0;
            cv[1] = 0;
            cv[2] = 0;
            cv[3] = 0;
        }

        return b[0];
    }

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

    // *********************TESTMETHODEN

    public static byte[] fromEvenLengthString(String hex) {
        int len = hex.length();
        byte[] buf = new byte[((len + 1) / 2)];

        int j = 0;
        if ((len % 2) == 1)
            throw new IllegalArgumentException("string must have an even number of digits");

        while (len > 0) {
            buf[j++] = (byte) (fromDigit(hex.charAt(--len)) | (fromDigit(hex.charAt(--len)) << 4));
        }
        return buf;
    }

    public static int fromDigit(char ch) {
        if (ch >= '0' && ch <= '9')
            return ch - '0';
        if (ch >= 'A' && ch <= 'F')
            return ch - 'A' + 10;
        if (ch >= 'a' && ch <= 'f')
            return ch - 'a' + 10;
        throw new IllegalArgumentException("Invalid hex digit '" + ch + "'");
    }

    public static void main(String[] args) {
        int t00, t01, t02, t03, t04, t05, t06, t07, t08, t09, t10;
        int t11, t12, t13, t14, t15, t16, t17, t18, t19;
        int y0, y1, y2, y3, z;

        // System.out.print("x0" + "\t" + "x1" + "\t" + "x2" + "\t" + "x3" +
        // "\t" + "t01" + "\t" + "t02" + "\t" + "t03" + "\t" + "y3" + "\t" +
        // "t05" + "\t"
        // + "t06" + "\t" + "t07" + "\t" + "t08" + "\t" + "t09" + "\t" + "y2" +
        // "\t" + "t11" + "\t" + "t12" + "\t" + "t13" + "\t" + "t14" + "\t" +
        // "t15"
        // + "\t" + "y0" + "\t" + "t17" + "\t" + "y1");
        // System.out.println();
        //
        // for (int x0 = 1; x0 >= 0; x0--) {
        // for (int x1 = 1; x1 >= 0; x1--) {
        // for (int x2 = 1; x2 >= 0; x2--) {
        // for (int x3 = 1; x3 >= 0; x3--) {
        // t01 = x1 ^ x2;
        // t02 = x0 | x3;
        // t03 = x0 ^ x1;
        // y3 = t02 ^ t01;
        // t05 = x2 | y3;
        // t06 = x0 ^ x3;
        // t07 = x1 | x2;
        // t08 = x3 & t05;
        // t09 = t03 & t07;
        // y2 = t09 ^ t08;
        // t11 = t09 & y2;
        // t12 = x2 ^ x3;
        // t13 = t07 ^ t11;
        // t14 = x1 & t06;
        // t15 = t06 ^ t13;
        // y0 = ~t15;
        // t17 = y0 ^ t14;
        // y1 = t12 ^ t17;
        // System.out.print(x0 + "\t" + x1 + "\t" + x2 + "\t" + x3 + "\t" + t01
        // + "\t" + t02 + "\t" + t03 + "\t" + y3 + "\t" + t05 + "\t" + t06
        // + "\t" + t07 + "\t" + t08 + "\t" + t09 + "\t" + y2 + "\t" + t11 +
        // "\t" + t12 + "\t" + t13 + "\t" + t14 + "\t" + t15 + "\t" + y0
        // + "\t" + t17 + "\t" + y1);
        // System.out.println();
        //
        // }
        // }
        // }
        // }

        // System.out.print("t01" + "\t" + "t02" + "\t" + "t03" + "\t" + "t04" +
        // "\t" + "t05" + "\t" + "t06" + "\t" + "t07" + "\t" + "t08" + "\t" +
        // "y2" + "\t"
        // + "t10" + "\t" + "t11" + "\t" + "t12" + "\t" + "t13" + "\t" + "y3" +
        // "\t" + "y1" + "\t" + "t16" + "\t" + "t17" + "\t" + "y0");
        // System.out.println();
        //
        // for (int x0 = 1; x0 >= 0; x0--) {
        // for (int x1 = 1; x1 >= 0; x1--) {
        // for (int x2 = 1; x2 >= 0; x2--) {
        // for (int x3 = 1; x3 >= 0; x3--) {
        // t01 = x0 | x3;
        // t02 = x2 ^ x3;
        // t03 = ~x1;
        // t03 += 2;
        //
        // t04 = x0 ^ x2;
        // t05 = x0 | t03;
        // t06 = x3 & t04;
        // t07 = t01 & t02;
        // t08 = x1 | t06;
        // y2 = t02 ^ t05;
        // t10 = t07 ^ t08;
        // t11 = t01 ^ t10;
        // t12 = y2 ^ t11;
        // t13 = x1 & x3;
        // y3 = ~t10;
        // y3 += 2;
        // y1 = t13 ^ t12;
        // t16 = t10 | y1;
        // t17 = t05 & t16;
        // y0 = x2 ^ t17;
        // System.out.print(t01 + "\t" + t02 + "\t" + t03 + "\t" + t04 + "\t" +
        // t05 + "\t" + t06 + "\t" + t07 + "\t" + t08 + "\t" + y2 + "\t"
        // + t10 + "\t" + t11 + "\t" + t12 + "\t" + t13 + "\t" + y3 + "\t" + y1
        // + "\t" + t16 + "\t" + t17 + "\t" + y0);
        // System.out.println();
        //
        // }
        // }
        // }
        // }

        // for (int x3 = 1; x3 >= 0; x3--) {
        // for (int x2 = 1; x2 >= 0; x2--) {
        // for (int x1 = 1; x1 >= 0; x1--) {
        // for (int x0 = 1; x0 >= 0; x0--) {
        // // int res = x1 + x1 * x0 + x2 * x0 + x2 * x1 * x0 + x3
        // // + x3 * x1 + x3 * x2 * x1;
        // int res = x0 + x1 + x2 + x3 + x3 * x0;
        // // int res = 1 + x0 + x2 * x0 + x2 * x1 + x2 * x1 * x0 +
        // // x3 * x1 + x3 * x2 * x0 + x3 * x2 * x1;
        // // int res = 1 + x0 + x1 * x0 + x2 + x2 * x0 + x2 * x1 +
        // // x2 * x1 * x0 + x3 + x3 * x2 * x0 + x3 * x2 * x1;
        // res %= 2;
        // System.out.println(res);
        // }
        // }
        // }
        // }

        // System.out.println();
        // for (byte[] i : SerpentTables.Sbox) {
        // for (int k = 15; k >= 0; k--) {
        // Utils.printBinary(i[k]);
        // System.out.println();
        // }
        // System.out.println();
        // }

        // int a0 = 1;
        // int a1 = 1;
        // int a2 = 0;
        // int a3 = 1;
        // int x0 = 0;
        // int x1 = 0;
        // int x2 = 0;
        // int x3 = 0;
        // System.out.println();
        // for (int i = 0; i < 32; i += 4) {
        // System.out.println("setBit: " + i + "  getBit: " + i / 4 + " ");
        // x0 = Utils.setBit(i + 0, x0, Utils.getBit(i / 4, a0));
        // System.out.println("setBit: " + (i + 1) + "  getBit: " + i / 4 +
        // " ");
        // x0 = Utils.setBit(i + 1, x0, Utils.getBit(i / 4, a1));
        // System.out.println("setBit: " + (i + 2) + "  getBit: " + i / 4 +
        // " ");
        // x0 = Utils.setBit(i + 2, x0, Utils.getBit(i / 4, a2));
        // System.out.println("setBit: " + (i + 3) + "  getBit: " + i / 4 +
        // " ");
        // x0 = Utils.setBit(i + 3, x0, Utils.getBit(i / 4, a3));
        // System.out.println();
        // }
        // System.out.println("------------");
        //
        // for (int i = 0; i < 32; i += 4) {
        // System.out.println("setBit: " + i + "  getBit: " + (i + 32) / 4 +
        // " ");
        // x1 = Utils.setBit(i + 0, x1, Utils.getBit((i + 32) / 4, a0));
        // System.out.println("setBit: " + (i + 1) + "  getBit: " + (i + 32) / 4
        // + " ");
        // x1 = Utils.setBit(i + 1, x1, Utils.getBit((i + 32) / 4, a1));
        // System.out.println("setBit: " + (i + 2) + "  getBit: " + (i + 32) / 4
        // + " ");
        // x1 = Utils.setBit(i + 2, x1, Utils.getBit((i + 32) / 4, a2));
        // System.out.println("setBit: " + (i + 3) + "  getBit: " + (i + 32) / 4
        // + " ");
        // x1 = Utils.setBit(i + 3, x1, Utils.getBit((i + 32) / 4, a3));
        // System.out.println();
        // }
        // System.out.println("------------");
        // for (int i = 0; i < 32; i += 4) {
        // System.out.println("setBit: " + i + "  getBit: " + (i + 64) / 4 +
        // " ");
        // x2 = Utils.setBit(i + 0, x2, Utils.getBit((i + 64) / 4, a0));
        // System.out.println("setBit: " + (i + 1) + "  getBit: " + (i + 64) / 4
        // + " ");
        // x2 = Utils.setBit(i + 1, x2, Utils.getBit((i + 64) / 4, a1));
        // System.out.println("setBit: " + (i + 2) + "  getBit: " + (i + 64) / 4
        // + " ");
        // x2 = Utils.setBit(i + 2, x2, Utils.getBit((i + 64) / 4, a2));
        // System.out.println("setBit: " + (i + 3) + "  getBit: " + (i + 64) / 4
        // + " ");
        // x2 = Utils.setBit(i + 3, x2, Utils.getBit((i + 64) / 4, a3));
        // System.out.println();
        // }
        // System.out.println("------------");
        // for (int i = 0; i < 32; i += 4) {
        // System.out.println("setBit: " + i + "  getBit: " + (i + 96) / 4 +
        // " ");
        // x3 = Utils.setBit(i + 0, x3, Utils.getBit((i + 96) / 4, a0));
        // System.out.println("setBit: " + (i + 1) + "  getBit: " + (i + 96) / 4
        // + " ");
        // x3 = Utils.setBit(i + 1, x3, Utils.getBit((i + 96) / 4, a1));
        // System.out.println("setBit: " + (i + 2) + "  getBit: " + (i + 96) / 4
        // + " ");
        // x3 = Utils.setBit(i + 2, x3, Utils.getBit((i + 96) / 4, a2));
        // System.out.println("setBit: " + (i + 3) + "  getBit: " + (i + 96) / 4
        // + " ");
        // x3 = Utils.setBit(i + 3, x3, Utils.getBit((i + 96) / 4, a3));
        // System.out.println();
        // }
        //
        // Utils.printBinary(a0);
        // System.out.println();
        // Utils.printBinary(a1);
        // System.out.println();
        // Utils.printBinary(a2);
        // System.out.println();
        // Utils.printBinary(a3);
        // System.out.println();
        // System.out.println();
        //
        // Utils.printBinary(x0);
        // System.out.println();
        // Utils.printBinary(x1);
        // System.out.println();
        // Utils.printBinary(x2);
        // System.out.println();
        // Utils.printBinary(x3);
        // System.out.println();

        int x0 = 5;
        int x1 = 1;
        int x2 = 0;
        int x3 = 1;

        t01 = x1 ^ x2;
        t02 = x0 | x3;
        t03 = x0 ^ x1;
        y3 = t02 ^ t01;

        // System.out.println();
        // Utils.printBinary(y3);
        // System.out.println();

        t05 = x2 | y3;
        t06 = x0 ^ x3;
        t07 = x1 | x2;
        t08 = x3 & t05;
        t09 = t03 & t07;
        y2 = t09 ^ t08;
        t11 = t09 & y2;
        t12 = x2 ^ x3;
        t13 = t07 ^ t11;
        t14 = x1 & t06;
        t15 = t06 ^ t13;
        y0 = ~t15;
        t17 = y0 ^ t14;
        y1 = t12 ^ t17;

        System.out.println(y0 + " " + y1 + " " + y2 + " " + y3);

        int[] cv = new int[4];
        int x[] = { 1, 1, 0, 1 };

         x[3] = 5;
         x[2] = 1;
         x[1] = 0;
         x[0] = 1;

        x = initialPermutation(x);
        System.out.println(x[0] + " " + x[1] + " " + x[2] + " " + x[3]);

        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j < 32; j += 4) {
                cv[i] = sBox(j, cv[i], Utils.get4Bits(j, x[i]), 0);
            }
            // System.out.println((i - 3) * -1);
        }
        cv = finalPermutation(cv);

        System.out.println(cv[3] + " " + cv[2] + " " + cv[1] + " " + cv[0]);

        // Utils.printBinary(y0);
        // Utils.printBinary(y1);
        // Utils.printBinary(y2);
        // Utils.printBinary(y3);
        System.out.println();
        int zahl = 48;
        zahl = Utils.get4BitsTEST(4, zahl);
        System.out.println(zahl);

        String hex = "00000003000000020000000100000000";
        byte[] b = fromEvenLengthString(hex);

        System.out.println();

        int offset = 0;

        // int[] value = { 52, 3323, 2, 424 };
        // int[][] keyBS =
        // getRoundKey(getPreKey(getSerpentK(Utils.getKey("test"))));

        // int[] enc = new int[4];

        long t1 = System.currentTimeMillis();
        // for (int i = 0; i < 3200000; i++) {
        // decrypt(encrypt(value, keyBS), keyBS);
        // encrypt(value, key);
        // decryptBitSlice(encryptBitSlice(value, keyBS), keyBS);
        // System.out.println(enc[0] + " " + enc[1] + " " + enc[2] + " " +
        // enc[3]);
        // }
        long t2 = System.currentTimeMillis();
        // System.out.println((t2 - t1) / 1000);
    }
}
