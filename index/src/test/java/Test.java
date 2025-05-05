import com.huaifang.yan.ExtendPolicySyncer;
import com.huaifang.yan.enums.PolicyUpdateTypeEnum;
import com.huaifang.yan.model.CallBackEventWrapper;
import com.huaifang.yan.model.PolicyConfig;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;

public class Test {

    public static void main(String[] args) throws Exception {
        PolicyConfig policyConfig = new PolicyConfig();
        policyConfig.setPolicySyncType(PolicySyncTypeEnum.PaperPolicyDO);
        policyConfig.setName("PaperPolicyDO");
        policyConfig.setDebug(true);

        LocalExtendDataProvider localExtendDataProvider = new LocalExtendDataProvider();
        ExtendPolicySyncer<LocalIndex> syncer=new ExtendPolicySyncer<LocalIndex>(policyConfig,localExtendDataProvider);
        //syncer.addCallbackEvent(Test::callbackEvent);

        syncer.start();
    }

    //CallBackEventWrapper<T>
    public static void callbackEvent(CallBackEventWrapper<LocalIndex> wrapper) {
        System.out.println("callbackEvent");
    }
}
