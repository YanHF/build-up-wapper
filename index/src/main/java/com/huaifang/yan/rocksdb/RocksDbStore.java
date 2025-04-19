package com.huaifang.yan.rocksdb;

import org.rocksdb.*;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class RocksDbStore implements Closeable {
    static {
        RocksDB.loadLibrary();
    }

    static private WriteOptions                           syncWriteOptions   = new WriteOptions().setSync(true);
    static private WriteOptions                           normalWriteOptions = new WriteOptions();

    private ColumnFamilyOptions                           cfOption;
    private TransactionDB                                 db;
    private ConcurrentHashMap<String, ColumnFamilyHandle> columnFamilyMap    = new ConcurrentHashMap<>();
    private AtomicLong                                    lastCompactTime    = new AtomicLong(0);

    public RocksDbStore(RocksDbOption rocksDbOption) throws RocksDBException {
        DBOptions option = buildOptions(rocksDbOption);
        cfOption = buildColumnFamilyOption(rocksDbOption);
        List<ColumnFamilyDescriptor> columnFamilyNames = getAllColumnFamilyName(rocksDbOption.getPath());
        TransactionDBOptions txnDbOptions = buildTransactionDBOptions();
        List<ColumnFamilyHandle> handleList = new ArrayList<>();
        db = TransactionDB.open(option, txnDbOptions, rocksDbOption.getPath(), columnFamilyNames, handleList);
        for (ColumnFamilyHandle handle : handleList) {
            columnFamilyMap.put(new String(handle.getName()), handle);
        }
    }

    @Override
    public void close() {
        //todo 在关闭时要处理当前的状态,并且保证当前没有get,put之类的操作进行当中
        db.close();
    }

    //获得数据库中预估的Key的数量
    public int getKeyNum() throws RocksDBException {
        if (System.currentTimeMillis() - lastCompactTime.get() > 60 * 1000) {
            compactRange();
        }
        String str = db.getProperty("rocksdb.estimate-num-keys");
        return Integer.parseInt(str);
    }

    public void compactRange() throws RocksDBException {
        db.compactRange();
    }

    public void compactRange(String columnFamilyName) throws Exception {
        ColumnFamilyHandle columnFamily = getColumnFamily(columnFamilyName);
        db.compactRange(columnFamily);
    }

    public int getKeyNumWithInCompactRange(String columnFamilyName) throws Exception {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCompactTime.get() > 60 * 1000) {
            compactRange(columnFamilyName);
            lastCompactTime.set(currentTime);
        }
        return getKeyNum(columnFamilyName);
    }

    public int getKeyNum(String columnFamilyName) throws Exception {
        ColumnFamilyHandle columnFamily = getColumnFamily(columnFamilyName);
        String str = db.getProperty(columnFamily, "rocksdb.estimate-num-keys");
        return Integer.parseInt(str);
    }

    public Transaction beginTransation() {
        return db.beginTransaction(normalWriteOptions);
    }

    public byte[] get(byte[] key) throws RocksDBException {
        return db.get(key);
    }

    public byte[] get(String columnFamilyName, byte[] key) throws Exception {
        ColumnFamilyHandle cfHandle = getColumnFamily(columnFamilyName);
        return db.get(cfHandle, key);
    }

    public void put(byte[] key, byte[] value) throws Exception {
        put(key, value, false);
    }

    public void put(byte[] key, byte[] value, int offest, int length, boolean flushToDb) throws Exception {
        if (key == null || key.length == 0) {
            throw new Exception("key can not be empty");
        }
        if (value == null || value.length == 0) {
            throw new Exception("value can not be empty");
        }
        if (flushToDb) {
            db.put(syncWriteOptions, key, 0, key.length, value, offest, length);
        } else {
            db.put(key, 0, key.length, value, offest, length);
        }
    }

    public void put(byte[] key, byte[] value, boolean flushToDb) throws Exception {
        if (key == null || key.length == 0) {
            throw new Exception("key can not be empty");
        }
        if (value == null || value.length == 0) {
            throw new Exception("value can not be empty");
        }
        if (flushToDb) {
            db.put(syncWriteOptions, key, value);
        } else {
            db.put(key, value);
        }
    }

    public void put(String columnFamilyName, byte[] key, byte[] value, boolean flushToDb) throws Exception {
        ColumnFamilyHandle columnFamilyHandle = this.getColumnFamily(columnFamilyName);
        if (key == null || key.length == 0) {
            throw new Exception("key can not be empty");
        }
        if (value == null || value.length == 0) {
            throw new Exception("value can not be empty");
        }
        if (flushToDb) {
            db.put(columnFamilyHandle, syncWriteOptions, key, value);
        } else {
            db.put(columnFamilyHandle, key, value);
        }
    }

    public void forEach(BiFunction<byte[], byte[], Boolean> action) {
        try (RocksIterator rocksIterator = db.newIterator()) {
            rocksIterator.seekToFirst();
            while (rocksIterator.isValid()) {
                boolean needContinue = action.apply(rocksIterator.key(), rocksIterator.value());
                if (needContinue) {
                    rocksIterator.next();
                } else {
                    break;
                }
            }
        }
    }

    public void forEach(String columnFamilyName, BiFunction<byte[], byte[], Boolean> action) throws Exception {
        ColumnFamilyHandle handle = getColumnFamily(columnFamilyName);

        try (RocksIterator rocksIterator = db.newIterator(handle)) {
            rocksIterator.seekToFirst();
            while (rocksIterator.isValid()) {
                if (action.apply(rocksIterator.key(), rocksIterator.value())) {
                    rocksIterator.next();
                } else {
                    break;
                }
            }
        }
    }

    public void forEach(byte[] prefix, BiFunction<byte[], byte[], Boolean> action) {
        try (RocksIterator rocksIterator = db.newIterator()) {
            foreachByPrefix(prefix, action, rocksIterator);
        }
    }

    public void forEach(String columnFamilyName, byte[] prefix, BiFunction<byte[], byte[], Boolean> action) throws Exception {
        ColumnFamilyHandle handle = getColumnFamily(columnFamilyName);
        try (RocksIterator rocksIterator = db.newIterator(handle)) {
            foreachByPrefix(prefix, action, rocksIterator);
        }
    }

    private void foreachByPrefix(byte[] prefix, BiFunction<byte[], byte[], Boolean> action, RocksIterator rocksIterator) {
        rocksIterator.seek(prefix);
        while (rocksIterator.isValid()) {
            if (!isPrefixEqual(rocksIterator.key(), prefix)) {
                break;
            }
            if (action.apply(rocksIterator.key(), rocksIterator.value())) {
                rocksIterator.next();
            } else {
                break;
            }
        }
    }

    public void delete(byte[] key) throws Exception {
        db.delete(key);
    }

    public void delete(String columnFamilyName, byte[] key) throws Exception {
        ColumnFamilyHandle handle = getColumnFamily(columnFamilyName);
        db.delete(handle, key);
    }

    public boolean isPrefixEqual(byte[] check, byte[] prefix) {
        if (prefix.length > check.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (check[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    public void createColumnFamily(String name) throws Exception {
        columnFamilyMap.compute(name, (k, v) -> {
            if (v != null) {
                throw new RuntimeException("ColumnFamily:" + k + " already exists");
            }
            try {
                return db.createColumnFamily(new ColumnFamilyDescriptor(name.getBytes(), cfOption));
            } catch (RocksDBException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void dropColumnFamily(String name) {
        columnFamilyMap.compute(name, (k, v) -> {
            if (v != null) {
                try {
                    db.dropColumnFamily(v);
                } catch (RocksDBException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });
    }

    public ColumnFamilyHandle getColumnFamily(String name) throws Exception {
        ColumnFamilyHandle handle = columnFamilyMap.get(name);
        if (handle != null) {
            return handle;
        } else {
            throw new Exception("can not find column family $name");
        }
    }

    public List<String> getColumnFamilyNames() {
        ArrayList<String> list = new ArrayList<>();
        Enumeration<String> keys = columnFamilyMap.keys();
        while (keys.hasMoreElements()) {
            list.add(keys.nextElement());
        }
        return list;
    }

    public boolean existsColumnFamily(String name) {
        return columnFamilyMap.containsKey(name);
    }

    private List<ColumnFamilyDescriptor> getAllColumnFamilyName(String path) throws RocksDBException {
        Options tmpOption = new Options();
        List<byte[]> columnFamilies = TransactionDB.listColumnFamilies(tmpOption, path);
        List<ColumnFamilyDescriptor> columnFamilyDescriptorList = columnFamilies.stream().map(p -> new ColumnFamilyDescriptor(p, cfOption)).collect(Collectors.toList());

        if (columnFamilyDescriptorList.isEmpty()) {
            columnFamilyDescriptorList.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOption));
        }

        return columnFamilyDescriptorList;
    }

    private TransactionDBOptions buildTransactionDBOptions() {
        return new TransactionDBOptions();
    }

    private DBOptions buildOptions(RocksDbOption rocksDbOption) {
        DBOptions option = new DBOptions();
        //当数据库不存在时自动创建
        option.setCreateIfMissing(rocksDbOption.isCreateIfMissing());
        //设置后台整理线程
        option.setMaxBackgroundCompactions(8);
        option.setMaxBackgroundJobs(4);
        option.setBytesPerSync(1048576);
        option.setWalSizeLimitMB(rocksDbOption.getWalSizeLimit());
        option.setWalTtlSeconds(rocksDbOption.getWalTTLSeconds());
        option.setMaxTotalWalSize(rocksDbOption.getMaxTotalWalSize());
        return option;
    }

    private ColumnFamilyOptions buildColumnFamilyOption(RocksDbOption rocksDbOption) {
        BlockBasedTableConfig blockBasedTableConfig = new BlockBasedTableConfig();
        blockBasedTableConfig.setBlockCacheSize(rocksDbOption.getCacheSize());
        blockBasedTableConfig.setCacheIndexAndFilterBlocks(true);
        blockBasedTableConfig.setPinL0FilterAndIndexBlocksInCache(true);
        ColumnFamilyOptions options = new ColumnFamilyOptions();
        options.setTableFormatConfig(blockBasedTableConfig);
        options.setWriteBufferSize(rocksDbOption.getWriteBufferSize());
        options.setCompressionType(CompressionType.LZ4_COMPRESSION);
        options.setCompactionPriority(CompactionPriority.MinOverlappingRatio);
        options.setLevelCompactionDynamicLevelBytes(true);
        options.setBottommostCompressionType(CompressionType.ZSTD_COMPRESSION);
        return options;
    }

}
