package com.huaifang.yan.rocksdb;

public class RocksDbOption {
    //写入操作是否同步,如果为true,则写入性能会大大降低,但能保证数据完整,默认为false
    private boolean writeSync       = false;
    //如果不存在则创建数据库
    private boolean createIfMissing = true;
    //数据库路径
    private String  path            = "./rocksdb";
    //设置写入缓冲区的大小,如果超过这个大小,rocksdb会延缓写入
    private long    writeBufferSize = 256 * 1024 * 1024;
    //设置日志文件保存的最大大小--默认不保留,合并后就删除
    private long    walSizeLimit    = 0;
    //设置日志文件保留的天数--默认不保留-合并后就删除
    private long    walTTLSeconds   = 0;
    //设置基于LRU的缓存空间的最大大小
    private long    CacheSize       = 256 * 1024 * 1024;
    //设置WAL保留的最大大小
    private long    maxTotalWalSize = 64 * 1024 * 1024;

    public boolean isWriteSync() {
        return writeSync;
    }

    public RocksDbOption setWriteSync(boolean writeSync) {
        this.writeSync = writeSync;
        return this;
    }

    public boolean isCreateIfMissing() {
        return createIfMissing;
    }

    public RocksDbOption setCreateIfMissing(boolean createIfMissing) {
        this.createIfMissing = createIfMissing;
        return this;
    }

    public String getPath() {
        return path;
    }

    public RocksDbOption setPath(String path) {
        this.path = path;
        return this;
    }

    public long getWriteBufferSize() {
        return writeBufferSize;
    }

    public RocksDbOption setWriteBufferSize(long writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
        return this;
    }

    public long getWalSizeLimit() {
        return walSizeLimit;
    }

    public RocksDbOption setWalSizeLimit(long walSizeLimit) {
        this.walSizeLimit = walSizeLimit;
        return this;
    }

    public long getWalTTLSeconds() {
        return walTTLSeconds;
    }

    public RocksDbOption setWalTTLSeconds(long walTTLSeconds) {
        this.walTTLSeconds = walTTLSeconds;
        return this;
    }

    public long getCacheSize() {
        return CacheSize;
    }

    public RocksDbOption setCacheSize(long cacheSize) {
        CacheSize = cacheSize;
        return this;
    }

    public long getMaxTotalWalSize() {
        return maxTotalWalSize;
    }

    public RocksDbOption setMaxTotalWalSize(long maxTotalWalSize) {
        this.maxTotalWalSize = maxTotalWalSize;
        return this;
    }
}
