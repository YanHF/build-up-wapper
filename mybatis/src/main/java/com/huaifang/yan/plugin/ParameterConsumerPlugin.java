package com.huaifang.yan.plugin;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;

import java.sql.PreparedStatement;
import java.util.Properties;

@Intercepts(value={@Signature(type = ParameterHandler.class,method = "setParameters",args = {PreparedStatement.class})})
public class ParameterConsumerPlugin implements Interceptor {

    private Properties _properties;
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this._properties=properties;
    }
}
