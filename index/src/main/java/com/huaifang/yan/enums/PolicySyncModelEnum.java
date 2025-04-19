package com.huaifang.yan.enums;


import com.huaifang.yan.main.PolicySync;
import com.huaifang.yan.model.PolicySearchWrapper;
import com.huaifang.yan.rocksdb.RocksDbStore;
import com.huaifang.yan.sync.BatchPolicySync;
import com.huaifang.yan.sync.DefaultPolicySync;
import com.huaifang.yan.sync.VipPolicySync;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;

/**
 * 政策同步模式枚举类
 *
 * @author xianyan.geng
 * @version PolicySyncModelEnum, v 0.1 2020/10/20 11:56 xianyan.geng Exp $
 */
@Getter
@AllArgsConstructor
public enum PolicySyncModelEnum {
                                 /** 正常模式：一种规则一个队列 */
                                 NORMAL(((store, searchWrapper) -> new DefaultPolicySync(store, searchWrapper))),
                                 /** 批量模式：一种规则一个手工操作队列和一个批量操作队列 */
                                 BATCH(((store, searchWrapper) -> new BatchPolicySync(store, searchWrapper))),
                                 /** VIP模式：按供应商拆分队列，没有单独配置对供应商合并为一个队列 */
                                 VIP(((store, searchWrapper) -> new VipPolicySync(store, searchWrapper)));

    private final BiFunction<RocksDbStore, PolicySearchWrapper, PolicySync> biFunction;

    /**
     * 根据名称获取枚举类
     * @param name
     * @return
     */
    public static PolicySyncModelEnum getByName(String name) {
        for (PolicySyncModelEnum e : PolicySyncModelEnum.values()) {
            if (StringUtils.equals(e.name(), name)) {
                return e;
            }
        }
        return PolicySyncModelEnum.NORMAL;
    }
}
