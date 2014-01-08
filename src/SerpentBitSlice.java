public class SerpentBitSlice {
    static private int y0, y1, y2, y3, z;
    static private int t00, t01, t02, t03, t04, t05, t06, t07, t08, t09, t10;
    static private int t11, t12, t13, t14, t15, t16, t17, t18, t19;

    /**
     * sBoxen im Bitslice Modus
     * 
     * @param number Nummer der sBox
     * @param x mit x3 als MSB und x0 als LSB
     * @return int[] mit x0 MSB und x3 LSB
     */
    public static void sBoxBitSlice(int number, int[] x) {
        // S0
        if (number == 0) {
            t01 = x[1] ^ x[2];
            t02 = x[0] | x[3];
            t03 = x[0] ^ x[1];
            y3 = t02 ^ t01;
            t05 = x[2] | y3;
            t06 = x[0] ^ x[3];
            t07 = x[1] | x[2];
            t08 = x[3] & t05;
            t09 = t03 & t07;
            y2 = t09 ^ t08;
            t11 = t09 & y2;
            t12 = x[2] ^ x[3];
            t13 = t07 ^ t11;
            t14 = x[1] & t06;
            t15 = t06 ^ t13;
            y0 = ~t15;
            t17 = y0 ^ t14;
            y1 = t12 ^ t17;

            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;
        }
        if (number == 1) {
            t01 = x[0] | x[3];
            t02 = x[2] ^ x[3];
            t03 = ~x[1];
            t04 = x[0] ^ x[2];
            t05 = x[0] | t03;
            t06 = x[3] & t04;
            t07 = t01 & t02;
            t08 = x[1] | t06;
            y2 = t02 ^ t05;
            t10 = t07 ^ t08;
            t11 = t01 ^ t10;
            t12 = y2 ^ t11;
            t13 = x[1] & x[3];
            y3 = ~t10;
            y1 = t13 ^ t12;
            t16 = t10 | y1;
            t17 = t05 & t16;
            y0 = x[2] ^ t17;
            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;
        } else if (number == 2) {
            t01 = x[0] | x[2];
            t02 = x[0] ^ x[1];
            t03 = x[3] ^ t01;
            y0 = t02 ^ t03;
            t05 = x[2] ^ y0;
            t06 = x[1] ^ t05;
            t07 = x[1] | t05;
            t08 = t01 & t06;
            t09 = t03 ^ t07;
            t10 = t02 | t09;
            y1 = t10 ^ t08;
            t12 = x[0] | x[3];
            t13 = t09 ^ y1;
            t14 = x[1] ^ t13;
            y3 = ~t09;
            y2 = t12 ^ t14;
            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 3) {
            t01 = x[0] ^ x[2];
            t02 = x[0] | x[3];
            t03 = x[0] & x[3];
            t04 = t01 & t02;
            t05 = x[1] | t03;
            t06 = x[0] & x[1];
            t07 = x[3] ^ t04;
            t08 = x[2] | t06;
            t09 = x[1] ^ t07;
            t10 = x[3] & t05;
            t11 = t02 ^ t10;
            y3 = t08 ^ t09;
            t13 = x[3] | y3;
            t14 = x[0] | t07;
            t15 = x[1] & t13;
            y2 = t08 ^ t11;
            y0 = t14 ^ t15;
            y1 = t05 ^ t04;
            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 4) {
            t01 = x[0] | x[1];
            t02 = x[1] | x[2];
            t03 = x[0] ^ t02;
            t04 = x[1] ^ x[3];
            t05 = x[3] | t03;
            t06 = x[3] & t01;
            y3 = t03 ^ t06;
            t08 = y3 & t04;
            t09 = t04 & t05;
            t10 = x[2] ^ t06;
            t11 = x[1] & x[2];
            t12 = t04 ^ t08;
            t13 = t11 | t03;
            t14 = t10 ^ t09;
            t15 = x[0] & t05;
            t16 = t11 | t12;
            y2 = t13 ^ t08;
            y1 = t15 ^ t16;
            y0 = ~t14;
            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 5) {
            t01 = x[1] ^ x[3];
            t02 = x[1] | x[3];
            t03 = x[0] & t01;
            t04 = x[2] ^ t02;
            t05 = t03 ^ t04;
            y0 = ~t05;
            t07 = x[0] ^ t01;
            t08 = x[3] | y0;
            t09 = x[1] | t05;
            t10 = x[3] ^ t08;
            t11 = x[1] | t07;
            t12 = t03 | y0;
            t13 = t07 | t10;
            t14 = t01 ^ t11;
            y2 = t09 ^ t13;
            y1 = t07 ^ t08;
            y3 = t12 ^ t14;
            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 6) {
            t01 = x[0] & x[3];
            t02 = x[1] ^ x[2];
            t03 = x[0] ^ x[3];
            t04 = t01 ^ t02;
            t05 = x[1] | x[2];
            y1 = ~t04;
            t07 = t03 & t05;
            t08 = x[1] & y1;
            t09 = x[0] | x[2];
            t10 = t07 ^ t08;
            t11 = x[1] | x[3];
            t12 = x[2] ^ t11;
            t13 = t09 ^ t10;
            y2 = ~t13;
            t15 = y1 & t03;
            y3 = t12 ^ t07;
            t17 = x[0] ^ x[1];
            t18 = y2 ^ t15;
            y0 = t17 ^ t18;
            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 7) {
            t01 = x[0] & x[2];
            t02 = ~x[3];
            t03 = x[0] & t02;
            t04 = x[1] | t01;
            t05 = x[0] & x[1];
            t06 = x[2] ^ t04;
            y3 = t03 ^ t06;
            t08 = x[2] | y3;
            t09 = x[3] | t05;
            t10 = x[0] ^ t08;
            t11 = t04 & y3;
            y1 = t09 ^ t10;
            t13 = x[1] ^ y1;
            t14 = t01 ^ y1;
            t15 = x[2] ^ t05;
            t16 = t11 | t13;
            t17 = t02 | t14;
            y0 = t15 ^ t17;
            y2 = x[0] ^ t16;
            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;
        }
    }
    
    public static void sBoxInvBitSlice(int number, int[] x) {
        // S0
        if (number == 0) {
            t01 = x[2]  ^ x[3] ;
            t02 = x[0]  | x[1] ;
            t03 = x[1]  | x[2] ;
            t04 = x[2]  & t01;
            t05 = t02 ^ t01;
            t06 = x[0]  | t04;
            y2  =     ~ t05;
            t08 = x[1]  ^ x[3] ;
            t09 = t03 & t08;
            t10 = x[3]  | y2 ;
            y1  = t09 ^ t06;
            t12 = x[0]  | t05;
            t13 = y1  ^ t12;
            t14 = t03 ^ t10;
            t15 = x[0]  ^ x[2] ;
            y3  = t14 ^ t13;
            t17 = t05 & t13;
            t18 = t14 | t17;
            y0  = t15 ^ t18;


            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;
        }
        if (number == 1) {
            t01 = x[0]  ^ x[1] ;
            t02 = x[1]  | x[3] ;
            t03 = x[0]  & x[2] ;
            t04 = x[2]  ^ t02;
            t05 = x[0]  | t04;
            t06 = t01 & t05;
            t07 = x[3]  | t03;
            t08 = x[1]  ^ t06;
            t09 = t07 ^ t06;
            t10 = t04 | t03;
            t11 = x[3]  & t08;
            y2  =     ~ t09;
            y1  = t10 ^ t11;
            t14 = x[0]  | y2 ;
            t15 = t06 ^ y1 ;
            y3  = t01 ^ t04;
            t17 = x[2]  ^ t15;
            y0  = t14 ^ t17;
            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;
        } else if (number == 2) {
            t01 = x[0]  ^ x[3] ;
            t02 = x[2]  ^ x[3] ;
            t03 = x[0]  & x[2] ;
            t04 = x[1]  | t02;
            y0  = t01 ^ t04;
            t06 = x[0]  | x[2] ;
            t07 = x[3]  | y0 ;
            t08 =     ~ x[3] ;
            t09 = x[1]  & t06;
            t10 = t08 | t03;
            t11 = x[1]  & t07;
            t12 = t06 & t02;
            y3  = t09 ^ t10;
            y1  = t12 ^ t11;
            t15 = x[2]  & y3 ;
            t16 = y0  ^ y1 ;
            t17 = t10 ^ t15;
            y2  = t16 ^ t17;

            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 3) {
            t01 = x[2]  | x[3] ;
            t02 = x[0]  | x[3] ;
            t03 = x[2]  ^ t02;
            t04 = x[1]  ^ t02;
            t05 = x[0]  ^ x[3] ;
            t06 = t04 & t03;
            t07 = x[1]  & t01;
            y2  = t05 ^ t06;
            t09 = x[0]  ^ t03;
            y0  = t07 ^ t03;
            t11 = y0  | t05;
            t12 = t09 & t11;
            t13 = x[0]  & y2 ;
            t14 = t01 ^ t05;
            y1  = x[1]  ^ t12;
            t16 = x[1]  | t13;
            y3  = t14 ^ t16;

            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 4) {
            t01 = x[1]  | x[3] ;
            t02 = x[2]  | x[3] ;
            t03 = x[0]  & t01;
            t04 = x[1]  ^ t02;
            t05 = x[2]  ^ x[3] ;
            t06 =     ~ t03;
            t07 = x[0]  & t04;
            y1  = t05 ^ t07;
            t09 = y1  | t06;
            t10 = x[0]  ^ t07;
            t11 = t01 ^ t09;
            t12 = x[3]  ^ t04;
            t13 = x[2]  | t10;
            y3  = t03 ^ t12;
            t15 = x[0]  ^ t04;
            y2  = t11 ^ t13;
            y0  = t15 ^ t09;

            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 5) {
            t01 = x[0]  & x[3] ;
            t02 = x[2]  ^ t01;
            t03 = x[0]  ^ x[3] ;
            t04 = x[1]  & t02;
            t05 = x[0]  & x[2] ;
            y0  = t03 ^ t04;
            t07 = x[0]  & y0 ;
            t08 = t01 ^ y0 ;
            t09 = x[1]  | t05;
            t10 =     ~ x[1] ;
            y1  = t08 ^ t09;
            t12 = t10 | t07;
            t13 = y0  | y1 ;
            y3  = t02 ^ t12;
            t15 = t02 ^ t13;
            t16 = x[1]  ^ x[3] ;
            y2  = t16 ^ t15;

            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 6) {
            t01 = x[0]  ^ x[2] ;
            t02 =     ~ x[2] ;
            t03 = x[1]  & t01;
            t04 = x[1]  | t02;
            t05 = x[3]  | t03;
            t06 = x[1]  ^ x[3] ;
            t07 = x[0]  & t04;
            t08 = x[0]  | t02;
            t09 = t07 ^ t05;
            y1  = t06 ^ t08;
            y0  =     ~ t09;
            t12 = x[1]  & y0 ;
            t13 = t01 & t05;
            t14 = t01 ^ t12;
            t15 = t07 ^ t13;
            t16 = x[3]  | t02;
            t17 = x[0]  ^ y1 ;
            y3  = t17 ^ t15;
            y2  = t16 ^ t14;

            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;

        } else if (number == 7) {
            t01 = x[0]  & x[1] ;
            t02 = x[0]  | x[1] ;
            t03 = x[2]  | t01;
            t04 = x[3]  & t02;
            y3  = t03 ^ t04;
            t06 = x[1]  ^ t04;
            t07 = x[3]  ^ y3 ;
            t08 =     ~ t07;
            t09 = t06 | t08;
            t10 = x[1]  ^ x[3] ;
            t11 = x[0]  | x[3] ;
            y1  = x[0]  ^ t09;
            t13 = x[2]  ^ t06;
            t14 = x[2]  & t11;
            t15 = x[3]  | y1 ;
            t16 = t01 | t10;
            y0  = t13 ^ t15;
            y2  = t14 ^ t16;

            x[0] = y0;
            x[1] = y1;
            x[2] = y2;
            x[3] = y3;
        }
    }
}
