import com.alibaba.fastjson.JSON;
import com.huaifang.yan.ExtendDataProvider;
import com.huaifang.yan.model.CallBackEventWrapper;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class LocalExtendDataProvider implements ExtendDataProvider<LocalIndex> {

    private Consumer<CallBackEventWrapper<LocalIndex>> fullDataConsumer;

    private Consumer<CallBackEventWrapper<LocalIndex>> consumer;
    @Override
    public Collection<LocalIndex> queryFullData() {
        String str="[{\"dataId\":\"wrwryiuywery1\",\"name\":\"张三\",\"gender\":1},{\"dataId\":\"wrwqweryiuywewery1\",\"name\":\"李四\",\"gender\":2}]";
        return JSON.parseArray(str, LocalIndex.class);
    }

    @Override
    public void addCallbackEvent(Consumer<CallBackEventWrapper<LocalIndex>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void addFullCallbackEvent(Consumer<CallBackEventWrapper<LocalIndex>> consumer) {
        this.fullDataConsumer = consumer;
    }

    @Override
    public boolean isSupported(String policyType) {
        return true;
    }
}
