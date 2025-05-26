package com.huaifang.yan.concurrent;

public class SingletonClass {

   private String name=null;

    public SingletonClass(String name) {
        this.name = name;
    }

    private  static SingletonClass instance = null;

    public static SingletonClass getInstance() {
        if (instance == null) {
            synchronized (SingletonClass.class) {
                if (instance == null) {
                    instance = new SingletonClass("A");
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        SingletonClass singletonClass = SingletonClass.getInstance();
        if(singletonClass.name.equals("A")){
            System.out.println(singletonClass.name);
        }
    }
}
