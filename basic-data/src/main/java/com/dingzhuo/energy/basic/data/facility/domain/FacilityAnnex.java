package com.dingzhuo.energy.basic.data.facility.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 设备档案附件对象 facility_annex
 * 
 * @author sys
 * @date 2020-02-24
 */
public class FacilityAnnex extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private String id;

    /** 设备档案id */
    private String facilityId;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 文件后缀 */
    private String filesuffix;

    /** 文件路径 */
    private String filePath;

    /** 删除标志 */
    private String delFlage;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setFacilityId(String facilityId) 
    {
        this.facilityId = facilityId;
    }

    public String getFacilityId() 
    {
        return facilityId;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setFilesuffix(String filesuffix) 
    {
        this.filesuffix = filesuffix;
    }

    public String getFilesuffix() 
    {
        return filesuffix;
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
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("id", getId())
            .append("facilityId", getFacilityId())
            .append("fileName", getFileName())
            .append("filesuffix", getFilesuffix())
            .append("filePath", getFilePath())
            .append("delFlage", getDelFlage())
            .toString();
    }
}
