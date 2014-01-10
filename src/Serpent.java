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

    // TODO ZURUECK AENDERN
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
    public static int[] encryptBitSlice(int[] plaintext, int[][] k) {

        int[][] b = new int[33][4]; // 33 Bs gibt es (letzte Runde 2)
        b[0] = plaintext;

        // RUNDE 0 - 30
        for (int r = 0; r <= 30; r++) {
            b[r][0] ^= k[r][0];
            b[r][1] ^= k[r][1];
            b[r][2] ^= k[r][2];
            b[r][3] ^= k[r][3];

            SerpentBitSlice.sBoxBitSlice(r % 8, b[r]);
            linTransformBS(b[r]);

            b[r + 1][0] = b[r][0];
            b[r + 1][1] = b[r][1];
            b[r + 1][2] = b[r][2];
            b[r + 1][3] = b[r][3];
        }

        // RUNDE 31
        b[31][0] ^= k[31][0];
        b[31][1] ^= k[31][1];
        b[31][2] ^= k[31][2];
        b[31][3] ^= k[31][3];

        SerpentBitSlice.sBoxBitSlice(31 % 8, b[31]);

        b[32][0] = b[31][0] ^ k[32][0];
        b[32][1] = b[31][1] ^ k[32][1];
        b[32][2] = b[31][2] ^ k[32][2];
        b[32][3] = b[31][3] ^ k[32][3];

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
    public static int[] decryptBitSlice(int[] ciphertext, int[][] k) {

        int[][] c = new int[33][4]; // 33 Bs gibt es (letzte Runde 2)
        c[32] = ciphertext;

        c[31][0] = c[32][0] ^ k[32][0];
        c[31][1] = c[32][1] ^ k[32][1];
        c[31][2] = c[32][2] ^ k[32][2];
        c[31][3] = c[32][3] ^ k[32][3];

        SerpentBitSlice.sBoxInvBitSlice(31 % 8, c[31]);

        c[31][0] ^= k[31][0];
        c[31][1] ^= k[31][1];
        c[31][2] ^= k[31][2];
        c[31][3] ^= k[31][3];

        for (int r = 31; r > 0; r--) {
            invLinTransformBS(c[r]);
            SerpentBitSlice.sBoxInvBitSlice((r - 1) % 8, c[r]);
            c[r][0] ^= k[r - 1][0];
            c[r][1] ^= k[r - 1][1];
            c[r][2] ^= k[r - 1][2];
            c[r][3] ^= k[r - 1][3];

            c[r - 1][0] = c[r][0];
            c[r - 1][1] = c[r][1];
            c[r - 1][2] = c[r][2];
            c[r - 1][3] = c[r][3];

        }

        return c[0];
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
    public static int[] encrypt(int[] plaintext, int[][] k) {
        int[][] b = new int[33][4]; // 33 Bs gibt es (letzte Runde 2)
        int[] cv = new int[4]; // concatVector Sbox
        int[] temp = new int[4];
        int kn[] = new int[4];

        // Anpassung an andere Reihenfolge der Autoren (MSB und LSB vertauscht)
        b[0][0] = plaintext[3];
        b[0][1] = plaintext[2];
        b[0][2] = plaintext[1];
        b[0][3] = plaintext[0];

        b[0] = initialPermutation(b[0]);

        for (int r = 0; r <= 30; r++) {

            kn[0] = k[r][3];
            kn[1] = k[r][2];
            kn[2] = k[r][1];
            kn[3] = k[r][0];

            kn = initialPermutation(kn);

            b[r][0] ^= kn[0];
            b[r][1] ^= kn[1];
            b[r][2] ^= kn[2];
            b[r][3] ^= kn[3];

            cv[0] = 0;
            cv[1] = 0;
            cv[2] = 0;
            cv[3] = 0;

            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j < 32; j += 4) {
                    cv[i] = sBox(j, cv[i], Utils.get4Bits(j, b[r][i]), r);
                }
            }

            cv = finalPermutation(cv);

            // Format wechseln
            temp[0] = cv[3];
            temp[1] = cv[2];
            temp[2] = cv[1];
            temp[3] = cv[0];

            linTransformBS(temp);

            // Format zurueckwechseln
            cv[0] = temp[3];
            cv[1] = temp[2];
            cv[2] = temp[1];
            cv[3] = temp[0];

            cv = initialPermutation(cv);

            b[r + 1][0] = cv[0];
            b[r + 1][1] = cv[1];
            b[r + 1][2] = cv[2];
            b[r + 1][3] = cv[3];

        }

        kn[0] = k[31][3];
        kn[1] = k[31][2];
        kn[2] = k[31][1];
        kn[3] = k[31][0];

        kn = initialPermutation(kn);

        b[31][0] ^= kn[0];
        b[31][1] ^= kn[1];
        b[31][2] ^= kn[2];
        b[31][3] ^= kn[3];

        cv[0] = 0;
        cv[1] = 0;
        cv[2] = 0;
        cv[3] = 0;

        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j < 32; j += 4) {
                cv[i] = sBox(j, cv[i], Utils.get4Bits(j, b[31][i]), 31);
            }
        }

        kn[0] = k[32][3];
        kn[1] = k[32][2];
        kn[2] = k[32][1];
        kn[3] = k[32][0];

        kn = initialPermutation(kn);

        b[32][0] = cv[0] ^ kn[0];
        b[32][1] = cv[1] ^ kn[1];
        b[32][2] = cv[2] ^ kn[2];
        b[32][3] = cv[3] ^ kn[3];

        b[32] = finalPermutation(b[32]);

        // umdrehen für einheitliche ausgabe
        temp[0] = b[32][3];
        temp[1] = b[32][2];
        temp[2] = b[32][1];
        temp[3] = b[32][0];

        return temp;
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
        int[] cv2 = new int[4]; // concatVector Sbox
        int[][] b2 = new int[33][4]; // 33 Bs gibt es (letzte Runde 2)
        int[] temp2 = new int[4];
        int[] kn2 = new int[4];

        temp2[0] = c[3];
        temp2[1] = c[2];
        temp2[2] = c[1];
        temp2[3] = c[0];

        b2[32] = initialPermutation(temp2);

        kn2[0] = k[32][3];
        kn2[1] = k[32][2];
        kn2[2] = k[32][1];
        kn2[3] = k[32][0];

        kn2 = initialPermutation(kn2);

        b2[31][0] = b2[32][0] ^ kn2[0];
        b2[31][1] = b2[32][1] ^ kn2[1];
        b2[31][2] = b2[32][2] ^ kn2[2];
        b2[31][3] = b2[32][3] ^ kn2[3];

        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j < 32; j += 4) {
                cv2[i] = invSBox(j, cv2[i], Utils.get4Bits(j, b2[31][i]), 31);
            }
        }

        kn2[0] = k[31][3];
        kn2[1] = k[31][2];
        kn2[2] = k[31][1];
        kn2[3] = k[31][0];

        kn2 = initialPermutation(kn2);

        b2[31][0] = cv2[0] ^ kn2[0];
        b2[31][1] = cv2[1] ^ kn2[1];
        b2[31][2] = cv2[2] ^ kn2[2];
        b2[31][3] = cv2[3] ^ kn2[3];

        // **eisnchub
        for (int r = 31; r >= 1; r--) {

            b2[r - 1][0] = b2[r][0];
            b2[r - 1][1] = b2[r][1];
            b2[r - 1][2] = b2[r][2];
            b2[r - 1][3] = b2[r][3];

            b2[r - 1] = finalPermutation(b2[r - 1]);

            // Format wechseln
            temp2[0] = b2[r - 1][3];
            temp2[1] = b2[r - 1][2];
            temp2[2] = b2[r - 1][1];
            temp2[3] = b2[r - 1][0];

            invLinTransformBS(temp2);

            // Format zurueckwechseln
            b2[r - 1][0] = temp2[3];
            b2[r - 1][1] = temp2[2];
            b2[r - 1][2] = temp2[1];
            b2[r - 1][3] = temp2[0];

            b2[r - 1] = initialPermutation(b2[r - 1]);

            cv2[0] = 0;
            cv2[1] = 0;
            cv2[2] = 0;
            cv2[3] = 0;

            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j < 32; j += 4) {
                    cv2[i] = invSBox(j, cv2[i], Utils.get4Bits(j, b2[r - 1][i]), r - 1);
                }
            }

            kn2[0] = k[r - 1][3];
            kn2[1] = k[r - 1][2];
            kn2[2] = k[r - 1][1];
            kn2[3] = k[r - 1][0];

            kn2 = initialPermutation(kn2);

            b2[r - 1][0] = kn2[0] ^ cv2[0];
            b2[r - 1][1] = kn2[1] ^ cv2[1];
            b2[r - 1][2] = kn2[2] ^ cv2[2];
            b2[r - 1][3] = kn2[3] ^ cv2[3];
        }
        System.out.println();

        b2[0] = finalPermutation(b2[0]);

        temp2[0] = b2[0][3];
        temp2[1] = b2[0][2];
        temp2[2] = b2[0][1];
        temp2[3] = b2[0][0];

        return temp2;
    }

    public static void main(String[] args) {

        int[] value = { 52, 3323, 2, 424 };
        int[] value2 = value.clone();

        int[][] keyBS = getRoundKey(getPreKey(getSerpentK(Utils.getKey("test"))));
        int[][] keyBS2 = keyBS.clone();

        int[] enc = encrypt(value, keyBS);
        int[] dec = decrypt(enc, keyBS);

        // int[] enc2 = encrypt(value2, keyBS2);
        // int[] dec2 = decryptBitSlice(enc2, keyBS2);

//        for (int i : enc) {
//            System.out.print(i + " ");
//        }
//        System.out.println();
//
//        for (int i : dec) {
//            System.out.print(i + " ");
//        }
        // System.out.println();
        // for (int i : enc2) {
        // System.out.print(i + " ");
        // }

        long t1 = System.currentTimeMillis();
         for (int i = 0; i < 3200000; i++) {
         decrypt(encrypt(value, keyBS), keyBS);
//         encrypt(value, key);
//         decryptBitSlice(encryptBitSlice(value, keyBS), keyBS);
//         System.out.println(enc[0] + " " + enc[1] + " " + enc[2] + " " +
//         enc[3]);
         }
        long t2 = System.currentTimeMillis();
         System.out.println((t2 - t1) / 1000);
    }
}
