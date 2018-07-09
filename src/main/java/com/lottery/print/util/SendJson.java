package com.lottery.print.util;

/**********************************************************************
 * FILE                ：SendJson.java
 * PACKAGE			   ：com.seaway.mobilebank.utils
 * AUTHOR              ：leo
 * DATE				   ：2012-8-9 下午5:41:42
 * FUNCTION            ：
 *
 * 杭州思伟版权所有
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|  DATE    | NAME           | REASON            | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |          | leo        | Created           |
 *
 * DESCRIPTION:
 *
 ***********************************************************************/

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.lottery.print.model.RetsultEntity;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * -------------------------------修订历史-------------------------- 修改人：leo
 * 修改时间：2012-8-9 下午5:41:42 修改备注：
 * 
 * @version：
 */
public class SendJson {
	private static final Logger logger = Logger.getLogger(SendJson.class);

	public static void sendJson(HttpServletResponse response, RetsultEntity ret) {
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(JSON.toJSONString(ret));
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error("发送json数据失败", e);
		}
	}

	public static void sendJson(HttpServletResponse response, String message) {
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(message);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error("发送json数据失败", e);
		}
	}
}
