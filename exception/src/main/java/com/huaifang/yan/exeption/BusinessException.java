package com.huaifang.yan.exeption;

import com.huaifang.yan.message.I18nErrorMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends Exception {
    private String message;
    private I18nErrorMessage i18nError;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(I18nErrorMessage i18nError) {
        super(i18nError.getMessage());
        this.message = i18nError.getMessage();
        this.i18nError = i18nError;
    }

    public BusinessException(String message, Throwable cause, I18nErrorMessage i18nError) {
        super(message, cause);
        this.message = message;
        this.i18nError = i18nError;
    }

    public BusinessException(String message, I18nErrorMessage i18nError) {
        super(message);
        this.message = message;
        this.i18nError = i18nError;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "message='" + message + '\'' +
                ", i18nError=" + i18nError.toString() +
                '}';
    }
}
