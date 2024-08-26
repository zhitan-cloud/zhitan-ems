package com.dingzhuo.energy.dataservice.data.influxdb;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBMapper;
import org.joda.time.DateTime;
import org.joda.time.Duration;

public class FastDB {

    private Logger logger = LogManager.getLogger(FastDB.class);
    private InfluxDB influxDB;
    private static final String TABLE_NAME = "daq";

    private String dataBaseName;
    private String host;
    private int port;

    public FastDB(String host, int port, String dataBaseName) {
        this.dataBaseName = dataBaseName;
        this.host = host;
        this.port = port;
        try {
            this.open();
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    private static InfluxDBMapper resultMapper;

    /**
     * 打开连接
     */
    private void open() {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            influxDB = InfluxDBFactory.connect("http://" + this.host + ":" + this.port, builder);
            influxDB
                    .enableBatch(BatchOptions.DEFAULTS.jitterDuration(500).actions(2000).flushDuration(100));
            influxDB.enableGzip();
            influxDB.ping();
            resultMapper = new InfluxDBMapper(influxDB);
            logger.info("实时库连接成功！");
        } catch (Exception ex) {
            logger.fatal("实时库连接失败！" + ex);
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        influxDB.close();
    }

    /**
     * 根据点位号获取实时数据
     *
     * @param tagCodes 点位号列表
     * @returnS
     */
    public List<RtdbResult> snapShot(List<String> tagCodes) {
        final String sql = "SELECT LAST(value) as value, quality FROM %s WHERE %s and " +
                "time<=(now()) and time>=(now()-20m) GROUP BY code";
        List<List<String>> newList = Lists.partition(tagCodes, 10);
        List<ListenableFuture<QueryResult>> taskList = new ArrayList<>();
        ListeningExecutorService service =
                MoreExecutors.listeningDecorator(customerThreadPool());
        for (List<String> tags : newList) {
            ListenableFuture<QueryResult> futureTask = service.submit(() -> {
                StringBuilder sb = new StringBuilder();
                String codes = formatCode(tags);
                sb.append(String.format(sql,
                        TABLE_NAME, codes));
                return builderResultValue(sb.toString());
            });
            taskList.add(futureTask);
        }
        List<RtdbResult> results = buildResult(taskList);
        service.shutdown();
        return results;
    }

    private ExecutorService customerThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("fast-db-thread-pool-%d").build();
        return new ThreadPoolExecutor(10, 50,
                30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 根据点位号获取某一时刻的历史数据
     *
     * @param tagCodes 点位号集合
     * @param dataTime 历史时刻
     * @param timeCode 区分时间类型的time code
     * @return List<RtdbResult>
     */
    public List<RtdbResult> getDataByTime(List<String> tagCodes, Date dataTime, String timeCode) {
        DateTime begin = new DateTime(dataTime).plusDays(-1);;
//        if (timeCode.startsWith("M")) {
//            begin = new DateTime(dataTime).plusMonths(-1);
//        } else if (timeCode.startsWith("Y")) {
//            begin = new DateTime(dataTime).plusYears(-1);
//        } else {
//            begin = new DateTime(dataTime).plusDays(-1);
//        }
        DateTime end = new DateTime(dataTime);
//        logger.error("开始计算数据，timeCode=" + timeCode + "开始时间=" + begin.toString("yyyy-MM-dd HH:mm:ss") + "结束时间=" + end.toString("yyyy-MM-dd HH:mm:ss"));
        final String sql =
                "SELECT LAST(value) as value, quality FROM %s WHERE %s AND time >=%dms AND " +
                        "time<=%dms GROUP BY code";

        List<List<String>> newList = Lists.partition(tagCodes, 10);
        List<ListenableFuture<QueryResult>> taskList = new ArrayList<>();
        ListeningExecutorService service =
                MoreExecutors.listeningDecorator(customerThreadPool());
        for (List<String> tags : newList) {
            ListenableFuture<QueryResult> futureTask = service.submit(() -> {
                StringBuilder sb = new StringBuilder();
                String codes = formatCode(tags);
                sb.append(String.format(sql,
                        TABLE_NAME, codes, begin.toDate().getTime(), end.toDate().getTime() + 999));
                return builderResultValue(sb.toString());
            });
            taskList.add(futureTask);
        }
        List<RtdbResult> results = buildResult(taskList);
        service.shutdown();
        return results;
    }

    /**
     * 根据查询方式获取一段时间内的历史数据
     *
     * @param tagCodes   点位号集合
     * @param beginTime  开始时间
     * @param endTime    结束时间
     * @param queryType  查询方式（循环、拟合、全部）
     * @param pointCount 要查询的数据个数
     * @return
     */
    public List<RtdbResult> getHistoryData(List<String> tagCodes, Date beginTime, Date endTime,
                                           QueryType queryType, int pointCount) {
        DateTime begin = new DateTime(beginTime);
        DateTime end = new DateTime(endTime);
        long millis = new Duration(begin, end).getMillis();
        List<List<String>> newList = Lists.partition(tagCodes, 10);
        List<ListenableFuture<QueryResult>> taskList = new ArrayList<>();

        ListeningExecutorService service =
                MoreExecutors.listeningDecorator(customerThreadPool());

        final String sqlFull =
                "select value,quality FROM %s WHERE %s AND time >=%dms AND time <=%dms " +
                        "GROUP BY code";
        final String sql = "select %s(value) as value, quality FROM %s WHERE %s AND time >=%dms " +
                "AND" +
                " time <=%dms GROUP BY code,time(%dms) fill(previous)";
        for (List<String> tags : newList) {
            ListenableFuture<QueryResult> futureTask = service.submit(() -> {
                StringBuilder sb = new StringBuilder();
                String codes = formatCode(tags);
                if (queryType != QueryType.FULL && pointCount > 0) {
                    long interval = millis / pointCount;
                    sb.append(String.format(sql, queryType.name(),
                            TABLE_NAME, codes, begin.toDate().getTime(), end.toDate().getTime() + 999, interval));
                } else {
                    sb.append(String.format(sqlFull,
                            TABLE_NAME, codes, begin.toDate().getTime(), end.toDate().getTime() + 999));
                }

                return builderResultValue(sb.toString());
            });
            taskList.add(futureTask);
        }
        List<RtdbResult> results = buildResult(taskList);
        service.shutdown();
        return results;
    }

    /**
     * 汇总实时数据
     *
     * @param tagCodes  点位号集合
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param queryType 汇总方式（最大值、最小值、平均值、求和等）
     * @return
     */
    public List<RtdbResult> statistics(List<String> tagCodes, Date beginTime, Date endTime,
                                       QueryType queryType) throws ExecutionException, InterruptedException {
        if (queryType == QueryType.FULL) {
            return new ArrayList<>();
        }
        DateTime begin = new DateTime(beginTime);
        DateTime end = new DateTime(endTime);
        List<List<String>> newList = Lists.partition(tagCodes, 10);
        List<ListenableFuture<QueryResult>> taskList = new ArrayList<>();
        ListeningExecutorService service =
                MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(50));

        final String sql = "select %s(value) as value, quality FROM FROM %s WHERE %s AND time >=%dms " +
                "AND" +
                " time <=%dms GROUP BY code fill(previous)";
        for (List<String> tags : newList) {
            ListenableFuture<QueryResult> futureTask = service.submit(() -> {
                StringBuilder sb = new StringBuilder();
                String codes = formatCode(tags);
                sb.append(String.format(sql, queryType.name(),
                        TABLE_NAME, codes, begin.toDate().getTime(), end.toDate().getTime() + 999));
                return builderResultValue(sb.toString());
            });
            taskList.add(futureTask);
        }
        List<RtdbResult> results = buildResult(taskList);
        service.shutdown();
        return results;
    }

    /**
     * 插入实时数据
     *
     * @param tagValues 实时数据集合
     * @return
     */
    public Boolean storeData(List<RtdbResult> tagValues) {
        Instant now = Instant.now();
        for (RtdbResult tagValue : tagValues) {
            tagValue.setTime(now);
        }
        return saveData(tagValues);
    }

    /**
     * 插入历史数据
     *
     * @param tagValues 历史数据集合
     * @return
     */
    public Boolean insertData(List<RtdbResult> tagValues) {
        return saveData(tagValues);
    }


    /**
     * 删除测点
     *
     * @param tagCode 点位号
     * @return
     */
    public Boolean deleteTag(String tagCode) {
        return false;
    }

    private Boolean saveData(List<RtdbResult> tagValues) {
        BatchPoints.Builder builder = BatchPoints
                .database(dataBaseName)
                .retentionPolicy("autogen").precision(TimeUnit.SECONDS);
        tagValues.forEach(tagValue -> {
            Point point = Point
                    .measurement(TABLE_NAME)
                    .addField("value", tagValue.getValue())
                    .addField("quality", tagValue.getQuality())
                    .tag("code", tagValue.getTagCode())
                    .time(tagValue.getTime().getEpochSecond(), TimeUnit.SECONDS).build();
            builder.point(point);
        });
        influxDB.write(builder.build());
        return true;
    }

    private QueryResult builderResultValue(String sql) {
        try {
//            logger.error("计算数据的SQL是" + sql);
            Query query = new Query(sql, dataBaseName);
            return influxDB.query(query);
        } catch (Throwable throwable) {
            return new QueryResult();
        }
    }

    private List<RtdbResult> buildResult(List<ListenableFuture<QueryResult>> taskList) {
        List<RtdbResult> results = new ArrayList<>();

        try {
            for (QueryResult queryResult : Futures.successfulAsList(taskList).get()) {
                try {
                    synchronized (this) {
                        if (queryResult != null) {
                            List<RtdbResult> rtdbResults = resultMapper.toPOJO(queryResult, RtdbResult.class);
                            results.addAll(rtdbResults);
                        }
                    }
                } catch (Throwable throwable) {
                }
            }
        } catch (InterruptedException | ExecutionException e) {
        }

        return results;
    }

    private String formatCode(List<String> codeList) {
        return "(code='" + String.join("' or code='", codeList) + "')";
    }
}
