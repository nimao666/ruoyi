package com.ruoyi.accountpoints.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.accountpoints.domain.Accountpoints;
import com.ruoyi.accountpoints.service.IAccountpointsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 安卓活下去账号Controller
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
@RestController
@RequestMapping("/accountpoints/accountpoints")
public class AccountpointsController extends BaseController
{
    @Autowired
    private IAccountpointsService accountpointsService;

    /**
     * 查询安卓活下去账号列表
     */
    @PreAuthorize("@ss.hasPermi('accountpoints:accountpoints:list')")
    @GetMapping("/list")
    public TableDataInfo list(Accountpoints accountpoints)
    {
        startPage();
        List<Accountpoints> list = accountpointsService.selectAccountpointsList(accountpoints);
        return getDataTable(list);
    }

    /**
     * 导出安卓活下去账号列表
     */
    @PreAuthorize("@ss.hasPermi('accountpoints:accountpoints:export')")
    @Log(title = "安卓活下去账号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Accountpoints accountpoints)
    {
        List<Accountpoints> list = accountpointsService.selectAccountpointsList(accountpoints);
        ExcelUtil<Accountpoints> util = new ExcelUtil<Accountpoints>(Accountpoints.class);
        util.exportExcel(response, list, "安卓活下去账号数据");
    }

    /**
     * 获取安卓活下去账号详细信息
     */
    @PreAuthorize("@ss.hasPermi('accountpoints:accountpoints:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(accountpointsService.selectAccountpointsById(id));
    }

    /**
     * 新增安卓活下去账号
     */
    @PreAuthorize("@ss.hasPermi('accountpoints:accountpoints:add')")
    @Log(title = "安卓活下去账号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Accountpoints accountpoints)
    {
        return toAjax(accountpointsService.insertAccountpoints(accountpoints));
    }

    /**
     * 修改安卓活下去账号
     */
    @PreAuthorize("@ss.hasPermi('accountpoints:accountpoints:edit')")
    @Log(title = "安卓活下去账号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Accountpoints accountpoints)
    {
        return toAjax(accountpointsService.updateAccountpoints(accountpoints));
    }

    /**
     * 删除安卓活下去账号
     */
    @PreAuthorize("@ss.hasPermi('accountpoints:accountpoints:remove')")
    @Log(title = "安卓活下去账号", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(accountpointsService.deleteAccountpointsByIds(ids));
    }
}
