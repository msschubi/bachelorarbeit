/**
 * Klasse für die Berechnung der Tabellen der differentiellen Kryptoanalyse
 * @author Marcel Schubert
 * @version 10.01.2013
 */
public class Analyse {
    static int[][] s0, s1, s2, s3, s4, s5, s6, s7;

    private static void diff(int x1, int x2) {
        Utils.printBinary(x1 ^ x2, 4);
    }

    public static void differentialAnalysis(boolean latex) {
        s0 = new int[16][16];
        s1 = new int[16][16];
        s2 = new int[16][16];
        s3 = new int[16][16];
        s4 = new int[16][16];
        s5 = new int[16][16];
        s6 = new int[16][16];
        s7 = new int[16][16];

        // System.out.println("I_x1\t\t\tI_x2\t\t\tI_Diff\t\t\tO_x1\t\t\tO_x2\t\t\tO_Diff");
        for (int s = 0; s < 8; s++) {
            for (int x1 = 0; x1 < 16; x1++) {
                for (int x2 = 0; x2 < 16; x2++) {

                    // calculation

                    switch (s) {
                        case 0:
                            s0[x1 ^ x2][SerpentTables.Sbox[s][x1] ^ SerpentTables.Sbox[s][x2]]++;
                            break;
                        case 1:
                            s1[x1 ^ x2][SerpentTables.Sbox[s][x1] ^ SerpentTables.Sbox[s][x2]]++;
                            break;
                        case 2:
                            s2[x1 ^ x2][SerpentTables.Sbox[s][x1] ^ SerpentTables.Sbox[s][x2]]++;
                            break;
                        case 3:
                            s3[x1 ^ x2][SerpentTables.Sbox[s][x1] ^ SerpentTables.Sbox[s][x2]]++;
                            break;
                        case 4:
                            s4[x1 ^ x2][SerpentTables.Sbox[s][x1] ^ SerpentTables.Sbox[s][x2]]++;
                            break;
                        case 5:
                            s5[x1 ^ x2][SerpentTables.Sbox[s][x1] ^ SerpentTables.Sbox[s][x2]]++;
                            break;
                        case 6:
                            s6[x1 ^ x2][SerpentTables.Sbox[s][x1] ^ SerpentTables.Sbox[s][x2]]++;
                            break;
                        case 7:
                            s7[x1 ^ x2][SerpentTables.Sbox[s][x1] ^ SerpentTables.Sbox[s][x2]]++;
                            break;
                    }

                    // // Input
                    // Utils.printBinary(x1, 4);
                    // System.out.print("\t");
                    // Utils.printBinary(x2, 4);
                    // System.out.print("\t");
                    // diff(x1, x2);
                    // System.out.print("\t");
                    //
                    // // Output berechnung
                    // Utils.printBinary(SerpentTables.Sbox[0][x1], 4);
                    // System.out.print("\t");
                    // Utils.printBinary(SerpentTables.Sbox[0][x2], 4);
                    // System.out.print("\t");
                    // diff(SerpentTables.Sbox[0][x1],
                    // SerpentTables.Sbox[0][x2]);
                    //
                    // System.out.println();
                }
            }
        }

        // Ausgabe der Tabellen
        // erste Reihe: Outputdifferenzen
        // erste Spalte: Inputdifferenzen
        // Tabelle: Anzahl der Kombinationen (pro Reihe und Spalte 16=2^4)

        for (int s = 0; s < 8; s++) {
            System.out.println("sBox " + s);
            System.out.println("-----------");
            System.out.print("in/out");
            for (int i = 0; i < 16; i++) {
                System.out.print("\t" + i);
            }
            System.out.println("\n");
            for (int i = 0; i < 16; i++) {
                if (latex)
                    System.out.print(i + "&");
                else
                    System.out.print(i + "\t");
                for (int o = 0; o < 16; o++) {

                    switch (s) {
                        case 0:
                            if (latex)
                                System.out.print(s0[i][o] + "&");
                            else
                                System.out.print(s0[i][o] + "\t");
                            break;
                        case 1:
                            if (latex)
                                System.out.print(s1[i][o] + "&");
                            else
                                System.out.print(s1[i][o] + "\t");
                            break;
                        case 2:
                            if (latex)
                                System.out.print(s2[i][o] + "&");
                            else
                                System.out.print(s2[i][o] + "\t");
                            break;
                        case 3:
                            if (latex)
                                System.out.print(s3[i][o] + "&");
                            else
                                System.out.print(s3[i][o] + "\t");
                            break;
                        case 4:
                            if (latex)
                                System.out.print(s4[i][o] + "&");
                            else
                                System.out.print(s4[i][o] + "\t");
                            break;
                        case 5:
                            if (latex)
                                System.out.print(s5[i][o] + "&");
                            else
                                System.out.print(s5[i][o] + "\t");
                            break;
                        case 6:
                            if (latex)
                                System.out.print(s6[i][o] + "&");
                            else
                                System.out.print(s6[i][o] + "\t");
                            break;
                        case 7:
                            if (latex)
                                System.out.print(s7[i][o] + "&");
                            else
                                System.out.print(s7[i][o] + "\t");
                            break;
                    }
                }
                if (latex)
                    System.out.println("\n \\hline");
                else
                    System.out.println();
            }
            System.out.println("\n");
        }
    }

    public static int NMatches(int sbox, int alpha, int beta, boolean verbose) {
        int count = 0;
        int alphaXor = 0, betaXor = 0;
        int sboxX;
        for (int x = 0; x < 16; x++) {
            sboxX = SerpentTables.Sbox[sbox][x];
            if (verbose) {
                System.out.print(print4Bits(x) + "\t");
                System.out.print(print4Bits(sboxX) + "\t");
            }
            for (int s = 0; s < 4; s++) {
                if (s == 0)
                    alphaXor = getBit(s, x) & getBit(s, alpha);
                else
                    alphaXor ^= getBit(s, x) & getBit(s, alpha);

            }
            for (int t = 0; t < 4; t++) {
                if (t == 0)
                    betaXor = getBit(t, sboxX) & getBit(t, beta);
                else
                    betaXor ^= getBit(t, sboxX) & getBit(t, beta);
            }
            if (verbose) {
                System.out.print(alphaXor + "\t");
                System.out.print(betaXor + "\t");
            }
            if (alphaXor == betaXor)
                count++;
            if (verbose) {
                if (alphaXor == betaXor)
                    System.out.println("j");
                else
                    System.out.println("n");
            }
        }
        return count;
    }

    public static void linearAnalysis() {
        int counter = 0;
        int gesamt = 0;
        int c;
        int kx, px, cx;
        // for(int k = 0; k<16; k++) {
        for (int p = 0; p < 16; p++) {
            c = SerpentTables.Sbox[2][p];

            // kx = (((k&8)>>>3)^((k&4)>>>2)^((k&2)>>>1^(k&1)));
            px = (((p & 8) >>> 3) ^ ((p & 4) >>> 2) ^ ((p & 2) >>> 1 ^ (p & 1)));
            cx = (((c & 8) >>> 3) ^ ((c & 4) >>> 2) ^ ((c & 2) >>> 1 ^ (c & 1)));
            if ((px == cx)) {
                counter++;
            }
            gesamt++;
            // System.out.println(kx +" "+px+" "+cx);
        }
        // }
        System.out.println(((double) counter / (double) gesamt));
    }

    public static String print4Bits(int value) {
        String s = "";
        for (int i = 3; i > -1; i--)
            s += getBit(i, value);
        return s;
    }

    public static int getBit(int pos, int value) {
        int offset = 1 << (pos); // little Endian
        if ((value & offset) != 0)
            return 1;
        else
            return 0;
    }

    public static boolean oneBitToOneBit(int alpha, int beta) {
        switch (alpha) {
            case 1:
                if (beta == 1 || beta == 2 || beta == 4 || beta == 8) {
                    return true;
                }
                break;
            case 2:
                if (beta == 1 || beta == 2 || beta == 4 || beta == 8) {
                    return true;
                }
                break;
            case 4:
                if (beta == 1 || beta == 2 || beta == 4 || beta == 8) {
                    return true;
                }
                break;
            case 8:
                if (beta == 1 || beta == 2 || beta == 4 || beta == 8) {
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    public static void main(String[] args) {

        // differentialAnalysis(true);
        // linearAnalysis();
        // System.out.println("SIn\tSOut\talpha\tbeta\talpha=?beta");
        // System.out.println(1000 + "\t" + 1111 + "\t" + NMatches(0, 8, 15,
        // true));
        // System.out.println(NMatches(0, 4, 15,false));
        for (int sbox = 0; sbox < 8; sbox++) {

            System.out.println("SBOX" + sbox);

            for (int beta = 0; beta < 16; beta++) {
                for (int alpha = 0; alpha < 16; alpha++) {
                    if (alpha == 15 || alpha == 7) {
                        System.out.println(alpha + "&" + beta + "&" + NMatches(sbox, alpha, beta, false) + "\\\\");
                        System.out.println("\\hline");
                    } else if (oneBitToOneBit(alpha, beta))
                        System.out.print("\\textcolor{red}{" + alpha + "}" + "&" + "\\textcolor{red}{" + beta + "}" + "&" + "\\textcolor{red}{"
                                + NMatches(sbox, alpha, beta, false) + "}" + "&");
                    else
                        System.out.print(alpha + "&" + beta + "&" + NMatches(sbox, alpha, beta, false) + "&");
                }
            }
            System.out.println("\n\n\n");
        }
    }
}
