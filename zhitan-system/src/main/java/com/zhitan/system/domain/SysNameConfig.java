package com.zhitan.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 系统名称配置对象 sys_name_config
 *
 * @author ZhiTan
 * @date 2024-11-05
 */
@TableName("sys_name_config")
public class SysNameConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String systemName;

    /** 左上角logo */
    @Excel(name = "左上角logo")
    private String leftLogo;

    /** 底部版权 */
    @Excel(name = "底部版权")
    private String copyRight;

    /** 主键 */
    private String id;

    /** 首页logo */
    @Excel(name = "首页logo")
    private String homeLogo;

        
    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }

    public String getSystemName()
    {
        return systemName;
    }
        
    public void setLeftLogo(String leftLogo)
    {
        this.leftLogo = leftLogo;
    }

    public String getLeftLogo()
    {
        return leftLogo;
    }
        
    public void setCopyRight(String copyRight)
    {
        this.copyRight = copyRight;
    }

    public String getCopyRight()
    {
        return copyRight;
    }
        
    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
        
    public void setHomeLogo(String homeLogo)
    {
        this.homeLogo = homeLogo;
    }

    public String getHomeLogo()
    {
        return homeLogo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("systemName", getSystemName())
            .append("leftLogo", getLeftLogo())
            .append("copyRight", getCopyRight())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("id", getId())
            .append("homeLogo", getHomeLogo())
        .toString();
    }
}
