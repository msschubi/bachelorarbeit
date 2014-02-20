public class Twofish {

    // Key Schedule
    private static int[] Me;
    private static int[] Mo;
    private static int[] S;

    // q0, q1 Variablen
    private static int a0, a1, a2, a3, a4, b0, b1, b2, b3, b4;

    public static byte q0(byte x) {
        a0 = x / 16;
        b0 = x % 16;
        a1 = a0 ^ b0;
        // Rotation um 1 von 4Bits
        b1 = (a0 ^ (((b0 << 1) & 0xF) | (b0 >> 3)) ^ (8 * a0)) % 16;
        a2 = TwofishTables.sBoxQ0[0][a1];
        b2 = TwofishTables.sBoxQ0[1][b1];
        a3 = a2 ^ b2;
        b3 = (a2 ^ (((b2 << 1) & 0xF) | (b2 >> 3)) ^ (8 * a2)) % 16;
        a4 = TwofishTables.sBoxQ0[2][a3];
        b4 = TwofishTables.sBoxQ0[3][b3];

        return (byte) (16 * b4 + a4);
    }

    public static byte q1(byte x) {
        a0 = x / 16;
        b0 = x % 16;
        a1 = a0 ^ b0;
        // Rotation um 1 von 4Bits
        b1 = (a0 ^ (((b0 << 1) & 0xF) | (b0 >> 3)) ^ (8 * a0)) % 16;
        a2 = TwofishTables.sBoxQ1[0][a1];
        b2 = TwofishTables.sBoxQ1[1][b1];
        a3 = a2 ^ b2;
        b3 = (a2 ^ (((b2 << 1) & 0xF) | (b2 >> 3)) ^ (8 * a2)) % 16;
        a4 = TwofishTables.sBoxQ1[2][a3];
        b4 = TwofishTables.sBoxQ1[3][b3];

        return (byte) (16 * b4 + a4);
    }

    public static int[] paddingKey(int[] M) {
        int[] padKey;
        if (M.length < 4) {
            padKey = new int[4];
        } else if (M.length > 4 && M.length < 6) {
            padKey = new int[6];
        } else {
            padKey = new int[8];
        }
        for (int x = 0; x < M.length; x++) {
            padKey[x] = M[x];
        }
        return padKey;
    }

    public static void calcBasisKey(int[] M) {
        int k = M.length / 2;
        Me = new int[k];
        Mo = new int[k];
        S = new int[k];

        // M_i berechnen
        int[] Mi = new int[2 * k];
        for (int x = 0; x < 2 * k; x++) {
            /*
             * ersten 8 Bit ausmaskieren und an richige Position shiften,
             * zweiten 8 Bits ausmaskieren und shiften, etc.
             */
            Mi[x] = (M[x] & -0x1000000) >>> 24;
            Mi[x] |= (M[x] & 0xff0000) >>> 8;
            Mi[x] |= (M[x] & 0xff00) << 8;
            Mi[x] |= (M[x] & 0xff) << 24;
        }

        // Me und Mo berechnen
        for (int x = 0; x < k; x++) {
            Me[x] = Mi[2 * x];
            Mo[x] = Mi[2 * x + 1];
        }

        // 8ter Byte Blöcke bilden aus dem eingegebenen Key M
        int m[][] = new int[k][8];
        for (int i = 0; i < k; i++) {
            m[i][0] = (M[2 * i] & -0x1000000) >>> 24;
            m[i][1] = (M[2 * i] & 0xff0000) >>> 16;
            m[i][2] = (M[2 * i] & 0xff00) >>> 8;
            m[i][3] = (M[2 * i] & 0xff);
            m[i][4] = (M[2 * i + 1] & -0x1000000) >>> 24;
            m[i][5] = (M[2 * i + 1] & 0xff0000) >>> 16;
            m[i][6] = (M[2 * i + 1] & 0xff00) >>> 8;
            m[i][7] = (M[2 * i + 1] & 0xff);
        }

        // Vektor S berechnen mit mult. von Matrix RS in GF(2^8)
        for (int x = 0; x < k; x++) {
            // (k-1)-x, da woerter in umgekehrter reihenfolge laut beschreibung
            // in S sind
            S[(k - 1) - x] = Utils.multGF(TwofishTables.RS[0][0], m[x][0], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[0][1], m[x][1], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[0][2], m[x][2], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[0][3], m[x][3], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[0][4], m[x][4], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[0][5], m[x][5], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[0][6], m[x][6], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[0][7], m[x][7], TwofishTables.IRRPOLYNOMTFKEY);

            S[(k - 1) - x] |= (Utils.multGF(TwofishTables.RS[1][0], m[x][0], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[1][1], m[x][1], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[1][2], m[x][2], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[1][3], m[x][3], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[1][4], m[x][4], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[1][5], m[x][5], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[1][6], m[x][6], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[1][7], m[x][7], TwofishTables.IRRPOLYNOMTFKEY)) << 8;

            S[(k - 1) - x] |= (Utils.multGF(TwofishTables.RS[2][0], m[x][0], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[2][1], m[x][1], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[2][2], m[x][2], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[2][3], m[x][3], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[2][4], m[x][4], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[2][5], m[x][5], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[2][6], m[x][6], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[2][7], m[x][7], TwofishTables.IRRPOLYNOMTFKEY)) << 16;

            S[(k - 1) - x] |= (Utils.multGF(TwofishTables.RS[3][0], m[x][0], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[3][1], m[x][1], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[3][2], m[x][2], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[3][3], m[x][3], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[3][4], m[x][4], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[3][5], m[x][5], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[3][6], m[x][6], TwofishTables.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TwofishTables.RS[3][7], m[x][7], TwofishTables.IRRPOLYNOMTFKEY)) << 24;
        }
    }

    public static void main(String[] args) {
        int p = 87;
        int q = 19;
        int M[] = { 1, 2, 3, 4, 1, 2, 3, 4 };
        calcBasisKey(paddingKey(M));

        b0 = 10;
        System.out.println((((b0 << 1) & 0xF) | (b0 >> 3)));
    }
}
