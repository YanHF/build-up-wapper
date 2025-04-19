/**
 * LY.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.huaifang.yan.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HttpUtils
 *
 * @author ls11694
 * @version $Id: HttpUtils.java, v 0.1 2018/9/5 下午5:36 Exp $
 */
public class HttpUtils {

    /** 最大的空闲连接 **/
    private static int          DEFAULT_MAX_IDLE_CONNS           = 10;
    /** 空闲连接最大存活时间 **/
    private static long         DEFAULT_KEEP_ALIVE_DURATION_SECS = 120;
    /** 默认连接超时时间 **/
    private static long         DEFAULT_CONN_TIMEOUT_SECS        = 5;
    /** 默认读取超时时间 **/
    private static long         DEFAULT_READ_TIMEOUT_SECS        = 30;
    private static OkHttpClient okHttpClient;

    static {
        init(DEFAULT_MAX_IDLE_CONNS, DEFAULT_KEEP_ALIVE_DURATION_SECS, DEFAULT_CONN_TIMEOUT_SECS, DEFAULT_READ_TIMEOUT_SECS);
    }

    /**
     * 初始化
     *
     * @param maxIdleConns 最大的空闲连接
     * @param keepAliveSecs	最大存活时间，单位：秒
     * @param connTimeoutSecs 建立连接超时时间，单位：秒
     * @param readTimeoutSecs 读取超时时间，单位：秒
     */
    public static void init(int maxIdleConns, long keepAliveSecs, long connTimeoutSecs, long readTimeoutSecs) {
        okHttpClient = new OkHttpClient.Builder().connectionPool(new ConnectionPool(maxIdleConns, keepAliveSecs, TimeUnit.SECONDS))
            .connectTimeout(connTimeoutSecs, TimeUnit.SECONDS).readTimeout(readTimeoutSecs, TimeUnit.SECONDS).build();
    }

    /**
     * GET 请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return getResponse(url).body().string();
    }

    public static Response getResponse(String url) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        return okHttpClient.newCall(request).execute();
    }

    public static Response postResponse(String url, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request.Builder builder = new Request.Builder().url(url).addHeader("Content-Type", "application/json; charset=utf-8");
        Request request = builder.post(requestBody).build();
        return okHttpClient.newCall(request).execute();
    }

}
