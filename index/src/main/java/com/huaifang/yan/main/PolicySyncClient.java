package com.huaifang.yan.main;


import com.alibaba.fastjson.JSON;
import com.huaifang.yan.model.PolicyConfig;
import com.huaifang.yan.model.PolicyMonitor;
import com.huaifang.yan.model.PolicySearchWrapper;
import com.huaifang.yan.utils.HttpUtils;
import com.huaifang.yan.utils.IpHelper;
import com.huaifang.yan.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;

/**
 * 政策同步系统请求客户端
 *
 * @author xianyan.geng
 * @version PolicySyncClient, v 0.1 2020/10/20 13:48 xianyan.geng Exp $
 */
@Slf4j
public class PolicySyncClient {
    /** Http状态码 */
    private static final int    OK_STATUS         = 200;
    /** Docker实例IP */
    private static final String DOCKER_IP         = "DAOKEIP";
    /** Docker实例ID */
    private static final String DOCKER_ID         = "DAOKEID";
    /** 拉取政策接口地址后缀 */
    private static final String SEARCH_URL_SUFFIX = "policy?type=%s&mode=%d&revision=%d&serializer=%s&dockerId=%s&env=%s&searchType=%s&searchValue=%s&searchAllValue=%s";
    /** 上报版本接口地址后缀 */
    private static final String UPLOAD_URL_SUFFIX = "report/%s/%s";

    /**
     * 调用政策同步系统查询接口
     * @param wrapper
     * @return
     * @throws Exception
     */
    public static byte[] search(PolicySearchWrapper wrapper) throws Exception {
        byte[] result = null;
        Response response = null;
        try {
            String url = buildSearchUrl(wrapper);
            LoggerUtils.debug(wrapper.getPolicyConfig(), log, "[{}]search policy by the url is {}", wrapper.getPolicySyncTypeName(), url);
            response = HttpUtils.getResponse(url);
            if (response.code() != OK_STATUS) {
                throw new Exception("访问:" + url + " 出错,code:" + response.code() + ",message:" + response.message());
            }
            ResponseBody body = response.body();
            if (body != null) {
                result = body.bytes();
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return result;
    }

    /**
     * 调用政策同步系统上传版本接口
     * @param wrapper
     * @param rocksdbKeySize
     * @param queueSize
     * @throws Exception
     */
    public static void upload(PolicySearchWrapper wrapper, long rocksdbKeySize, int queueSize) throws Exception {
        Response response = null;
        try {
            PolicySearchWrapper.ProcessorStatePair statePair = wrapper.getStatePair();
            PolicyMonitor monitor = new PolicyMonitor();
            monitor.setPolicySyncType(wrapper.getPolicySyncType());
            monitor.setStarted(statePair.isStarted());
            monitor.setStartTime(statePair.getStartTime());
            monitor.setSize(rocksdbKeySize);
            monitor.setUpdateTime(statePair.getLastSearchTime().getTime());
            monitor.setRevision(statePair.getRevision());
            monitor.setRevisions(statePair.getRevisions());
            monitor.setRecords(statePair.getErrorRecords());
            monitor.setEventSize(queueSize);
            monitor.setSearchType(statePair.getSearchType());
            monitor.setSearchValue(statePair.getSearchValue());
            monitor.setSearchAllValue(wrapper.getPolicyConfig().getSearchValue());
            monitor.setEnv(wrapper.getPolicyConfig().getEnv());
            response = HttpUtils.postResponse(buildUploadUrl(wrapper), JSON.toJSONString(monitor));
            if (response.code() != OK_STATUS) {
                throw new RuntimeException("提交数据失败,状态为:" + response.code());
            }
        } finally {
            if (response != null) {
                response.close();
            }
            wrapper.getStatePair().release();
        }
    }

    /**
     * 构建拉取政策接口地址
     * @param wrapper
     * @return
     */
    private static String buildSearchUrl(PolicySearchWrapper wrapper) {
        PolicyConfig policyConfig = wrapper.getPolicyConfig();
        PolicySearchWrapper.ProcessorStatePair statePair = wrapper.getStatePair();
        String url = buildUrl(wrapper.getPolicyConfig().getBackgroundUrl(), SEARCH_URL_SUFFIX);
        String serializerType = policyConfig.getSerializerType().name();
        return String.format(url, wrapper.getPolicySyncTypeName(), statePair.getMode(), statePair.getRevision(), serializerType, getDockerId(), policyConfig.getEnv(),
            statePair.getSearchType(), statePair.getSearchValue(), policyConfig.getSearchValue());
    }

    /**
     * 构建上传版本接口地址
     * @param wrapper
     * @return
     */
    private static String buildUploadUrl(PolicySearchWrapper wrapper) {
        return String.format(buildUrl(wrapper.getPolicyConfig().getBackgroundUrl(), UPLOAD_URL_SUFFIX), getIp(), getDockerId());
    }

    /**
     * 拼接接口地址
     * @param prefix
     * @param suffix
     * @return
     */
    private static String buildUrl(String prefix, String suffix) {
        String url = StringUtils.trimToEmpty(prefix);
        if (!url.endsWith("/")) {
            url += "/";
        }
        return url + suffix;
    }

    /**
     * 获取Docker实例IP
     * @return
     */
    private static String getIp() {
        return StringUtils.defaultIfBlank(System.getenv(DOCKER_IP), IpHelper.getLocalIp());
    }

    /**
     * 获取Docker实例ID
     * @return
     */
    private static String getDockerId() {
        return StringUtils.defaultIfBlank(System.getenv(DOCKER_ID), "unknown");
    }
}
