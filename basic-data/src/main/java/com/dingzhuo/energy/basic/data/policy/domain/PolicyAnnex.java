package com.dingzhuo.energy.basic.data.policy.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 政策法规附件对象 policy_annex
 * 
 * @author liuli
 * @date 2020-04-24
 */
public class PolicyAnnex extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件路径 */
    private String filePath;

    /** 删除标志Y删除 N为删除 */
    private String delFlage;

    /** 建立人 */
    private String createOperator;

    /** 修改人 */
    private String updateOperator;

    /** 主键 */
    private String id;

    /** 关联policy_regulations */
    private String regulationsId;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 文件后缀 */
    private String fileSuffix;

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
    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setRegulationsId(String regulationsId) 
    {
        this.regulationsId = regulationsId;
    }

    public String getRegulationsId() 
    {
        return regulationsId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("filePath", getFilePath())
            .append("delFlage", getDelFlage())
            .append("createTime", getCreateTime())
            .append("createOperator", getCreateOperator())
            .append("updateTime", getUpdateTime())
            .append("updateOperator", getUpdateOperator())
            .append("id", getId())
            .append("regulationsId", getRegulationsId())
            .append("fileName", getFileName())
            .append("fileSuffix", getFileSuffix())
            .toString();
    }
}
