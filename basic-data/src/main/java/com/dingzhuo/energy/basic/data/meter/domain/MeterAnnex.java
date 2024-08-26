package com.dingzhuo.energy.basic.data.meter.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 计量器具档案附件对象 meter_annex
 * 
 * @author zhaowei
 * @date 2020-02-14
 */
public class MeterAnnex extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 关联meter_implement.id */
    private String implementId;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 文件后缀 */
    private String fileSuffix;

    /** 文件路径 */
    private String filePath;

    /** 删除标志Y 删除  N未删除 */
    private String delFlage;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setImplementId(String implementId) 
    {
        this.implementId = implementId;
    }

    public String getImplementId() 
    {
        return implementId;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setFileSuffix(String fileSuffix) 
    {
        this.fileSuffix = fileSuffix;
    }

    public String getFileSuffix() 
    {
        return fileSuffix;
    }
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }
    public void setDelFlage(String delFlage) 
    {
        this.delFlage = delFlage;
    }

    public String getDelFlage() 
    {
        return delFlage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("implementId", getImplementId())
            .append("fileName", getFileName())
            .append("fileSuffix", getFileSuffix())
            .append("filePath", getFilePath())
            .append("delFlage", getDelFlage())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
