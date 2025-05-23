package com.huaifang.yan;

import com.huaifang.yan.exeption.BusinessException;
import com.huaifang.yan.model.GenericEnum;
import com.huaifang.yan.model.MyEnum;
import com.huaifang.yan.model.ValidatorEnum;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws BusinessException {


    }

    public void Start() throws BusinessException {
       // TomcatWebServer.this.getTomcat().getServer().await();
    }
}
