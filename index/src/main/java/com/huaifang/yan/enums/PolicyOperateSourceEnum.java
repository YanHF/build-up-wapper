package com.huaifang.yan.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 政策操作来源枚举
 *
 * @author xianyan.geng
 * @version PolicyOperateTypeEnum, v 0.1 2020/10/21 11:40 xianyan.geng Exp $
 */
public enum PolicyOperateSourceEnum {
    /**
     * WEB操作
     */
    WEB(0),
    /**
     * EXCEL导入
     */
    EXCEL(1),
    /**
     * API导入
     */
    API(2);

    /** code */
    private final int code;

    PolicyOperateSourceEnum(int code) {
        this.code = code;
    }

    /**
     * 根据Code获取枚举类
     *
     * @param code the code
     * @return by code
     */
    public static PolicyOperateSourceEnum getByCode(Integer code) {
        if (code == null) {
            return PolicyOperateSourceEnum.WEB;
        }
        return PolicyOperateSourceEnum.values()[code];
    }

    /**
     * Gets by name.
     *
     * @param name the name
     * @return the by name
     */
    public static PolicyOperateSourceEnum getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (PolicyOperateSourceEnum value : PolicyOperateSourceEnum.values()) {
            if (StringUtils.equals(name, value.name())) {
                return value;
            }
        }
        return null;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }
}
