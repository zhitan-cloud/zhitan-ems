package com.dingzhuo.energy.dataservice.service.impl;

import com.dingzhuo.energy.dataservice.data.RealtimeDatabaseManager;
import com.dingzhuo.energy.dataservice.domain.CollectionModes;
import com.dingzhuo.energy.dataservice.domain.RetrievalModes;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 实时数据库取数服务实现类.
 */
@Service
public class RealtimeDatabaseServiceImpl implements RealtimeDatabaseService {
    @Autowired
    private RealtimeDatabaseManager realtimeDatabaseManager;

    @Override
    public TagValue retrieve(String tagCode) {
        return realtimeDatabaseManager.retrieve(tagCode);
    }

    @Override
    public List<TagValue> retrieve(List<String> tagCodes) {
        return realtimeDatabaseManager.retrieve(tagCodes);
    }

    @Override
    public TagValue retrieve(String tagCode, Date dataTime, String timeCode) {
        return realtimeDatabaseManager.retrieve(tagCode, dataTime, timeCode);
    }

    @Override
    public List<TagValue> retrieve(List<String> tagCodes, Date dataTime, String timeCode) {
        return realtimeDatabaseManager.retrieve(tagCodes, dataTime, timeCode);
    }

    @Override
    public List<TagValue> retrieve(String tagCode, Date beginTime, Date endTime,
                                   RetrievalModes retrievalModes, int pointCount) {
        return realtimeDatabaseManager.retrieve(tagCode, beginTime, endTime, retrievalModes, pointCount);
    }

    @Override
    public List<TagValue> retrieve(List<String> tagCodes, Date beginTime, Date endTime,
                                   RetrievalModes retrievalModes, int pointCount) {
        return realtimeDatabaseManager.retrieve(tagCodes, beginTime, endTime, retrievalModes, pointCount);
    }

    @Override
    public TagValue statistics(String tagCode, Date beginTime, Date endTime,
                               CollectionModes collectionModes) {
        return realtimeDatabaseManager.statistics(tagCode, beginTime, endTime, collectionModes);
    }

    @Override
    public List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime,
                                     CollectionModes collectionModes) {
        return realtimeDatabaseManager.statistics(tagCodes, beginTime, endTime, collectionModes);
    }

    @Override
    public void storeData(List<TagValue> tagValues) {
        realtimeDatabaseManager.storeData(tagValues);
    }

    @Override
    public void insertData(List<TagValue> tagValues) {
        realtimeDatabaseManager.insertData(tagValues);
    }

}
