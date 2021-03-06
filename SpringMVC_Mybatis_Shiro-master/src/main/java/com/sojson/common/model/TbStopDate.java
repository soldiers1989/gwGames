package com.sojson.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class TbStopDate implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 停止标示（0：未停止，1：已停止）
     */
    private Byte stopFlag = 0;

    /**
     * '开始时间'
     */
    private Date bgnTime;

    /**
     * '结束时间'
     */
    private Date endTime;
    /**
     * '操作人员'
     */
    private Long userId;

    /**
     * '操作人员姓名'
     */
    private String userName;

    /**
     * '创建时间'
     */
    private Date crtTime;


    /**
     * 审核标致（0：待审核，1：审核通过，2：审核不通过）
     */
    private Byte auditFlag;


    /**
     * '审核员姓名'
     */
    private String auditUserName;

    /**
     * '审核时间'
     */
    private Date auditTime;

    /**
     * 备注
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(Byte stopFlag) {
        this.stopFlag = stopFlag;
    }

    public Date getBgnTime() {
        return bgnTime;
    }

    public void setBgnTime(Date bgnTime) {
        this.bgnTime = bgnTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public Byte getAuditFlag() {
        return auditFlag;
    }

    public void setAuditFlag(Byte auditFlag) {
        this.auditFlag = auditFlag;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
