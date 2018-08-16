package com.j1weng.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.j1weng.entity.Seckill;

public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 如果影响行数>1，表示更新库存的记录行数
     */
    int reduceNumber(@Param("seckillId")Long seckillId, @Param("killTime")Date killTime);

    /**
     * 根据id查询秒杀的商品信息
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param off
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("outset")int outset,@Param("limit")int limit);
    /**
     * 使用存储过程执行秒杀
     * @param parmMap
     */
    void killByProcedure(@Param("paramMap")Map<String,Object> paramMap);

}