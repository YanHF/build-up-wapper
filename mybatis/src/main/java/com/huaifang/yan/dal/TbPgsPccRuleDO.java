package com.huaifang.yan.dal;



import java.util.Date;
import java.util.List;


/**
 * pgs识别运价适用pcc对象 tb_pgs_pcc_rule
 *
 * @author huaifang.yan
 * @date 2022-09-15
 */

public class TbPgsPccRuleDO {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 优先级
     */

    private Long level;

    /**
     * 航司
     */

    private List<String> airline;

    /**
     * 规则
     */

    private String rule;

    /**
     * tariff
     */

    private String tariff;

    /**
     * 运价始发type
     */

    private String depType;

    /**
     * 运价始发code
     */

    private String depCode;

    /**
     * 运价到达type
     */

    private String arrType;

    /**
     * 运价到达code
     */

    private String arrCode;

    /**
     * farebasis
     */

    private String farebasis;

    /**
     * 适用iata
     */

    private String applyIata;

    /**
     * 不适用iata
     */

    private String notApplyIata;

    /**
     * 适用pcc
     */

    private String applyPcc;

    /**
     * 不适用pcc
     */
    private String notApplyPcc;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 状态
     */
    private String stateDesc;

    /**
     * 环境
     */
    private String env;

    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 删除状态
     */
    private Integer delFlag;
    /**
     * 备注
     */
    private String remark;

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Long getLevel() {
        return level;
    }

    public void setAirline(List<String> airline) {
        this.airline = airline;
    }

    public List<String> getAirline() {
        return airline;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getTariff() {
        return tariff;
    }

    public void setDepType(String depType) {
        this.depType = depType;
    }

    public String getDepType() {
        return depType;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setArrType(String arrType) {
        this.arrType = arrType;
    }

    public String getArrType() {
        return arrType;
    }

    public void setArrCode(String arrCode) {
        this.arrCode = arrCode;
    }

    public String getArrCode() {
        return arrCode;
    }

    public void setFarebasis(String farebasis) {
        this.farebasis = farebasis;
    }

    public String getFarebasis() {
        return farebasis;
    }

    public void setApplyIata(String applyIata) {
        this.applyIata = applyIata;
    }

    public String getApplyIata() {
        return applyIata;
    }

    public void setNotApplyIata(String notApplyIata) {
        this.notApplyIata = notApplyIata;
    }

    public String getNotApplyIata() {
        return notApplyIata;
    }

    public void setApplyPcc(String applyPcc) {
        this.applyPcc = applyPcc;
    }

    public String getApplyPcc() {
        return applyPcc;
    }

    public void setNotApplyPcc(String notApplyPcc) {
        this.notApplyPcc = notApplyPcc;
    }

    public String getNotApplyPcc() {
        return notApplyPcc;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getEnv() {
        return env;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return null;
    }
}
