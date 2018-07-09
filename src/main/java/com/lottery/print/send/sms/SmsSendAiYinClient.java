package com.lottery.print.send.sms;

import com.alibaba.fastjson.JSONObject;
import com.lottery.print.util.PropertiesLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 爱音短信渠道
 */
public class SmsSendAiYinClient {

	private static final Log log = LogFactory.getLog(SmsSendAiYinClient.class);
	private static final SmsSendAiYinClient sms = new SmsSendAiYinClient();
	private String SMS_SIGN = PropertiesLoader.getProperty("sms.aiyin.sign");;
	private final static String CHARSETSTR = "gbk";

	/**
	 * @return the sMS_SIGN
	 */
	public String getSMS_SIGN() {
		return SMS_SIGN;
	}

	/**
	 * @param sMS_SIGN the sMS_SIGN to set
	 */
	public void setSMS_SIGN(String sMS_SIGN) {
		SMS_SIGN = sMS_SIGN;
	}

	/**
	 * 地址
	 */
	private String serviceURL = PropertiesLoader.getProperty("sms.aiyin.sendUrl");

	/**
	 * @return the serviceURL
	 */
	public String getServiceURL() {
		return serviceURL;
	}

	/**
	 * @param serviceURL the serviceURL to set
	 */
	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 *  账户ID
	 */
	private String username = PropertiesLoader.getProperty("sms.aiyin.username");

	/**
	 * 账户密码
	 */
	private String pwd = PropertiesLoader.getProperty("sms.aiyin.pwd");
	/**
	 * 业务代码
	 */
	private String serviceCode = PropertiesLoader.getProperty("sms.baiw.serviceCode");

	/**
	 * 实例
	 */
	public static SmsSendAiYinClient getInstance() {
		return sms;
	}

	/**
	 * 返回值
	 */

	public static Map<String, String> RESPONSMAP = new HashMap<String, String>();

	static {
		RESPONSMAP.put("100", "提交成功");
		RESPONSMAP.put("101", "参数错误");
		RESPONSMAP.put("102", "号码错误");
		RESPONSMAP.put("103", "当日余量不足");
		RESPONSMAP.put("104", "请求超时");
		RESPONSMAP.put("105", "用户余量不足");
		RESPONSMAP.put("106", "非法用户");
		RESPONSMAP.put("107", "提交号码超限");
		RESPONSMAP.put("111", "签名不合法");
		RESPONSMAP.put("120", "内容长度超长，请不要超过500个字");
		RESPONSMAP.put("121", "内容中有屏蔽词");
		RESPONSMAP.put("131", "IP非法");
		RESPONSMAP.put("fail", "未定义异常");
	}


	/**
	 * 发送短信
	 * TODO
	 * @param mobileNumber	手机号码
	 * @param content	发送短信内容
	 * @return 
	 * <br>----------------------------------------------------变更记录--------------------------------------------------
	 * <br> <br> 序号      |            时间                         |   作者       |         描述       
	 * <br>  0     |  2015-1-14 上午11:34:54  |  lxj    |    创建
	 */
	public Map<String, String> sendMessage(String mobileNumber,String content){
		log.info("===爱音发送短信开始了");
		content = SMS_SIGN+ content;
		Map<String, String> retMap = new HashMap<String, String>();
		if (null==mobileNumber||"".equals(mobileNumber)) {
			retMap.put("state", "3");
			return retMap;
		}
		OutputStream out = null;
		try {
			// 用UTF-8编码执行URLEncode
			content = java.net.URLEncoder.encode(content, CHARSETSTR);
			String _param = null;
			URL url = null;
			url = new URL(serviceURL);
			HttpURLConnection urlConn = null;
            /*
             * POST数据格式：pwd= 665e8568e26144bbfe34687ffd57dc &username=test
			 * &p=15000000000
			 * ,15111111111,15222222222,15333333333,15444444444,15555555555
			 * ,15666666666,15777777777,15888888888,15999999999 &msg=本次短信的发送内容
			 */
			_param = "pwd=" + pwd + "&username=" + username + "&p=" + mobileNumber + "&msg=" + content;
			url = new URL(serviceURL);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setRequestMethod("POST");
			urlConn.setDoInput(true);
			// Post 请求不能使用缓存
			urlConn.setUseCaches(false);
			urlConn.setInstanceFollowRedirects(false);
			out = urlConn.getOutputStream();
			out.write(_param.getBytes(CHARSETSTR));
			out.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), CHARSETSTR));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			rd.close();
			String json = sb.toString();
			log.info("爱英短信接口返回数据：" + json);
			JSONObject jsonObject = JSONObject.parseObject(json);
			String status = jsonObject.getString("status");
			String count = jsonObject.getString("count");
			log.info("---end---爱音发送短信结果content|"+content+",mobile|"+mobileNumber+",result|"+RESPONSMAP.get(status)+" count|"+count);
			if("100".equals(status)){
				retMap.put("state", "0");
				retMap.put("resultMess", "发送成功");
				retMap.put("extOrderId", Long.toString(System.currentTimeMillis()));
			}else
				retMap.put("state", "3");
			return retMap;
		} catch (Exception e) {
			log.error("发送信息失败:" + e.getMessage() + " moblie:" + mobileNumber + ",msg:" + content, e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

				}
			}
		}
		log.info("===爱音发送短信结束了 "+content);
		return retMap;
	}
	
	public static void main(String[] args) {
	SmsSendAiYinClient aiyin = SmsSendAiYinClient.getInstance();
	System.out.println(aiyin.sendMessage("13658595659",
			"恭喜您，您可凭借此验证码:3323"));
    }
}
