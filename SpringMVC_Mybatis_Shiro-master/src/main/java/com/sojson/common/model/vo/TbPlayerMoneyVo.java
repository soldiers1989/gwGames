package com.sojson.common.model.vo;

import com.sojson.common.IConstant;
import com.sojson.common.model.TbPlayerMoney;

/**
 * 选手收益展示信息
 */
public class TbPlayerMoneyVo extends TbPlayerMoney {

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 参赛者名称
     */
    private String name;

    /**
     * 交易时间格式
     */
    private String businessTimeStr;


    /**
     * 本金
     */
    private String capital;

    /**
     * 是否关注 1已关注 0未关注
     */
    private byte isFollow = IConstant.YES_OR_NO.NO.v;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessTimeStr() {
        return businessTimeStr;
    }

    public void setBusinessTimeStr(String businessTimeStr) {
        this.businessTimeStr = businessTimeStr;
    }


    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public byte getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(byte isFollow) {
        this.isFollow = isFollow;
    }
}
