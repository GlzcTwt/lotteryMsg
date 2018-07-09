package com.lottery.print.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesLoader {

	private static final Logger logger = Logger
			.getLogger(PropertiesLoader.class);

	private static final String resourcePath = "conf/config.properties";
	private static final Properties properties = new Properties();

	static {
		InputStream inputStream = null;
		try {
			logger.debug("加载配置文件...");
			inputStream = PropertiesLoader.class.getResourceAsStream("/"
					+ resourcePath);
			properties.load(inputStream);
		} catch (IOException e) {
			logger.error("配置文件加载失败!", e);
			throw new RuntimeException("配置文件加载失败!");
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
					logger.debug("加载关闭.");
				} catch (IOException e) {
					logger.error("输入流关闭失败!", e);
					throw new RuntimeException("输入流关闭失败!");
				}
		}
	}

	public static Properties getProperties() {
		return properties;
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		if (value != null) {
			return value;
		} else {
			return defaultValue;
		}
	}

	public static Integer getInteger(String key) {
		return Integer.valueOf(getProperty(key));
	}

	public static Integer getInteger(String key, Integer defaultValue) {
		return Integer.valueOf(getProperty(key, String.valueOf(defaultValue)));
	}

	public static Boolean getBoolean(String key) {
		return Boolean.valueOf(getProperty(key));
	}

	public static Boolean getBoolean(String key, boolean defaultValue) {
		return Boolean.valueOf(getProperty(key, String.valueOf(defaultValue)));
	}

}
