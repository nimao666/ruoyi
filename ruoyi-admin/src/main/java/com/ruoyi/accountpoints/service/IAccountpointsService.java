package com.ruoyi.accountpoints.service;

import java.util.List;
import com.ruoyi.accountpoints.domain.Accountpoints;

/**
 * 安卓活下去账号Service接口
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
public interface IAccountpointsService 
{
    /**
     * 查询安卓活下去账号
     * 
     * @param id 安卓活下去账号主键
     * @return 安卓活下去账号
     */
    public Accountpoints selectAccountpointsById(Long id);

    /**
     * 查询安卓活下去账号列表
     * 
     * @param accountpoints 安卓活下去账号
     * @return 安卓活下去账号集合
     */
    public List<Accountpoints> selectAccountpointsList(Accountpoints accountpoints);

    /**
     * 新增安卓活下去账号
     * 
     * @param accountpoints 安卓活下去账号
     * @return 结果
     */
    public int insertAccountpoints(Accountpoints accountpoints);

    /**
     * 修改安卓活下去账号
     * 
     * @param accountpoints 安卓活下去账号
     * @return 结果
     */
    public int updateAccountpoints(Accountpoints accountpoints);

    /**
     * 批量删除安卓活下去账号
     * 
     * @param ids 需要删除的安卓活下去账号主键集合
     * @return 结果
     */
    public int deleteAccountpointsByIds(Long[] ids);

    /**
     * 删除安卓活下去账号信息
     * 
     * @param id 安卓活下去账号主键
     * @return 结果
     */
    public int deleteAccountpointsById(Long id);
}
