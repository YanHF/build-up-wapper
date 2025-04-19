package com.huaifang.yan.model;

import java.io.Serializable;

/**
 * SearchPolicyDTO
 *
 * @author xianyan.geng
 * @version SearchPolicyDTO, v 0.1 2020/10/26 14:05 xianyan.geng Exp $
 */
public class SearchPolicyDTO implements Serializable {
    private static final long serialVersionUID = -3745533359246277345L;
    private String            type;
    private Integer           model;
    private Long              revision;
    private String            searchType;
    private String            searchValue;
    private String            searchAll;
    private String            serializerType;
    private String            dockerId;
    private String            env;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getSearchAll() {
        return searchAll;
    }

    public void setSearchAll(String searchAll) {
        this.searchAll = searchAll;
    }

    public String getSerializerType() {
        return serializerType;
    }

    public void setSerializerType(String serializerType) {
        this.serializerType = serializerType;
    }

    public String getDockerId() {
        return dockerId;
    }

    public void setDockerId(String dockerId) {
        this.dockerId = dockerId;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
