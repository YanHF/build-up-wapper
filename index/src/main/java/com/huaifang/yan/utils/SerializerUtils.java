package com.huaifang.yan.utils;

import com.alibaba.fastjson.JSON;
import com.huaifang.yan.enums.SerializerTypeEnum;


import java.io.IOException;
import java.nio.charset.Charset;

/**
 * SerializerUtils
 *
 * @author xianyan.geng
 * @version SerializerUtils, v 0.1 2020/10/20 17:48 xianyan.geng Exp $
 */
public class SerializerUtils {

    /**
     * The constant DEFAULT_CHARSET.
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * Serializer byte [ ].
     *
     * @param <T>            the type parameter
     * @param serializerType the serializer type
     * @param data           the data
     * @return the byte [ ]
     */
    public static <T> byte[] serializer(SerializerTypeEnum serializerType, T data) {
        switch (serializerType) {
            case KRYO:
                return KryoUtils.writeObjectToByteArray(data);
            case JSON:
                return JSON.toJSONBytes(data);
            default:
                throw new RuntimeException("can not support SerializerType" + serializerType.name());
        }
    }

    /**
     * De serializer t.
     *
     * @param <T>            the type parameter
     * @param serializerType the serializer type
     * @param data           the data
     * @param clazz          the clazz
     * @return the t
     */
    public static <T> T deSerializer(SerializerTypeEnum serializerType, byte[] data, Class<T> clazz) {
        switch (serializerType) {
            case KRYO:
                return KryoUtils.readObjectFromByteArray(data, clazz);
            case JSON:
                return JSON.parseObject(new String(data, DEFAULT_CHARSET), clazz);
            default:
                throw new RuntimeException("can not support SerializerType" + serializerType.name());
        }
    }

    /**
     * Serializer and compress byte [ ].
     *
     * @param <T>            the type parameter
     * @param serializerType the serializer type
     * @param data           the data
     * @return the byte [ ]
     */
    public static <T> byte[] serializerAndCompress(SerializerTypeEnum serializerType, T data) {
        try {
            return CompressUtils.encode(serializer(serializerType, data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * De serializer and un compress t.
     *
     * @param <T>            the type parameter
     * @param serializerType the serializer type
     * @param data           the data
     * @param clazz          the clazz
     * @return the t
     */
    public static <T> T deSerializerAndUnCompress(SerializerTypeEnum serializerType, byte[] data, Class<T> clazz) {
        try {
            return deSerializer(serializerType, CompressUtils.decode(data), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
