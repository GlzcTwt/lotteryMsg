/**
 * 
 */
package com.lottery.print.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 * 公用方法类
 * 
 * @date Jun 23, 20108:09:12 PM
 */
public class FunctionUtil {

	private static final Logger logger = Logger.getLogger(FunctionUtil.class);

	// 写文件
	public static boolean WriteFile(String fileName, String StrBuf)
			throws IOException {
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(StrBuf, 0, StrBuf.length());
		bw.newLine();
		bw.close();
		fw.close();
		return true;
	}

	/**
	 * 手机号码验证
	 * 
	 * @param str
	 *            传入的字串
	 * @return ture 通过 false 不通过
	 */
	public static boolean checkMobile(String str) {
		boolean tem = false;
		String reg = "1[358][0-9]{9}";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		tem = matcher.matches();
		return tem;
	}

	/**
	 * 检查EMAIL 地址结尾必须是以com|cn|com|cn|net|org|gov|gov.cn|edu|edu.cn结尾
	 * 
	 * @param str
	 *            传入的字串
	 * @return ture or false
	 **/
	public static boolean checkEmail(String str) {
		boolean tem = false;
		String reg = "\\w+\\@\\w+\\.(com|cn|com.cn|net|org|gov|gov.cn|edu|edu.cn)";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		tem = matcher.matches();
		return tem;
	}

	/**
	 * 验证数据
	 * 
	 * @param webRequestData
	 *            :被验证的数据
	 * @return 非空:true 空 false
	 */
	public static boolean isNull(String webRequestData) {
		return (null != webRequestData && !webRequestData.equals("null") && !webRequestData
				.equals(""));
	}

	/**
	 * 验证数据
	 * 
	 * @param webRequestData
	 *            :被验证的数据
	 * @return 非空:true 空 false
	 */
	public static boolean checkNull(String webRequestData) {
		return (null != webRequestData && !webRequestData.equals("null"));
	}

	public static String getRandomStr(int count) {
		StringBuffer charStr = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			charStr.append(i);
		}
		for (int i = 65; i < 91; i++) {
			charStr.append((char) i);
		}
		for (int i = 97; i < 123; i++) {
			charStr.append((char) i);
		}

		String chars = charStr.toString();
		char[] rands = new char[count];
		for (int i = 0; i < count; i++) {
			int rand = (int) (Math.random() * charStr.length());
			rands[i] = chars.charAt(rand);
		}
		return String.valueOf(rands);
	}

	public static String getRandomStrForNumber(int count) {
		StringBuffer charStr = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			charStr.append(i);
		}

		String chars = charStr.toString();
		char[] rands = new char[count];
		for (int i = 0; i < count; i++) {
			int rand = (int) (Math.random() * charStr.length());
			rands[i] = chars.charAt(rand);
		}
		return String.valueOf(rands);
	}

	public static String getRandomStrReplace(int count, String oldChar,
			String newChar) {
		String str = getRandomStr(count);
		str.replace(oldChar, newChar);
		return str;
	}

	public static String fmtNumber(Double nub) {
		DecimalFormat df = new DecimalFormat("####.00");
		return df.format(nub);
	}

	/**
	 * 
	 * 签名加密方法
	 * 
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getEncryptionStr(String content) {
		logger.info("签名前字符串: " + content);
		String ret = null;
		try {
			ret = DigestUtils.md5Hex(content.getBytes("UTF-8")).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("签名后字符串: " + ret);
		return ret;
	}

	/**
	 * 
	 * 签名加密方法
	 * 
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSupEncryptionStr(String content) {
		logger.info("签名前字符串: " + content);
		String ret = null;
		try {
			// ret = DigestUtils.md5Hex(content).toUpperCase();
			ret = DigestUtils.md5Hex(content.getBytes("GBK")).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("签名后字符串: " + ret);
		return ret;
	}

	/**
	 * 
	 * 签名加密方法
	 * 
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSupEncryptionStrJcard(String content, String key) {
		logger.info("签名前字符串: " + content);
		logger.info("签名前KEY: " + key);
		StringBuffer sbf = new StringBuffer();
		sbf.append(content).append("|||").append(key);
		String ret = null;
		try {

			logger.info("签名前字符串: " + sbf.toString());
			ret = DigestUtils.md5Hex(sbf.toString().getBytes("GBK"))
					.toLowerCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("签名后字符串: " + ret);
		return ret;
	}

	/**
	 * 
	 * 签名加密方法
	 * 
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getEncryptionStrByUpperCase(String content) {
		content = content.trim();
		logger.info("签名前字符串: " + content);
		String ret = null;
		try {
			ret = DigestUtils.md5Hex(
					(URLEncoder.encode(content, "utf-8").toUpperCase())
							.toUpperCase()).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("签名后字符串: " + ret);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static Map getMap(Map con) {
		Map myMap = new LinkedHashMap();
		Set keys = con.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			if (key == null)
				continue;
			String value = "";
			if (con.get(key) instanceof String) {
				value = (String) con.get(key);
			}
			if (con.get(key) instanceof String[]) {
				String[] values = (String[]) con.get(key);
				value = values[0];
			}
			if (value == null)
				value = "";
			myMap.put(key, value);
		}
		return myMap;
	}

	public static void main(String[] args) {

		String ss = "agent_id=1372988&bill_id=10001-20120507205457-9785774&time_stamp=2012122156";
		// String ss = "bargainor_id=37053&sp_billno=20001-20110720233340&"
		// + "pay_type=c&ka_number=cs30e0009723208&ka_password=28515552&"
		// + "key=8b6cb67a2c15ce910f774ca68183988d";

		System.out.println(getSupEncryptionStrJcard(ss,
				"56d3555fe431920fdc5b047e46308d73"));
	}
}
