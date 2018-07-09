/**	
 * Copyright (c) 2010-2014  Seaway I.T. Co, Ltd, All Rights Reserved.
 *		This source code contains CONFIDENTIAL INFORMATION and 
 *		TRADE SECRETS of Seaway I.T, Co, Ltd. ANY REPRODUCTION,
 * 		DISCLOSURE OR USE IN WHOLE OR IN PART is EXPRESSLY PROHIBITED, 
 * 		except as may be specifically authorized by prior written 
 *		agreement or permission by Seaway I.T, Co, Ltd.
 *
 *		THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE, The copyright 
 *		notice above does not evidence any actual or intended publication
 *		of such source code. 
 *
 * 项目名称(ProjectName)：mobilebank_mtp
 * 文件名称(FileName):StringUtil.java
 * @author：fanghua
 * @e_mail：fanghua@seaway.net.cn
 * @date：2014年6月24日上午10:20:41
 * --------------------修改历史-----------------------
 * Reporting Bugs:
 * Modification history:
 */
package com.lottery.print.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,6,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 
	 * @Title: isEmail
	 * @Description: 验证邮箱格式是否对
	 * @param: @param email
	 * @param: @return
	 * @return: boolean
	 * @throws
	 * @author fanghua
	 * @Date 2014年6月24日 上午10:25:14
	 */
	public static boolean isEmail(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	public static void main(String[] args) {
		System.out.println(isMobile("12312345678"));
		System.out.println(isPhone("87963066123"));
		System.out.println(isEmail("f_zhao_haibo@126.cn"));
	}
}
