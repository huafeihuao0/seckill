package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Repo;

import java.util.Date;
import java.util.List;

/***
 *  【仓库DAO】
 * */
public interface RepoDao
{
    /**
     * 根据商品id进行减库存，不用指定减多少库存，因为默认是每次只能秒杀一件，是更新操作
     *
     * @param productId 商品id
     * @param killTime  秒杀成功的时t间，也就是表中对应的create_time
     * @return 是否执行成功 0表示失败
     */
    int reduceNum(@Param("product_id") long productId, @Param("kill_time") Date killTime);

    /**
     * 根据商品id查询库存信息
     *
     * @param productId 商品id
     * @return 库存信息
     */
    Repo queryById(@Param("product_id") long productId);

    /**
     * 根据偏移量查询库存信息列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Repo> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
