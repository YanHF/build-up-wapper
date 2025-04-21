package com.huaifang.yan.model;

import lombok.Getter;

@Getter
public class GenericEnum<T> {

    private final T value;

    public GenericEnum(T value) {
        this.value = value;
    }

}
