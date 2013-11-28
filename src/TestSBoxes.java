import java.math.BigInteger;

public class TestSBoxes {

    static final byte[][] Sbox = new byte[][] { { 3, 8, 15, 1, 10, 6, 5, 11, 14, 13, 4, 2, 7, 0, 9, 12 },/* S0: */
    { 15, 12, 2, 7, 9, 0, 5, 10, 1, 11, 14, 8, 6, 13, 3, 4 },/* S1: */
    { 8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2 },/* S2: */
    { 0, 15, 11, 8, 12, 9, 6, 3, 13, 1, 2, 4, 10, 7, 5, 14 },/* S3: */
    { 1, 15, 8, 3, 12, 0, 11, 6, 2, 5, 4, 10, 9, 14, 7, 13 },/* S4: */
    { 15, 5, 2, 11, 4, 10, 9, 12, 0, 3, 14, 8, 13, 6, 7, 1 },/* S5: */
    { 7, 2, 12, 5, 8, 4, 6, 11, 14, 9, 1, 15, 13, 3, 10, 0 },/* S6: */
    { 1, 13, 15, 0, 14, 8, 2, 11, 7, 4, 12, 10, 9, 3, 5, 6 },/* S7: */
    { 3, 8, 15, 1, 10, 6, 5, 11, 14, 13, 4, 2, 7, 0, 9, 12 },/* S0: */
    { 15, 12, 2, 7, 9, 0, 5, 10, 1, 11, 14, 8, 6, 13, 3, 4 },/* S1: */
    { 8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2 },/* S2: */
    { 0, 15, 11, 8, 12, 9, 6, 3, 13, 1, 2, 4, 10, 7, 5, 14 },/* S3: */
    { 1, 15, 8, 3, 12, 0, 11, 6, 2, 5, 4, 10, 9, 14, 7, 13 },/* S4: */
    { 15, 5, 2, 11, 4, 10, 9, 12, 0, 3, 14, 8, 13, 6, 7, 1 },/* S5: */
    { 7, 2, 12, 5, 8, 4, 6, 11, 14, 9, 1, 15, 13, 3, 10, 0 },/* S6: */
    { 1, 13, 15, 0, 14, 8, 2, 11, 7, 4, 12, 10, 9, 3, 5, 6 },/* S7: */
    { 3, 8, 15, 1, 10, 6, 5, 11, 14, 13, 4, 2, 7, 0, 9, 12 },/* S0: */
    { 15, 12, 2, 7, 9, 0, 5, 10, 1, 11, 14, 8, 6, 13, 3, 4 },/* S1: */
    { 8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2 },/* S2: */
    { 0, 15, 11, 8, 12, 9, 6, 3, 13, 1, 2, 4, 10, 7, 5, 14 },/* S3: */
    { 1, 15, 8, 3, 12, 0, 11, 6, 2, 5, 4, 10, 9, 14, 7, 13 },/* S4: */
    { 15, 5, 2, 11, 4, 10, 9, 12, 0, 3, 14, 8, 13, 6, 7, 1 },/* S5: */
    { 7, 2, 12, 5, 8, 4, 6, 11, 14, 9, 1, 15, 13, 3, 10, 0 },/* S6: */
    { 1, 13, 15, 0, 14, 8, 2, 11, 7, 4, 12, 10, 9, 3, 5, 6 },/* S7: */
    { 3, 8, 15, 1, 10, 6, 5, 11, 14, 13, 4, 2, 7, 0, 9, 12 },/* S0: */
    { 15, 12, 2, 7, 9, 0, 5, 10, 1, 11, 14, 8, 6, 13, 3, 4 },/* S1: */
    { 8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2 },/* S2: */
    { 0, 15, 11, 8, 12, 9, 6, 3, 13, 1, 2, 4, 10, 7, 5, 14 },/* S3: */
    { 1, 15, 8, 3, 12, 0, 11, 6, 2, 5, 4, 10, 9, 14, 7, 13 },/* S4: */
    { 15, 5, 2, 11, 4, 10, 9, 12, 0, 3, 14, 8, 13, 6, 7, 1 },/* S5: */
    { 7, 2, 12, 5, 8, 4, 6, 11, 14, 9, 1, 15, 13, 3, 10, 0 },/* S6: */
    { 1, 13, 15, 0, 14, 8, 2, 11, 7, 4, 12, 10, 9, 3, 5, 6 } /* S7: */
    };
    
    static final byte[] IPtable = new byte[] {
        0, 32, 64,  96,  1, 33, 65,  97,  2, 34, 66,  98,  3, 35, 67,  99,
        4, 36, 68, 100,  5, 37, 69, 101,  6, 38, 70, 102,  7, 39, 71, 103,
        8, 40, 72, 104,  9, 41, 73, 105, 10, 42, 74, 106, 11, 43, 75, 107,
       12, 44, 76, 108, 13, 45, 77, 109, 14, 46, 78, 110, 15, 47, 79, 111,
       16, 48, 80, 112, 17, 49, 81, 113, 18, 50, 82, 114, 19, 51, 83, 115,
       20, 52, 84, 116, 21, 53, 85, 117, 22, 54, 86, 118, 23, 55, 87, 119,
       24, 56, 88, 120, 25, 57, 89, 121, 26, 58, 90, 122, 27, 59, 91, 123,
       28, 60, 92, 124, 29, 61, 93, 125, 30, 62, 94, 126, 31, 63, 95, 127
   };

   static final byte[] FPtable = new byte[] {
        0,  4,  8, 12, 16, 20, 24, 28, 32,  36,  40,  44,  48,  52,  56,  60,
       64, 68, 72, 76, 80, 84, 88, 92, 96, 100, 104, 108, 112, 116, 120, 124,
        1,  5,  9, 13, 17, 21, 25, 29, 33,  37,  41,  45,  49,  53,  57,  61,
       65, 69, 73, 77, 81, 85, 89, 93, 97, 101, 105, 109, 113, 117, 121, 125,
        2,  6, 10, 14, 18, 22, 26, 30, 34,  38,  42,  46,  50,  54,  58,  62,
       66, 70, 74, 78, 82, 86, 90, 94, 98, 102, 106, 110, 114, 118, 122, 126,
        3,  7, 11, 15, 19, 23, 27, 31, 35,  39,  43,  47,  51,  55,  59,  63,
       67, 71, 75, 79, 83, 87, 91, 95, 99, 103, 107, 111, 115, 119, 123, 127
   };
   
   /*
    * Setzt/entfernt ein Bit an der gewuenschten Stelle einer Zahl
    * @param pos Position die manipuliert werden soll
    * @param value Wert der manipuliert werden soll
    * @param bit 1 oder 0, je nachdem welches Bit gesetzt werden soll
    * @return manipulierter Wert
    */
   public static int setBit(int pos, int value, int bit) {
       int flag = 1<<pos;
       return bit == 1 ? (value | flag) : (value & (~flag)) ;
   }
   
   /*
    * Gibt zurueck ob Bit an abgefragter Stelle gesetzt ist
    */
   public static int getBit(int pos, int value) {
       int offset = 1<<pos;
       return (value & offset) > 0 ? 1 : 0;
   }
   
   /*
    * Setzt Bit bis Pos 0-127, aufgeteilt in 4*32 Bit Array
    */
//   public static int[] setIPbits(int[] value, int pos) {
//       int arrayPos = pos/32;
//       int nPos = pos%32;
//       
//       if(getBit(nPos,value[arrayPos])==0) {
//           value[arrayPos] = setBit(nPos,value[arrayPos]);
//       }
//       return value;
//   }
   
   /*
    * Gibt zurueck ob Bit an Pos 0-127 gesetzt ist
    */
//   public static int getIPbit(int[] value, int pos) {
//       int arrayPos = pos/32;
//       int offset = 1<<(pos%32);
//       return (value[arrayPos] & offset)>0?1:0; 
//   }
   
   public static void swapPosition(int pos1, int pos2, int[] array) {
       int arrayPos1 = pos1/32;
       int arrayPos2 = pos2/32;
       
       int temp = getBit(pos1%32,array[arrayPos1]);
       
       array[arrayPos1] = setBit(pos1%32, array[arrayPos1], getBit(pos2%32,array[arrayPos2]));
       array[arrayPos2] = setBit(pos2%32, array[arrayPos2], temp);
       
       System.out.println("p1:\t\tp2:");
       System.out.println();
       System.out.println(pos1+"\t\t"+pos2);
       System.out.println(pos1/32+"/"+pos1%32+"\t\t"+pos2/32+"/"+pos2%32);
       System.out.println("------------------------");
       System.out.println();
   }
   
    public static void main(String[] args)
    {
        int [] p = new int[4];
        p[0] = 1;
        p[1] = 2;
        p[2] = 3;
        p[3] = 4;
        
//        setIPbits(p, 126);
        
//        p = swapPosition(2, IPtable[2], p);

        
        for(int i=0; i<128; i++) {
            swapPosition(i,IPtable[i],p);
//            for(int x:p) {
//                System.out.print(x+" \t");
//            }
//            System.out.println();
        }
        
        for(int i=0; i<128; i++) {
            swapPosition(i,FPtable[i],p);
        }
        

        for(int i:p) {
            System.out.println(i);
        }
        
//        int p = 1;
//        int k = 128128739;
//        
//        int pIP = 0;
//        for(int i=0; i<128; i++) {
//            if(getBit(i,p)>0) {
//                pIP = setBit(IPtable[i],pIP);
//            }
//        }
//        System.out.println(pIP);
        
//        System.out.println(setBit(0,100));
//        System.out.println();
//        
//        int t00, t01, t02, t03, t04, t05, t06, t07, t08, t09, t10;
//        int t11, t12, t13, t14, t15, t16, t17, t18, t19;
//        int y0,y1,y2,y3;
//        
//        int value = 17185;
//        int x0,x1,x2,x3;
//        
//        x0 = value&15;
//        value = value >> 4;
//        x1 = (value&15);
//        value = value >> 4;
//        x2 = (value&15);
//        value = value >> 4;
//        x3 = (value&15);
//        
//        System.out.println(x0+" "+x1+" "+x2+" "+x3);
//        
//        
//        
//        
//        
//        t01 = x1  ^ x2 ;
//        t02 = x0  | x3 ;
//        t03 = x0  ^ x1 ;
//        y3  = t02 ^ t01;
//        t05 = x2  | y3 ;
//        t06 = x0  ^ x3 ;
//        t07 = x1  | x2 ;
//        t08 = x3  & t05;
//        t09 = t03 & t07;
//        y2  = t09 ^ t08;
//        t11 = t09 & y2 ;
//        t12 = x2  ^ x3 ;
//        t13 = t07 ^ t11;
//        t14 = x1  & t06;
//        t15 = t06 ^ t13;
//        y0  =     ~ t15;
//        t17 = y0  ^ t14;
//        y1  = t12 ^ t17;

        // System.out.println(v2);

    }
}
