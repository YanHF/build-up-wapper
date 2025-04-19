package com.huaifang.yan.utils;

/**
 * 协议格式转换工具
 *
 * @author xianyan.geng
 * @version ProtocolConverter, v 0.1 2020/10/22 09:58 xianyan.geng Exp $
 */
public class ProtocolConverter {
    /**
     * Int 2 bytes byte [ ].
     *
     * @param num the num
     * @return the byte [ ]
     */
    public static byte[] int2Bytes(int num) {
        byte[] byteNum = new byte[4];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * Bytes 2 int int.
     *
     * @param byteNum the byte num
     * @return the int
     */
    public static int bytes2Int(byte[] byteNum) {
        int num = 0;
        for (int ix = 0; ix < 4; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

    /**
     * Int 2 one byte byte.
     *
     * @param num the num
     * @return the byte
     */
    public static byte int2OneByte(int num) {
        return (byte) (num & 0x000000ff);
    }

    /**
     * One byte 2 int int.
     *
     * @param byteNum the byte num
     * @return the int
     */
    public static int oneByte2Int(byte byteNum) {
        return byteNum > 0 ? byteNum : (128 + (128 + byteNum));
    }

    /**
     * Long 2 bytes byte [ ].
     *
     * @param num the num
     * @return the byte [ ]
     */
    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * Bytes 2 long long.
     *
     * @param byteNum the byte num
     * @return the long
     */
    public static long bytes2Long(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }
}
