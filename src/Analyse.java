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

    public static void main(String[] args) {
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
                System.out.print(i + "\t");
                for (int o = 0; o < 16; o++) {

                    switch (s) {
                        case 0:
                            System.out.print(s0[i][o] + "\t");
                            break;
                        case 1:
                            System.out.print(s1[i][o] + "\t");
                            break;
                        case 2:
                            System.out.print(s2[i][o] + "\t");
                            break;
                        case 3:
                            System.out.print(s3[i][o] + "\t");
                            break;
                        case 4:
                            System.out.print(s4[i][o] + "\t");
                            break;
                        case 5:
                            System.out.print(s5[i][o] + "\t");
                            break;
                        case 6:
                            System.out.print(s6[i][o] + "\t");
                            break;
                        case 7:
                            System.out.print(s7[i][o] + "\t");
                            break;
                    }
                }
                System.out.println();
            }
            System.out.println("\n");
        }

    }
}
