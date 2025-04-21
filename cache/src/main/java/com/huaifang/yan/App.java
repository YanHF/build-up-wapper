package com.huaifang.yan;

import com.huaifang.yan.exeption.BusinessException;
import com.huaifang.yan.model.GenericEnum;
import com.huaifang.yan.model.MyEnum;
import com.huaifang.yan.model.ValidatorEnum;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws BusinessException {
        System.out.println(MyEnum.INT_VALUE.getValue().getValue());
        System.out.println(MyEnum.STRING_VALUE.getValue().getValue());
        System.out.println(MyEnum.DOUBLE_VALUE.getValue().getValue());

        ValidatorEnum.Name.getPredicate().test(new GenericEnum<String>("123213"));
        throw ValidatorEnum.Name.getBusinessException();
    }
}
