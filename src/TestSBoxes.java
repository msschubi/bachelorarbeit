import java.math.BigInteger;

public class TestSBoxes {

    static final byte[][] Sbox = new byte[][] { 
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
    
    static final byte[][] SboxInverse = new byte[][] {
        {13, 3,11, 0,10, 6, 5,12, 1,14, 4, 7,15, 9, 8, 2 },/* InvS0: */
        { 5, 8, 2,14,15, 6,12, 3,11, 4, 7, 9, 1,13,10, 0 },/* InvS1: */
        {12, 9,15, 4,11,14, 1, 2, 0, 3, 6,13, 5, 8,10, 7 },/* InvS2: */
        { 0, 9,10, 7,11,14, 6,13, 3, 5,12, 2, 4, 8,15, 1 },/* InvS3: */
        { 5, 0, 8, 3,10, 9, 7,14, 2,12,11, 6, 4,15,13, 1 },/* InvS4: */
        { 8,15, 2, 9, 4, 1,13,14,11, 6, 5, 3, 7,12,10, 0 },/* InvS5: */
        {15,10, 1,13, 5, 3, 6, 0, 4, 9,14, 7, 2,12, 8,11 },/* InvS6: */
        { 3, 0, 6,13, 9,14,15, 8, 5,12,11, 7,10, 1, 4, 2 },/* InvS7: */
        {13, 3,11, 0,10, 6, 5,12, 1,14, 4, 7,15, 9, 8, 2 },/* InvS0: */
        { 5, 8, 2,14,15, 6,12, 3,11, 4, 7, 9, 1,13,10, 0 },/* InvS1: */
        {12, 9,15, 4,11,14, 1, 2, 0, 3, 6,13, 5, 8,10, 7 },/* InvS2: */
        { 0, 9,10, 7,11,14, 6,13, 3, 5,12, 2, 4, 8,15, 1 },/* InvS3: */
        { 5, 0, 8, 3,10, 9, 7,14, 2,12,11, 6, 4,15,13, 1 },/* InvS4: */
        { 8,15, 2, 9, 4, 1,13,14,11, 6, 5, 3, 7,12,10, 0 },/* InvS5: */
        {15,10, 1,13, 5, 3, 6, 0, 4, 9,14, 7, 2,12, 8,11 },/* InvS6: */
        { 3, 0, 6,13, 9,14,15, 8, 5,12,11, 7,10, 1, 4, 2 },/* InvS7: */
        {13, 3,11, 0,10, 6, 5,12, 1,14, 4, 7,15, 9, 8, 2 },/* InvS0: */
        { 5, 8, 2,14,15, 6,12, 3,11, 4, 7, 9, 1,13,10, 0 },/* InvS1: */
        {12, 9,15, 4,11,14, 1, 2, 0, 3, 6,13, 5, 8,10, 7 },/* InvS2: */
        { 0, 9,10, 7,11,14, 6,13, 3, 5,12, 2, 4, 8,15, 1 },/* InvS3: */
        { 5, 0, 8, 3,10, 9, 7,14, 2,12,11, 6, 4,15,13, 1 },/* InvS4: */
        { 8,15, 2, 9, 4, 1,13,14,11, 6, 5, 3, 7,12,10, 0 },/* InvS5: */
        {15,10, 1,13, 5, 3, 6, 0, 4, 9,14, 7, 2,12, 8,11 },/* InvS6: */
        { 3, 0, 6,13, 9,14,15, 8, 5,12,11, 7,10, 1, 4, 2 },/* InvS7: */
        {13, 3,11, 0,10, 6, 5,12, 1,14, 4, 7,15, 9, 8, 2 },/* InvS0: */
        { 5, 8, 2,14,15, 6,12, 3,11, 4, 7, 9, 1,13,10, 0 },/* InvS1: */
        {12, 9,15, 4,11,14, 1, 2, 0, 3, 6,13, 5, 8,10, 7 },/* InvS2: */
        { 0, 9,10, 7,11,14, 6,13, 3, 5,12, 2, 4, 8,15, 1 },/* InvS3: */
        { 5, 0, 8, 3,10, 9, 7,14, 2,12,11, 6, 4,15,13, 1 },/* InvS4: */
        { 8,15, 2, 9, 4, 1,13,14,11, 6, 5, 3, 7,12,10, 0 },/* InvS5: */
        {15,10, 1,13, 5, 3, 6, 0, 4, 9,14, 7, 2,12, 8,11 },/* InvS6: */
        { 3, 0, 6,13, 9,14,15, 8, 5,12,11, 7,10, 1, 4, 2 } /* InvS7: */
    };

    
    static final byte[] IPtable = new byte[] {
        0, 32, 64,  96,  1, 33, 65,  97,  2, 34, 66,  98,  3, 35, 67,  99, //16
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
    * Setzt 4 Bits an eine gewuenschte Stelle einer Zahl
    * 
    * @param pos Endposition der 4 Bits (Referenz ist MSB)
    * @param value Zahl welche mit 4 Bits manipuliert werden soll
    * @param bits Zahl 0-15 die in value eingefuegt wird
    */
   public static int set4Bits(int pos, int value, int bits) {
       int pattern = -1 & ~(1<<pos) & ~(1<<(pos-1)) & ~(1<<(pos-2)) & ~(1<<(pos-3));
       value &= pattern; // an die 4 Stellen werden 0en gesetzt
       //0en werden hier mit den 4 Bits ueberschrieben
       value |= ((8&bits)<<pos-3) | ((4&bits)<<(pos-3)) | ((2&bits)<<(pos-3)) | ((1&bits)<<(pos-3));
       return value;
   }
   
   public static int get4Bits(int pos, int value) {
       int ones = 15 << pos-3;
           return (value&ones)>>>pos-3;
   }
   
   /*
    * Gibt zurueck ob Bit an abgefragter Stelle gesetzt ist
    */
   public static int getBit(int pos, int value) {
       int offset = 1<<pos;
       return (value & offset) != 0 ? 1 : 0;
   }
 
   public static void setArrayBit(int posIn,int posOut, int[] in, int[] out) {  
       int posOutArr = posOut/32;
       out[posOutArr] = setBit(posOut%32, out[posOutArr], getBit(posIn%32, in[posIn/32]));
   }
   
   public static int[] initialPermutation(int[] in) {
       int [] out = new int[4];
       
       for(int i=0; i<128; i++) {
           setArrayBit(IPtable[i], i, in, out);
       }
       return out;
   }
   
   public static int[] finalPermutation(int[] in) {
       int [] out = new int[4];
       
       for(int i=0; i<128; i++) {
           setArrayBit(FPtable[i], i, in, out);
       }
       return out;
   }
   
   /*
    * Konkateniert 4 Bits an eine bestimmte Position unter Zusicherung, dass an der 
    * Position sich vorher nur 0en befanden;
    * auf die Bits wird die entsprechende sBox angewendet
    */
   public static int sBox(int pos, int concatValue, int bits, int sBoxNr) {
       return (Sbox[sBoxNr][bits] << (pos-3)) | concatValue;
   }
   
   public static int invSBox(int pos, int concatValue, int bits, int sBoxNr) {
       return (SboxInverse[sBoxNr][bits] << (pos-3)) | concatValue;
   }
   
    public static void main(String[] args)
    {
        
//        int i = 0xFFFFFFFF;
//        int i = 0;
        
        int a = 1<<31;
        int b = 1<<30;
        int c = 1<<29;
        int d = 1<<28;
        int konkat = a|b|c|d;
        System.out.println("konkat: "+konkat);
        //testschleife sbox
        int temp = 0;
        for(int i=3; i<=31; i+=4) {
            temp = sBox(i, temp, get4Bits(i, konkat), 0);
        }
        System.out.println(temp);
        
        int temp2 = 0;
        for(int i=3; i<=31; i+=4) {
            temp2 = invSBox(i, temp2, get4Bits(i, temp), 0);
        }
        System.out.println(temp2);
        
        
//        int temp = sBox(31,concatValue,get4Bits(31, konkat),0);
        
        
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(c);
//        System.out.println(d);
//        
//        System.out.println(i|a|b|c|d);
//        System.out.println(set4Bits(31, i, 15));
        
//        System.out.println(i);
//        System.out.println(set4Bits(3, i, 10));
        
//        int i = 10212;
        
//        -1 000 00000 00000 00000 00000 00000
        
//        int [] in = new int[4];
//        in[0] = 1242340;
//        in[1] = 2024234;
//        in[2] = 3242340;
//        in[3] = (1<<31)-1;
//        
//        int [] out = initialPermutation(in);
//        for(int i:out) {
//            System.out.println(i);
//        }
//        
//        
//        System.out.println();
//        for(int a:finalPermutation(out)) {
//            System.out.println(a);
//        }
        
        
        
        
        



    }
}
