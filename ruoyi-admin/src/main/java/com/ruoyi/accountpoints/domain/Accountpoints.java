package com.ruoyi.accountpoints.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 安卓活下去账号对象 accountpoints
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
public class Accountpoints extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** playerId */
    @Excel(name = "playerId")
    private String playerId;

    /** 点数1 */
    @Excel(name = "点数1")
    private String advertCount1;

    /** 点数2 */
    @Excel(name = "点数2")
    private String advertCount2;

    /** 信用点 */
    @Excel(name = "信用点")
    private String noney;

    /** 统计 */
    @Excel(name = "统计")
    private Long sumpoints;

    /** 昵称 */
    @Excel(name = "昵称")
    private String name;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date utime;

    /** 是否出售 */
    @Excel(name = "是否出售")
    private String isBuy;

    /** 备用1 */
    private String by1;

    /** 备用2 */
    private String by2;

    /** 备用3 */
    private String by3;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPlayerId(String playerId) 
    {
        this.playerId = playerId;
    }

    public String getPlayerId() 
    {
        return playerId;
    }
    public void setAdvertCount1(String advertCount1) 
    {
        this.advertCount1 = advertCount1;
    }

    public String getAdvertCount1() 
    {
        return advertCount1;
    }
    public void setAdvertCount2(String advertCount2) 
    {
        this.advertCount2 = advertCount2;
    }

    public String getAdvertCount2() 
    {
        return advertCount2;
    }
    public void setNoney(String noney) 
    {
        this.noney = noney;
    }

    public String getNoney() 
    {
        return noney;
    }
    public void setSumpoints(Long sumpoints) 
    {
        this.sumpoints = sumpoints;
    }

    public Long getSumpoints() 
    {
        return sumpoints;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setUtime(Date utime) 
    {
        this.utime = utime;
    }

    public Date getUtime() 
    {
        return utime;
    }
    public void setIsBuy(String isBuy) 
    {
        this.isBuy = isBuy;
    }

    public String getIsBuy() 
    {
        return isBuy;
    }
    public void setBy1(String by1) 
    {
        this.by1 = by1;
    }

    public String getBy1() 
    {
        return by1;
    }
    public void setBy2(String by2) 
    {
        this.by2 = by2;
    }

    public String getBy2() 
    {
        return by2;
    }
    public void setBy3(String by3) 
    {
        this.by3 = by3;
    }

    public String getBy3() 
    {
        return by3;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("playerId", getPlayerId())
            .append("advertCount1", getAdvertCount1())
            .append("advertCount2", getAdvertCount2())
            .append("noney", getNoney())
            .append("sumpoints", getSumpoints())
            .append("name", getName())
            .append("password", getPassword())
            .append("utime", getUtime())
            .append("isBuy", getIsBuy())
            .append("by1", getBy1())
            .append("by2", getBy2())
            .append("by3", getBy3())
            .toString();
    }
}
