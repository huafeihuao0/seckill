package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.KilledInfo;

/***
 *  【秒杀明细表DAO】
 * */
public interface KilledInfoDao
{
    //对秒杀明细表的一系列增删改查操作

    /**
     * 插入一条秒杀成功明细表
     *
     * @param productId 商品id
     * @param userPhone 用户手机信息
     * @return 是否插入成功 0表示失败
     */
    int insertKilledInfo(@Param("product_id") long productId, @Param("user_phone") long userPhone);

    /**
     * 根据商品id查询秒杀成功明细表
     *
     * @param productId
     */
    KilledInfo queryById(@Param("product_id") long productId, @Param("user_phone") long userPhone);

}
