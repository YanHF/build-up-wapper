package com.huaifang.yan;

import com.huaifang.yan.message.I18nErrorMessage;
import com.huaifang.yan.message.Message;

public class BusinessExceptionErrorFactory extends AbstractMessageFactory{
    @Override
    protected String provideBundleName() {
        return "business-error";
    }

    /**
     * LY0510300000=系统异常，异常信息:{0}
     *
     * @param message 异常信息
     * @return ERROR
     */
    public I18nErrorMessage systemError(String message) {
        return createError("BW0510300000", message);
    }

    /**
     * 从错误消息描述资源文件中根据错误代码创建错误消息对象
     *
     * @param errorCode 错误代码
     * @param args 错误消息占位符参数
     * @return 错误消息
     */
    protected final I18nErrorMessage createError(String errorCode, Object... args) {
        Message msg = getMessage(errorCode, args);
        I18nErrorMessage error = new I18nErrorMessage(msg.getMessage(), errorCode, args);
        return error;
    }

}
