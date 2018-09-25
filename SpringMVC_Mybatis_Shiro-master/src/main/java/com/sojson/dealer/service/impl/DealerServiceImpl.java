package com.sojson.dealer.service.impl;

import com.sojson.common.IConstant;
import com.sojson.common.ResultMessage;
import com.sojson.common.dao.UTbDealerMapper;
import com.sojson.common.dao.UTbVipRecordMapper;
import com.sojson.common.model.TbDealer;
import com.sojson.common.model.UUser;
import com.sojson.common.model.vo.DealerCountVo;
import com.sojson.common.model.vo.VipRecordCount;
import com.sojson.common.utils.StringUtils;
import com.sojson.core.mybatis.BaseMybatisDao;
import com.sojson.core.mybatis.page.Pagination;
import com.sojson.core.shiro.token.manager.TokenManager;
import com.sojson.dealer.service.DealerService;
import com.sojson.user.manager.UserManager;
import com.sojson.user.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lx on 2018/9/4.
 */
@Service
public class DealerServiceImpl extends BaseMybatisDao<UTbDealerMapper> implements DealerService {

    @Autowired
    UTbDealerMapper uTbDealerMapper;

    @Autowired
    UTbVipRecordMapper uTbVipRecordMapper;

    @Resource
    UUserService userService;

    protected Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

    @SuppressWarnings("unchecked")
    @Override
    public Pagination<TbDealer> findByPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize) {
        return super.findPage(resultMap, pageNo, pageSize);
    }

    @Override
    public ResultMessage update(TbDealer entity) {
        //数据验证
        ResultMessage msg = beforeUpdateVaild(entity);
        if (msg.getLevel() == ResultMessage.MSG_LEVEL.SUCC.v) {
            entity.setModTime(new Date()); //设置修改时间
            uTbDealerMapper.update(entity);
        }
        return new ResultMessage(ResultMessage.MSG_LEVEL.SUCC.v);
    }

    @Transactional
    @Override
    public ResultMessage insert(TbDealer entity) {
        resultMap.put("status", 400);
        String email = entity.getLoginName();
        UUser user = userService.findUserByEmail(email);
        if(null != user){
            return new ResultMessage(ResultMessage.MSG_LEVEL.FAIL.v, "帐号|Email已经存在！");
        }

        UUser userEntity = new UUser();
        userEntity.setEmail(email);
        Date date = new Date();
        userEntity.setCreateTime(date);
        userEntity.setLastLoginTime(date);
        userEntity.setPswd("123456");
        userEntity.setNickname(entity.getName());
        //把密码md5
        userEntity = UserManager.md5Pswd(userEntity);
        //设置有效
        userEntity.setStatus(UUser._1);
        //新增user表数据
        userEntity = userService.insert(userEntity);
        //新增经销商
        entity.setUserId(userEntity.getId());
        entity.setDelFlag(TbDealer._0);
        entity.setCrtTime(date);

        String seatNum = "";
        if(entity.getParentId().equals("0")) {
            int count = uTbDealerMapper.queryDealerCount() + 1;
            seatNum = String.format("%03d", count);
        }else{
            seatNum = uTbDealerMapper.getSeatNumByUserId(entity.getParentId());
        }
        String inviteNum = seatNum + userEntity.getId();

        entity.setSeatNum(seatNum);
        entity.setInviteNum(inviteNum);
        uTbDealerMapper.insert(entity);
        userService.addRole2User(userEntity.getId(),entity.getRoleId());
        return new ResultMessage(ResultMessage.MSG_LEVEL.SUCC.v);
    }

    @Override
    public ResultMessage delete(String id) {
        uTbDealerMapper.delete(id);

        return new ResultMessage(ResultMessage.MSG_LEVEL.SUCC.v);
    }

    private ResultMessage beforeUpdateVaild(TbDealer entity){
        //修改ID验证
        if (StringUtils.isBlank(entity.getId())) {
            return new ResultMessage(ResultMessage.MSG_LEVEL.FAIL.v,"经销商信息错误！");
        }

        return new ResultMessage(ResultMessage.MSG_LEVEL.SUCC.v);
    }


    /**
     * 统计经销商的会员数
     * @param param
     * @return
     */
    @Override
    public List<DealerCountVo> countDealerVip(Map<String,Object> param){
        List<VipRecordCount> vipRecordCounts = uTbVipRecordMapper.countByCode(param);
        Map<String,Object> dealerParam = new HashMap<String,Object>();
        List<TbDealer> dealers = uTbDealerMapper.findAll(dealerParam);
        //key是员工邀请码，value是父类
        Map<String,String> dealerReal = new HashMap<String,String>();
        //将员工和经销商分类
        for(TbDealer tbDealer : dealers){
            if (StringUtils.isBlank(tbDealer.getParentId()) || IConstant.parentId.equals(tbDealer.getParentId())) {
                dealerReal.put(tbDealer.getInviteNum(),tbDealer.getId());
            } else {
                dealerReal.put(tbDealer.getInviteNum(),tbDealer.getParentId());
            }
        }
        List<DealerCountVo> dealerCounts = new ArrayList<DealerCountVo>();
        //获取统计信息
        for(TbDealer tbDealer : dealers){
            //统计经销商下的总额
            if (StringUtils.isBlank(tbDealer.getParentId()) || IConstant.parentId.equals(tbDealer.getParentId())) {
                DealerCountVo vo = new DealerCountVo();
                vo.setName(tbDealer.getName()); //名称
                //统计开通会员总额
                for (VipRecordCount vipRecordCount : vipRecordCounts){
                    if (dealerReal.get(vipRecordCount.getInvitaionCode()) != null
                            && dealerReal.get(vipRecordCount.getInvitaionCode()).equals(tbDealer.getInviteNum())) { //判断是否要归到经销商下
                        Integer money = vipRecordCount.getCountMoney();
                        if (vipRecordCount.getLevel().intValue() == IConstant.VIP_LEVEL.VIP_A.v) {
                            vo.setVipACount(vo.getVipACount() + vipRecordCount.getCountNum());
                            vo.setVipAMoneyCount(new BigDecimal(vo.getVipAMoneyCount() + money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() );
                        } else if (vipRecordCount.getLevel().intValue() == IConstant.VIP_LEVEL.VIP_B.v) {
                            vo.setVipBCount(vo.getVipBCount() + vipRecordCount.getCountNum());
                            vo.setVipBMoneyCount(new BigDecimal(vo.getVipBMoneyCount() + money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() );
                        } else if (vipRecordCount.getLevel().intValue() == IConstant.VIP_LEVEL.VIP_C.v) {
                            vo.setVipCCount(vo.getVipCCount() + vipRecordCount.getCountNum());
                            vo.setVipCMoneyCount(new BigDecimal(vo.getVipCMoneyCount() + money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() );
                        }
                        vo.setVipMoneyCount(new BigDecimal(vo.getVipMoneyCount() + money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                }
                dealerCounts.add(vo);
            }
        }

        return dealerCounts;
    }


    /**
     * 统计经销商的会员数
     * @param param
     * @return
     */
    @Override
    public List<DealerCountVo> countDealerVipById(Map<String,Object> param){
       TbDealer  tbDealer = uTbDealerMapper.findDealerByUserId(TokenManager.getUserId());
       param.put("dealerId",tbDealer.getId());
       List<VipRecordCount> vipRecordCounts = uTbVipRecordMapper.countByEmployee(param);

        //key是邀请码，value是具体的值
        Map<String, DealerCountVo> map = new HashMap<String, DealerCountVo>();
        List<DealerCountVo> list = new ArrayList<DealerCountVo>();
        for (VipRecordCount vipRecordCount : vipRecordCounts){
            Integer money = vipRecordCount.getCountMoney();
            if (map.get(vipRecordCount.getInvitaionCode()) == null) {
                map.put(vipRecordCount.getInvitaionCode(),new DealerCountVo());
            }
            DealerCountVo vo = map.get(vipRecordCount.getInvitaionCode());
            vo.setName(vipRecordCount.getName());
            if (vipRecordCount.getLevel().intValue() == IConstant.VIP_LEVEL.VIP_A.v) {
                vo.setVipACount(vo.getVipACount() + vipRecordCount.getCountNum());
                vo.setVipAMoneyCount(new BigDecimal(vo.getVipAMoneyCount() + money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() );
            } else if (vipRecordCount.getLevel().intValue() == IConstant.VIP_LEVEL.VIP_B.v) {
                vo.setVipBCount(vo.getVipBCount() + vipRecordCount.getCountNum());
                vo.setVipBMoneyCount(new BigDecimal(vo.getVipBMoneyCount() + money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() );
            } else if (vipRecordCount.getLevel().intValue() == IConstant.VIP_LEVEL.VIP_C.v) {
                vo.setVipCCount(vo.getVipCCount() + vipRecordCount.getCountNum());
                vo.setVipCMoneyCount(new BigDecimal(vo.getVipCMoneyCount() + money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() );
            }
            vo.setVipMoneyCount(new BigDecimal(vo.getVipMoneyCount() + money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        list.addAll(map.values());
        return list;
    }

}
