package com.huaifang.yan.utils;

import com.huaifang.yan.model.PolicyConfig;
import org.slf4j.Logger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * LoggerUtils
 *
 * @author xianyan.geng
 * @version LoggerUtils, v 0.1 2020/10/21 14:00 xianyan.geng Exp $
 */
public class LoggerUtils {

    /**
     * 日志打印
     * @param policyConfig
     * @param format
     * @param args
     */
    public static void debug(PolicyConfig policyConfig, Logger logger, String format, Object... args) {
        if (policyConfig.isDebug()) {
            logger.debug(format, args);
        }
    }

    /**
     * Info级别记录日志
     *
     * @param logger   slf4j logger实现
     * @param format   日志模版
     * @param args 日志参数
     */
    public static void info(Logger logger, String format, Object... args) {
        logger.info(format, args);
    }

    /**
     * Warn级别记录日志
     *
     * @param logger   slf4j logger实现
     * @param format   日志模版
     * @param args 日志参数
     */
    public static void warn(Logger logger, String format, Object... args) {
        logger.warn(format, args);
    }

    /**
     * 错误级别记录日志
     *
     * @param logger   slf4j logger实现
     * @param format   日志模版
     * @param e        异常
     * @param args 日志参数
     */
    public static void error(Logger logger, String format, Throwable e, Object... args) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
        logger.error(ft.getMessage(), e);
    }

}
