package com.sojson.common.dao;

import com.sojson.common.model.TbPlayerMoney;
import com.sojson.common.model.vo.TbGainsInfoVo;
import com.sojson.common.model.vo.TbPlayerMoneyVo;

import java.util.List;
import java.util.Map;

public interface UTbPlayerMoneyMapper {

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatch(List<TbPlayerMoney> list);

    int updateByPrimaryKeySelective(TbPlayerMoney tbPlayerMoney);

    /**
     * 不分页查询
     * @param PlayerMoney
     * @return
     */
    List<TbPlayerMoneyVo> findPlayerMoney(Object PlayerMoney);

    /**
     * 添加
     * @param PlayerMoney
     * @return
     */
    int insert(Object PlayerMoney);

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * 查询总榜的内容
     */
    List<TbPlayerMoneyVo>  findTopForAll();

    /**
     * 查询月榜
     */
    List<TbPlayerMoneyVo>  findTopByMonth(Map<String,Object> param);

    List<TbPlayerMoneyVo>  findAll(Object param);
}