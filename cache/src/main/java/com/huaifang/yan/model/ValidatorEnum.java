package com.huaifang.yan.model;

import com.huaifang.yan.BusinessExceptionErrorFactory;
import com.huaifang.yan.exeption.BusinessException;
import lombok.Getter;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
public enum ValidatorEnum {

    Name(t-> Objects.equals(t.getValue(), "A"),new BusinessException(EnumException.errorFactory.systemError("当前变量不等于A")));

    private final Predicate<GenericEnum<String>> predicate;


    private BusinessException businessException;
    ValidatorEnum(Predicate<GenericEnum<String>> actor, BusinessException businessException) {
        this.predicate = actor;
    }

}
