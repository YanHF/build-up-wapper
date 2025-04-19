package com.huaifang.yan.model;

import java.io.Serializable;

/**
 * 错误记录模型
 *
 * @author xianyan.geng
 * @version ErrorInfo, v 0.1 2020/10/20 13:46 xianyan.geng Exp $
 */
public class ErrorRecord implements Serializable {
    private static final long serialVersionUID = -8465440911422748472L;
    /** 内容 */
    private String message;
    /** 时间戳 */
    private long   time;

    public ErrorRecord() {

    }

    public ErrorRecord(String message, long time) {
        this.message = message;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
