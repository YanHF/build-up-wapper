package com.huaifang.yan.symbol;

/**
 * >> 有符号右移,左边补0,小于0补1
 */
public class SymbolTest {
    public static void main(String[] args) {

        String str=null;
        Integer cap=33;
        int n = cap - 1;//
         str=Integer.toBinaryString(n);
        n |= n >>> 1;// 9|4
         str=Integer.toBinaryString(n);
        n |= n >>> 2;
         str=Integer.toBinaryString(n);
        n |= n >>> 4;
        str=Integer.toBinaryString(n);
        n |= n >>> 8;
        str=Integer.toBinaryString(n);
        n |= n >>> 16;

        str=Integer.toBinaryString(n);
        Integer A=-1;

        //A=A>>2;
        //String str=Integer.toBinaryString(A);
        System.out.println(str);
    }

}
//1100011 >>1
//0110001
//1110011
//01111111

//1,2,4,8,16,32,64
//100

//64+32+4
 //      1100100
 //      1111111
