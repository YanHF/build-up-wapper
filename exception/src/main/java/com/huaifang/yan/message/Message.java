package com.huaifang.yan.message;

import lombok.Getter;

import java.io.Serializable;

public class Message implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    /**消息数据文本*/
    private final String            message;

    /**消息代码
     * -- GETTER --
     *  获取消息Key
     *
     */
    @Getter
    private String            messageKey;

    /**消息模版占位符参数
     * -- GETTER --
     *  获取消息占位符参数
     *
     */
    @Getter
    private Object[]          args;

    /**下一条消息
     * -- GETTER --
     *  获取关联的下一条消息
     *
     */
    @Getter
    private Message           nextMessage;

    public Message(String message, String messageKey, Object... args) {
        super();
        this.message = message;
        this.messageKey = messageKey;
        this.args = args;
    }

    /**
     * 获取消息描述文本
     *
     * @return the message
     */
    public String getMessage() {
        return message + (nextMessage != null ? ". " + nextMessage.getMessage() : "");
    }

    /**
     * 设置关联的下一条消息
     *
     * @param nextMessage the nextMessage to set
     */
    public Message setNextMessage(Message nextMessage) {
        this.nextMessage = nextMessage;
        return this;
    }

    @Override
    public String toString() {
        return this.getMessage();
    }
}
