package com.huaifang.yan.classLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ConfigClassLoader extends ClassLoader {
    @Override
    protected URL findResource(String name) {
        String path = "/Users/user/Documents/config";
        try {
            URL url = new URL("file",null,path+"/"+name);
            return url;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        URL url = findResource(name);

        try {
            return url==null?null:url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
