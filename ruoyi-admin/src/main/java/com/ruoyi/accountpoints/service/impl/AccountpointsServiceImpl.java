package com.ruoyi.accountpoints.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.accountpoints.mapper.AccountpointsMapper;
import com.ruoyi.accountpoints.domain.Accountpoints;
import com.ruoyi.accountpoints.service.IAccountpointsService;

/**
 * 安卓活下去账号Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
@Service
public class AccountpointsServiceImpl implements IAccountpointsService 
{
    @Autowired
    private AccountpointsMapper accountpointsMapper;

    /**
     * 查询安卓活下去账号
     * 
     * @param id 安卓活下去账号主键
     * @return 安卓活下去账号
     */
    @Override
    public Accountpoints selectAccountpointsById(Long id)
    {
        return accountpointsMapper.selectAccountpointsById(id);
    }

    /**
     * 查询安卓活下去账号列表
     * 
     * @param accountpoints 安卓活下去账号
     * @return 安卓活下去账号
     */
    @Override
    public List<Accountpoints> selectAccountpointsList(Accountpoints accountpoints)
    {
        return accountpointsMapper.selectAccountpointsList(accountpoints);
    }

    /**
     * 新增安卓活下去账号
     * 
     * @param accountpoints 安卓活下去账号
     * @return 结果
     */
    @Override
    public int insertAccountpoints(Accountpoints accountpoints)
    {
        return accountpointsMapper.insertAccountpoints(accountpoints);
    }

    /**
     * 修改安卓活下去账号
     * 
     * @param accountpoints 安卓活下去账号
     * @return 结果
     */
    @Override
    public int updateAccountpoints(Accountpoints accountpoints)
    {
        return accountpointsMapper.updateAccountpoints(accountpoints);
    }

    /**
     * 批量删除安卓活下去账号
     * 
     * @param ids 需要删除的安卓活下去账号主键
     * @return 结果
     */
    @Override
    public int deleteAccountpointsByIds(Long[] ids)
    {
        return accountpointsMapper.deleteAccountpointsByIds(ids);
    }

    /**
     * 删除安卓活下去账号信息
     * 
     * @param id 安卓活下去账号主键
     * @return 结果
     */
    @Override
    public int deleteAccountpointsById(Long id)
    {
        return accountpointsMapper.deleteAccountpointsById(id);
    }
}
