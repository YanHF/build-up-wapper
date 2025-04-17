package com.huaifang.yan.message;


public class I18nErrorMessage extends Message {
    String code;

    public I18nErrorMessage(String message, String messageKey, Object... args) {
        super(message, messageKey, args);
    }

    @Override
    public String toString() {
        return "I18nError{" +
                "code='" + code + '\'' +
                ", message='" + this.getMessage() + '\'' +
                '}';
    }
}
