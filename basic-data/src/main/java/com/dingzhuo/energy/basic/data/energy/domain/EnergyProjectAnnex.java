package com.dingzhuo.energy.basic.data.energy.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 节能项目管理附件对象 energy_project_annex
 * 
 * @author sys
 * @date 2020-12-08
 */
public class EnergyProjectAnnex extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 关联关联energy_project */
    @Excel(name = "关联energy_project")
    private String projectId;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 文件后缀 */
    @Excel(name = "文件后缀")
    private String fileSuffix;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    /** 删除标志Y删除 N为删除 */
    @Excel(name = "删除标志Y删除 N为删除")
    private String delFlage;

    /** 建立人 */
    @Excel(name = "建立人")
    private String createOperator;

    /** 修改人 */
    @Excel(name = "修改人")
    private String updateOperator;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setProjectId(String projectId) 
    {
        this.projectId = projectId;
    }

    public String getProjectId() 
    {
        return projectId;
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
    public void setCreateOperator(String createOperator) 
    {
        this.createOperator = createOperator;
    }

    public String getCreateOperator() 
    {
        return createOperator;
    }
    public void setUpdateOperator(String updateOperator) 
    {
        this.updateOperator = updateOperator;
    }

    public String getUpdateOperator() 
    {
        return updateOperator;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectId", getProjectId())
            .append("fileName", getFileName())
            .append("fileSuffix", getFileSuffix())
            .append("filePath", getFilePath())
            .append("delFlage", getDelFlage())
            .append("createTime", getCreateTime())
            .append("createOperator", getCreateOperator())
            .append("updateTime", getUpdateTime())
            .append("updateOperator", getUpdateOperator())
            .toString();
    }
}
