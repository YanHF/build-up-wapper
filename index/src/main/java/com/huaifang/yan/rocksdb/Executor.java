package com.huaifang.yan.rocksdb;

import com.huaifang.yan.Main;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Executor {

    private String dbPath;

    private static RocksDB db = null;
    static {
        try {
            RocksDB.loadLibrary();
            Options options = new Options();
            options.setCreateIfMissing(true);
            db=  RocksDB.open(options,"/Users/yanhuaifang/Documents/data/rocksdb");
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    public void  put(String key,String value) throws RocksDBException {

        db.put(key.getBytes(StandardCharsets.UTF_8),value.getBytes(StandardCharsets.UTF_8));


    }

    public  String  get(String key) throws RocksDBException {
          return new String(db.get(key.getBytes(StandardCharsets.UTF_8)));
    }


    public static void main(String[] args) throws RocksDBException {
        Executor executor = new Executor();
        executor.put("name","huaifang.yan");
        System.out.println(executor.get("name"));

        executor.put("name","zuozuo");
        System.out.println(executor.get("name"));

        executor.put("name","youyou");
        System.out.println(executor.get("name"));
    }

}
