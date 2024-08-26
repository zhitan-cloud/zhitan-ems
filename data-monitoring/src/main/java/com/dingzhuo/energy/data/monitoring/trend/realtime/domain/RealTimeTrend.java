package com.dingzhuo.energy.data.monitoring.trend.realtime.domain;

import com.dingzhuo.energy.data.model.domain.IndexType;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

public class RealTimeTrend  extends BaseEntity {

    private String nodeId;

    /**
     * 系统指标类型
     */
    private IndexType indexType;

    private int minute;

    private int count;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public void setIndexType(IndexType indexType) {
        this.indexType = indexType;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
