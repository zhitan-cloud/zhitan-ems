package com.zhitan.alarm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.alarm.domain.AlarmItem;
import com.zhitan.alarm.mapper.AlarmItemMapper;
import com.zhitan.alarm.services.IAlarmItemService;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 预报警设置Service业务层处理
 *
 * @author sys
 * @date 2020-03-02
 */
@Service
public class AlarmItemServiceImpl implements IAlarmItemService {
    @Autowired
    private AlarmItemMapper alarmItemMapper;

    /**
     * 查询预报警设置
     *
     * @param id 预报警设置ID
     * @return 预报警设置
     */
    @Override
    public AlarmItem selectAlarmItemById(String id) {
        return alarmItemMapper.selectAlarmItemById(id);
    }

    /**
     * 查询预报警设置列表
     *
     * @param alarmItem 预报警设置
     * @return 预报警设置
     */
    @Override
    public List<AlarmItem> selectAlarmItemList(AlarmItem alarmItem) {
        return alarmItemMapper.selectAlarmItemList(alarmItem);
    }

    /**
     * 新增预报警设置
     *
     * @param alarmItem 预报警设置
     * @return 结果
     */
    @Override
    public int insertAlarmItem(AlarmItem alarmItem) {
        alarmItem.setCreateTime(DateUtils.getNowDate());
        return alarmItemMapper.insertAlarmItem(alarmItem);
    }

    /**
     * 修改预报警设置
     *
     * @param alarmItem 预报警设置
     * @return 结果
     */
    @Override
    public int updateAlarmItem(AlarmItem alarmItem) {
        alarmItem.setUpdateTime(DateUtils.getNowDate());
        return alarmItemMapper.updateAlarmItem(alarmItem);
    }

    /**
     * 修改预报警设置
     *
     * @param ids,flag 预报警设置
     * @return 结果
     */
    @Override
    public int updateStartStop(String[] ids, String flag, String username) {

        return alarmItemMapper.updateStartStop(ids, flag, username);
    }

    /**
     * 批量删除预报警设置
     *
     * @param ids 需要删除的预报警设置ID
     * @return 结果
     */
    @Override
    public int deleteAlarmItemByIds(String[] ids) {
        return alarmItemMapper.deleteAlarmItemByIds(ids);
    }

    /**
     * 删除预报警设置信息
     *
     * @param id 预报警设置ID
     * @return 结果
     */
    @Override
    public int deleteAlarmItemById(String id) {
        return alarmItemMapper.deleteAlarmItemById(id);
    }

    /**
     * 查询预报警设置
     *
     * @param id 预报警设置ID
     * @return 预报警设置
     */
    @Override
    public List<AlarmItem> selectAlarmItemtingById(String id) {
        return alarmItemMapper.selectAlarmItemtingById(id);
    }

    /**
     * 查询预报警设置
     *
     * @return 预报警设置
     */
    @Override
    public String getStartStop(String indexid) {
        return alarmItemMapper.getStartStop(indexid);
    }

    /**
     * 修改弹出框限值
     *
     * @param data 【修改弹出框限值】
     * @return 结果
     */
    @Override
    public int updateLimitVal(List data, String username) {
        String id = "";
        int num = 0;
        int dataNum = data.size();
        Map<String, Object> map = (Map<String, Object>) data.get(0);
        id = map.get("indexId").toString();
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map1 = (Map<String, Object>) data.get(i);
            String val = map1.get("limitVal").toString().trim();
            if ("".equals(val) || null == val) {
                data.remove(i);
                i--;
                num++;
            }
            map1.put("id", IdUtils.fastUUID());
            map1.put("alarmCode", map1.get("indexId").toString() + ":" + map1.get("timeSlotVal").toString() + ":" + map1.get("limitTypeVal").toString());
        }
        //查询条数
        int count = alarmItemMapper.selectCountById(id);
        if (num == dataNum) {//对比全空数据需要把数据库里id一致的全删除
            if (count > 0) {
                return alarmItemMapper.deleteAllLimitVal(id);
            } else {
                return 1;
            }
        } else {
            if (count > 0) {
                alarmItemMapper.deleteAllLimitVal(id);
                return alarmItemMapper.updateLimitValNoDel(data, id, username);
            } else {
                return alarmItemMapper.updateLimitValNoDel(data, id, username);
            }
        }
    }

    /**
     * 查询预报警设置
     *
     * @param id 预报警设置ID
     * @return 预报警设置
     */
    @Override
    public ArrayList getSettingCount(String[] id) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < id.length; i++) {
            int count = alarmItemMapper.getSettingCount(id[i]);
            if (count > 0) {
                list.add(count);
            } else {
                list.add(0);
            }
        }

        return list;
    }

    @Override
    public List<AlarmItem> getAllAlarmItem() {
        return alarmItemMapper.getAllAlarmItem();
    }

    @Override
    public Page<AlarmItem> selectAlarmItemPage(AlarmItem alarmItem, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<AlarmItem> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AlarmItem::getCreateTime);
        return alarmItemMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
    }
}
