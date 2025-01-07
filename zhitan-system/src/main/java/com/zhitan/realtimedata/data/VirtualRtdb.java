package com.zhitan.realtimedata.data;

import com.zhitan.common.enums.CollectionModes;
import com.zhitan.common.enums.Quality;
import com.zhitan.common.enums.RetrievalModes;
import com.zhitan.realtimedata.domain.TagValue;
import com.zhitan.realtimedata.service.RealtimeDatabase;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class VirtualRtdb implements RealtimeDatabase {

  /**
   * 关闭连接
   */
  @Override
  public void close() {

  }

  /**
   * 根据点位号获取实时数据
   *
   * @param tagCodes 点位号列表
   * @return
   */
  @Override
  public List<TagValue> retrieve(List<String> tagCodes) throws Exception {
    List<TagValue> tagValues = new ArrayList<>();
    Random random = new Random();
    tagCodes.forEach(tagCode -> {
      TagValue value = new TagValue();
      value.setTagCode(tagCode);
      value.setDataTime(DateTime.now().toDate());
      value.setQuality(Quality.GOOD);
      value.setValue(random.nextDouble() * 100);
      tagValues.add(value);
    });

    return tagValues;
  }

  /**
   * 根据点位号获取某一时刻的历史数据
   *
   * @param tagCodes 点位号集合
   * @param dataTime 历史时刻
   * @param timeCode 区分时间类型的time code
   * @return
   */
  @Override
  public List<TagValue> retrieve(List<String> tagCodes, Date dataTime,String timeCode) throws Exception {
    List<TagValue> tagValues = new ArrayList<>();
    Random random = new Random();
    tagCodes.forEach(tagCode -> {
      TagValue value = new TagValue();
      value.setTagCode(tagCode);
      value.setDataTime(dataTime);
      value.setQuality(Quality.GOOD);
      value.setValue(random.nextDouble() * 100);
      tagValues.add(value);
    });

    return tagValues;
  }

  /**
   * 根据查询方式获取一段时间内的历史数据
   *
   * @param tagCodes       点位号集合
   * @param beginTime      开始时间
   * @param endTime        结束时间
   * @param retrievalModes 查询方式（循环、拟合、全部）
   * @param pointCount     要查询的数据个数
   * @return
   */
  @Override
  public List<TagValue> retrieve(List<String> tagCodes, Date beginTime, Date endTime,
                                 RetrievalModes retrievalModes, int pointCount) throws Exception {
    List<TagValue> tagValues = new ArrayList<>();
    Random random = new Random();
    pointCount = retrievalModes == RetrievalModes.Full ? 200 : pointCount;
    int finalPointCount = pointCount;
    int span =
        Seconds.secondsBetween(new DateTime(beginTime), new DateTime(endTime)).getSeconds();
    int interval = span / finalPointCount;
    tagCodes.forEach(tagCode -> {
      for (int i = 1; i <= finalPointCount; i++) {
        TagValue value = new TagValue();
        value.setTagCode(tagCode);
        value.setDataTime(new DateTime(beginTime).plusSeconds(interval * i).toDate());
        value.setQuality(Quality.GOOD);
        value.setValue(random.nextDouble() * 100);
        tagValues.add(value);
      }
    });

    return tagValues;
  }

  /**
   * 汇总实时数据
   *
   * @param tagCodes        点位号集合
   * @param beginTime       开始时间
   * @param endTime         结束时间
   * @param collectionModes 汇总方式（最大值、最小值、平均值、求和等）
   * @return
   */
  @Override
  public List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime,
      CollectionModes collectionModes) throws Exception {
    List<TagValue> tagValues = new ArrayList<>();
    Random random = new Random();
    tagCodes.forEach(tagCode -> {
      TagValue value = new TagValue();
      value.setTagCode(tagCode);
      value.setDataTime(beginTime);
      value.setQuality(Quality.GOOD);
      value.setValue(random.nextDouble() * 1000);
      tagValues.add(value);
    });

    return tagValues;
  }

  /**
   * 插入实时数据
   *
   * @param tagValues 实时数据集合
   * @return
   */
  @Override
  public Boolean storeData(List<TagValue> tagValues) throws Exception {
    return true;
  }

  /**
   * 插入历史数据
   *
   * @param tagValues 历史数据集合
   * @return
   */
  @Override
  public Boolean insertData(List<TagValue> tagValues) throws Exception {
    return true;
  }

  /**
   * 打开连接
   *
   * @param host     实时数据库地址
   * @param port     端口号
   * @param userName 登录用户名
   * @param pwd      登录密码
   * @return 是否连接成功
   */
  @Override
  public boolean open(String host, int port, String userName, String pwd) throws Exception {
    return true;
  }
}
