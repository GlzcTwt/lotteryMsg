package com.lottery.print.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by GLZC on 2017/2/20.
 * 数据源的Handler类
 */
public class DataSourceHandler {
    protected static Logger logger = LoggerFactory.getLogger(DataSourceHandler.class);

    /**
     * 数据源名称线程池
     */
    public static final ThreadLocal<String> holder = new ThreadLocal<String>();

    /**
     * 在项目启动的时候将配置的读、写数据源加到holder中
     *
     * @param dataSource
     */
    public static void putDataSource(String dataSource) {
        holder.set(dataSource);
    }

    /**
     * 从holder中获取数据源字符串
     *
     * @return
     */
    public static String getDateSource() {
        String s = holder.get();
//        logger.debug("从holder中获取数据源字符串:"+s);
        return s;
    }

}
