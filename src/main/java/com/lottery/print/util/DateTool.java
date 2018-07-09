package com.lottery.print.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTool {
	private int weeks = 0;

	// 默认转换出为2005-09-08类型的当前日
	public static String getToday() {
		Date dt = new java.util.Date();
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(dt);
	}

	public static String parseDates(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return sdf.format(dt);
	}

	public static String parseDates2(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(dt);
	}

	public static String parseDates3(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		return sdf.format(dt);
	}

	/**
	 * 时间处理
	 * 
	 * @param dt
	 * @return "yyyy-MM-dd"格式
	 */
	public static String parseDate(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(dt);
	}

	public static String parseDategk(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMM");
		return sdf.format(dt);
	}

	public static Date strintToDate(String ds) throws ParseException {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Date d = sdf.parse(ds);
		return d;
	}

	public static Date strintTotime() {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(DateTool.getTime("HH:mm:ss"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}

	public static Date strintToDatetime(String ds) throws ParseException {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date d = sdf.parse(ds);
		return d;
	}

	public static String getDayAfter(Date d, int dayAfter)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DAY_OF_MONTH, dayAfter);
		Date dt = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		return DateTool.parseDate(dt);
	}

	public static String getminuteAfter(Date d, int dayAfter)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MINUTE, dayAfter);
		Date dt = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
		return DateTool.parseDates(dt);
	}

	public static Date getminuteAfters(Date d, int dayAfter)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MINUTE, dayAfter);
		Date dt = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
		return dt;
	}

	public static String getDayAfterMonth(Date d, int monthAfter) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MONTH, monthAfter);
		Date dt = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		return DateTool.parseDate(dt);
	}

	public static String getDayAfterMonthforgk(Date d, int monthAfter) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MONTH, monthAfter);
		Date dt = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		return DateTool.parseDategk(dt);
	}

	public static String getFirstDayOfMonth(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DAY_OF_MONTH, 1);
		Date dt = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		return DateTool.parseDate(dt);
	}

	/**
	 * 获取本月月末时间 add by zhoujunwei 2011 7 22
	 * 
	 * @param d
	 * @return
	 */
	public static String getEndDayOfMonth(Date d) {
		Calendar localTime = Calendar.getInstance();
		String strY = null;
		String strZ = null;
		boolean leap = false;
		int x = localTime.get(Calendar.YEAR);
		int y = localTime.get(Calendar.MONTH) + 1;
		if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10
				|| y == 12) {
			strZ = "31";
		}
		if (y == 4 || y == 6 || y == 9 || y == 11) {
			strZ = "30";
		}
		if (y == 2) {
			leap = leapYear(x);
			if (leap) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		return x + "-" + strY + "-" + strZ;

	}

	/**
	 * 功能：判断输入年份是否为闰年<br>
	 * add by zhoujunwei
	 * 
	 * @param year
	 * @return 是：true 否：false
	 * @author pure
	 */
	public static boolean leapYear(int year) {
		boolean leap;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0)
					leap = true;
				else
					leap = false;
			} else
				leap = true;
		} else
			leap = false;
		return leap;
	}

	// 根据传入的type进行转换日期，type必须遵循Date转换的规则
	public static String getTime(String type) {
		Date dt = new java.util.Date();
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(type);
		return sdf.format(dt);
	}

	public static int getDay(String d) {
		int day = 1;
		if (d.length() >= 10) {
			day = Integer.parseInt(d.substring(8, 10));
		}
		return day;
	}

	public static int getMonth(String d) {
		int m = 1;
		if (d.length() >= 10) {
			m = Integer.parseInt(d.substring(5, 7));
		}
		return m;
	}

	public static int getYear(String d) {
		int y = 1;
		if (d.length() >= 10) {
			y = Integer.parseInt(d.substring(0, 4));
		}
		return y;
	}

	// 根据传入的两个日期计算相差秒数
	public static long getDaySecond(Date c1, Date c2) {
		long iReturn = 0;
		if (c1 != null && c2 != null) {
			iReturn = (long) ((c2.getTime() - c1.getTime()) / 1000);
		}
		return iReturn;
	}

	// 根据传入的两个日期计算相差小时
	public static long getDayHous(Date c1, Date c2) {
		long iReturn = 0;
		if (c1 != null && c2 != null) {
			iReturn = (long) ((c2.getTime() - c1.getTime()) / (60 * 60 * 1000));
		}
		return iReturn;
	}

	// 根据传入的两个日期计算相差天数
	public static int getDayBetween(Calendar c1, Calendar c2) {
		int iReturn = 0;
		if (c1 != null && c2 != null) {
			iReturn = (int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / (24 * 60 * 60 * 1000));
		}
		return iReturn;
	}

	// 根据传入的两个日期计算相差年数
	public static int getYearBetween(Calendar c1, Calendar c2) {
		int iReturn = 0;
		if (c1 != null && c2 != null) {
			iReturn = (int) (getDayBetween(c1, c2) / 365);
		}
		return iReturn;
	}

	/**
	 * 把2005-02-5这样的日期格式转化为Calendar类型
	 * 
	 * @param stringTo
	 * @return
	 * @throws ParseException
	 */
	public static Calendar stringToCalendar(String stringTo)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		if (stringTo != null) {
			SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(stringTo);
			c.setTime(d);
		}
		return c;
	}

	/**
	 * 把2005年02月5日12:12:12这样的日期格式转化为Calendar类型
	 * 
	 * @param stringTo
	 * @return
	 * @throws ParseException
	 */
	public static Calendar StringToCalendar(String stringTo)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		if (stringTo != null) {
			SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy年MM月dd日HH:mm:ss");
			Date d = sdf.parse(stringTo);
			c.setTime(d);
		}
		return c;
	}

	public static String getNextMonday(String s) throws ParseException {
		Calendar c = DateTool.stringToCalendar(s);
		return (DateTool.getDayAfter(DateTool.strintToDate(s),
				7 - c.get(Calendar.DAY_OF_WEEK) + 2));
	}

	public static String getNextSunday(String s) throws ParseException {
		Calendar c = DateTool.stringToCalendar(s);
		return (DateTool.getDayAfter(DateTool.strintToDate(s),
				7 - c.get(Calendar.DAY_OF_WEEK) + 2 + 7));
	}

	public static String getThisSaturday(String s) throws ParseException {
		Calendar c = DateTool.stringToCalendar(s);
		return (DateTool.getDayAfter(DateTool.strintToDate(s),
				7 - c.get(Calendar.DAY_OF_WEEK)));
	}

	public static String getWeekOfYear(String s) throws ParseException {
		Calendar c = DateTool.stringToCalendar(s);
		return String.valueOf(c.get(Calendar.WEEK_OF_YEAR));

	}

	public static String getNow() {
		return DateTool.getTime("yyyy-MM-dd HH:mm:ss");
	}

	public static String getNowTime() {
		return DateTool.getTime("HH:mm:ss");
	}

	// 获得本周的第一天
	public static String getMondayOFWeek() throws ParseException {
		int mondayPlus = DateTool.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	private static int getMondayPlus() throws ParseException {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	// 获得本周的最后一天
	public static String getCurrentWeekday() throws ParseException {
		int mondayPlus = DateTool.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期一的日期
	public static String getPreviousWeekday() throws ParseException {
		int weeks = 0;
		weeks--;
		int mondayPlus = DateTool.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期日的日期
	public static String getPreviousWeekSunday() throws ParseException {
		int weeks = 0;
		weeks--;
		int mondayPlus = DateTool.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 判断是否润年
	 * 
	 * @param ddate
	 * @return
	 */
	public static boolean isLeapYear(String ddate) {
		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		 * 3.能被4整除同时能被100整除则不是闰年
		 */
		Date d = new Date();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

	/**
	 * 获取一个月的最后一天
	 * 
	 * @param dat
	 * @return
	 */

	public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
		String str = dat.substring(0, 8);
		String month = dat.substring(5, 7);
		int mon = Integer.parseInt(month);
		if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8
				|| mon == 10 || mon == 12) {
			str += "31";
		} else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
			str += "30";
		} else {
			if (isLeapYear(dat)) {
				str += "29";
			} else {
				str += "28";
			}
		}
		return str;
	}

	// 获取当月第一天
	public static String getFirstDayOfMonth() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 计算当月最后一天,返回字符串
	public static String getDefaultDay() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 计算前一天,返回字符串
	public static String getForWardDay() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.DATE, -1);// 减去一天

		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 日期计算处理方法 接受参数比如：200912，200901 返回值为201001，200902
	 * 
	 * @param date
	 * @return
	 */
	public long dateaccount(long date) {
		long d = date;
		String dateTemp = Long.toString(d);
		String yearStr = dateTemp.substring(0, 4);
		String monthStr = dateTemp.substring(4, dateTemp.length());
		long year = Long.parseLong(yearStr);
		long month = Long.parseLong(monthStr);
		if (month == 12) {
			yearStr = Long.toString(year + 1);
			monthStr = "01";
			dateTemp = yearStr + monthStr;
			d = Long.parseLong(dateTemp);
		} else {
			d++;
		}
		return d;
	}
}