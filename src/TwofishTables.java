public class TwofishTables {
    /**
     * irreduzible Polynome in GF(2^8)
     */
    final static int IRRPOLYNOM = 283; // AES
    final static int IRRPOLYNOM2 = 361; // Twofish
    final static int[][] MDS = { { 1, 239, 91, 91 }, { 91, 239, 239, 1 }, { 239, 91, 1, 239 }, { 239, 1, 239, 91 } };

    public static void main(String[] args) {
        int y0 = 16;
        int y1 = 32;
        int y2 = 64;
        int y3 = 64;
        
        //MDS
        int z0 = Utils.multGF(y0, MDS[0][0]) ^ Utils.multGF(y0, MDS[0][1]) ^ Utils.multGF(y0, MDS[0][2]) ^ Utils.multGF(y0, MDS[0][3]);
        int z1 = Utils.multGF(y1, MDS[1][0]) ^ Utils.multGF(y1, MDS[1][1]) ^ Utils.multGF(y1, MDS[1][2]) ^ Utils.multGF(y1, MDS[1][3]);
        int z2 = Utils.multGF(y2, MDS[2][0]) ^ Utils.multGF(y2, MDS[2][1]) ^ Utils.multGF(y2, MDS[2][2]) ^ Utils.multGF(y2, MDS[2][3]);
        int z3 = Utils.multGF(y3, MDS[3][0]) ^ Utils.multGF(y3, MDS[3][1]) ^ Utils.multGF(y3, MDS[3][2]) ^ Utils.multGF(y3, MDS[3][3]);
        
        
        
        System.out.println(z0 + " " + z1 + " " + z2 + " " + z3);
        System.out.println((1077944336/(256*256*256)) % 256);

    }
}
