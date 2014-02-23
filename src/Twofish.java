public class Twofish {

    // Key Schedule
    private static int[] Me;
    private static int[] Mo;
    private static int[] S;
    private static int k;

    private final static int p = 0x1010101; // 2^24+2^16+2^8+2^0
    private static int[] K = new int[40];

    // q0, q1 Variablen
    private static int a0, a1, a2, a3, a4, b0, b1, b2, b3, b4;

    // h Variablen
    private static int y0, y1, y2, y3;
    private static int[] z = { 0, 0, 0, 0 };

    public static void calcKey() {
        int A, B, temp;
        for (int i = 0; i < 20; i++) {
            A = h(2 * i * p, Me);
//            System.out.println(A);
            B = h((2 * i + 1) * p, Mo);
            B = (B << 8) | (B >>> 24);
            // Pseudo-Hadamard Transformation
            K[2 * i] = (int) ((A + B) % (0x100000000L)); // mod 2^32
            temp = (int) ((A + 2 * B) % (0x100000000L));
            K[2 * i + 1] = (temp << 9) | (temp >>> 23);
        }
//        for(int a:K) {
//            System.out.println(a);
//        }
    }

    // public static int[] inputWhitening ()
    public static int g(int X) {
        return h(X, S);
    }

    public static int h(int X, int[] L) {
        y0 = X & 0xFF;
        y1 = (X >>> 8) & 0xFF;
        y2 = (X >>> 16) & 0xFF;
        y3 = (X >>> 24) & 0xFF;

        // System.out.println(y0+" "+y1+" "+y2+" "+y3);

        if (k == 4) {
            y0 = TFT.Q[1][y0] ^ (L[3] & 0xFF);
            y1 = TFT.Q[0][y1] ^ ((L[3] >>> 8) & 0xFF);
            y2 = TFT.Q[0][y2] ^ ((L[3] >>> 16) & 0xFF);
            y3 = TFT.Q[1][y3] ^ ((L[3] >>> 24) & 0xFF);
        }
        if (k >= 3) {
            y0 = TFT.Q[1][y0] ^ (L[2] & 0xFF);
            y1 = TFT.Q[1][y1] ^ ((L[2] >>> 8) & 0xFF);
            y2 = TFT.Q[0][y2] ^ ((L[2] >>> 16) & 0xFF);
            y3 = TFT.Q[0][y3] ^ ((L[2] >>> 24) & 0xFF);
//            System.out.println(y0 + " " + y1 + " " + y2 + " " + y3);
        }
        // TODO ersetzen durch Table
        y0 = TFT.Q[1][TFT.Q[0][TFT.Q[0][y0] ^ (L[1] & 0xFF)] ^ (L[0] & 0xFF)];
        y1 = TFT.Q[0][TFT.Q[0][TFT.Q[1][y1] ^ ((L[1] >>> 8) & 0xFF)] ^ ((L[0] >>> 8) & 0xFF)];
        y2 = TFT.Q[1][TFT.Q[1][TFT.Q[0][y2] ^ ((L[1] >>> 16) & 0xFF)] ^ ((L[0] >>> 16) & 0xFF)];
        y3 = TFT.Q[0][TFT.Q[1][TFT.Q[1][y3] ^ ((L[1] >>> 24) & 0xFF)] ^ ((L[0] >>> 24) & 0xFF)];

        z[0] = Utils.multGF(TFT.MDS[0][0], y0, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[0][1], y1, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[0][2], y2, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[0][3], y3, TFT.IRRPOLYNOMTFMDS);
        z[1] = Utils.multGF(TFT.MDS[1][0], y0, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[1][1], y1, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[1][2], y2, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[1][3], y3, TFT.IRRPOLYNOMTFMDS);
        z[2] = Utils.multGF(TFT.MDS[2][0], y0, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[2][1], y1, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[2][2], y2, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[2][3], y3, TFT.IRRPOLYNOMTFMDS);
        z[3] = Utils.multGF(TFT.MDS[3][0], y0, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[3][1], y1, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[3][2], y2, TFT.IRRPOLYNOMTFMDS) ^
                Utils.multGF(TFT.MDS[3][3], y3, TFT.IRRPOLYNOMTFMDS);

        return (z[3] << 24) | (z[2] << 16) | (z[1] << 8) | z[0];
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

    // Funktoniert
    public static void calcBasisKey(int[] M) {
        k = M.length / 2;
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
            S[(k - 1) - x] = Utils.multGF(TFT.RS[0][0], m[x][0], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[0][1], m[x][1], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[0][2], m[x][2], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[0][3], m[x][3], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[0][4], m[x][4], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[0][5], m[x][5], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[0][6], m[x][6], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[0][7], m[x][7], TFT.IRRPOLYNOMTFKEY);

            S[(k - 1) - x] |= (Utils.multGF(TFT.RS[1][0], m[x][0], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[1][1], m[x][1], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[1][2], m[x][2], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[1][3], m[x][3], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[1][4], m[x][4], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[1][5], m[x][5], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[1][6], m[x][6], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[1][7], m[x][7], TFT.IRRPOLYNOMTFKEY)) << 8;

            S[(k - 1) - x] |= (Utils.multGF(TFT.RS[2][0], m[x][0], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[2][1], m[x][1], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[2][2], m[x][2], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[2][3], m[x][3], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[2][4], m[x][4], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[2][5], m[x][5], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[2][6], m[x][6], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[2][7], m[x][7], TFT.IRRPOLYNOMTFKEY)) << 16;

            S[(k - 1) - x] |= (Utils.multGF(TFT.RS[3][0], m[x][0], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[3][1], m[x][1], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[3][2], m[x][2], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[3][3], m[x][3], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[3][4], m[x][4], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[3][5], m[x][5], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[3][6], m[x][6], TFT.IRRPOLYNOMTFKEY)
                    ^ Utils.multGF(TFT.RS[3][7], m[x][7], TFT.IRRPOLYNOMTFKEY)) << 24;
        }
    }

    public static void main(String[] args) {
        int p = 87;
        int q = 19;
        int M[] = { 0, 0, 0, 0, 0, 0, 0, 0 };
        calcBasisKey(paddingKey(M));

        calcKey();

         for (int k : K) {
         System.out.println(k);
         }
    }
}
