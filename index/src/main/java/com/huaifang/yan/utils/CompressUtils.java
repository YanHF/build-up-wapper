package com.huaifang.yan.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * CompressUtils
 *
 * @author xianyan.geng
 * @version CompressUtils, v 0.1 2020/11/03 13:40 xianyan.geng Exp $
 */
public class CompressUtils {

    /**
     * Compress byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    public static byte[] encode(byte[] data) throws IOException {
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream(Math.max((int) (data.length * 0.4), 32));
                GZIPOutputStream gzip = new GZIPOutputStream(out)
        ) {
            gzip.write(data);
            gzip.close();
            return out.toByteArray();
        }
    }

    /**
     * Un compress byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    public static byte[] decode(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return null;
        }
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream(Math.max(data.length * 4, 32));
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                GZIPInputStream gzip = new GZIPInputStream(bis)
        ) {
            byte[] buf = new byte[1024];
            int num = -1;
            while ((num = gzip.read(buf, 0, buf.length)) > 0) {
                baos.write(buf, 0, num);
            }
            baos.flush();
            return baos.toByteArray();
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        byte[] sources = "hello".getBytes(Charset.forName("UTF-8"));
        System.out.println(sources.length);
        byte[] encodes = encode(sources);
        System.out.println(encodes.length);
        System.out.println(new String(encodes,"UTF-8"));
        byte[] decodes = decode(encodes);
        System.out.println(decodes.length);
        System.out.println(new String(decodes,"UTF-8"));
    }
}
