package com.dingzhuo.energy.project.electricity.domain;

import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import com.dingzhuo.energy.common.utils.time.TimeType;
import java.util.Date;

/**
 *
 * @author sys
 * @date 2020-02-18
 */
public class statistics{
    private static final long serialVersionUID = 1L;
    private String nodeId;
    private String nodeName;
    private String eierarchyFlag;
    private TimeType TimeType;
    private String dictType;
    private Date beginTime;
    private Date endTime;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getEierarchyFlag() {
        return eierarchyFlag;
    }

    public void setEierarchyFlag(String eierarchyFlag) {
        this.eierarchyFlag = eierarchyFlag;
    }

    public TimeType getTimeType() {
        return TimeType;
    }

    public void setTimeType(TimeType timeType) {
        TimeType = timeType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }
}