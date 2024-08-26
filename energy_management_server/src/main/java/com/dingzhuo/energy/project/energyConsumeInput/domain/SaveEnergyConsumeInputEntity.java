package com.dingzhuo.energy.project.energyConsumeInput.domain;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.Quality;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 保存能耗手动录入数据
 * @author: yxw
 * @date: 2022年03月17日 12:05
 */
@Data
public class SaveEnergyConsumeInputEntity {

    private String indexId;
    private String indexCode;
    private Date beginTime;
    private Date endTime;
    private Date dataTime;
    private String timeCode;
    private String timeType;
    private Double value;
    private Quality quality;
    private Date createTime;
    private Date updateTime;
    /**
     * 创建人
     */
    private String createBy;
}
