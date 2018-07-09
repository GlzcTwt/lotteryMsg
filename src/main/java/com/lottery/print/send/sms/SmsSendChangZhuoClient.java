package com.lottery.print.send.sms;

import com.lottery.print.util.PropertiesLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 畅卓短信渠道
 */
public class SmsSendChangZhuoClient {

	private static final Log log = LogFactory.getLog(SmsSendChangZhuoClient.class);
	private static final SmsSendChangZhuoClient sms = new SmsSendChangZhuoClient();
	private String SMS_SIGN = "【金云彩】";
	private final static String CHARSET = "utf-8";
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
	private String serviceURL = PropertiesLoader.getProperty("sms.changzhuo.sendUrl");

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



	/**
	 *  账户ID
	 */
	private String username = PropertiesLoader.getProperty("sms.changzhuo.username");

	/**
	 * 账户密码
	 */
	private String pwd = PropertiesLoader.getProperty("sms.changzhuo.pwd");

	/**
	 * 实例
	 */
	public static SmsSendChangZhuoClient getInstance() {
		return sms;
	}

	/**
	 * 返回值
	 */

	public static Map<String, String> RESPONSMAP = new HashMap<String, String>();

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
		log.info("畅卓发送短信开始了===================");
		content = SMS_SIGN+ content;
		Map<String, String> returnMap = new HashMap();
		if (null==mobileNumber||"".equals(mobileNumber)) {
			returnMap.put("state", "3");
			return returnMap;
		}
		try {

			// 用UTF-8编码执行URLEncode
			content = java.net.URLEncoder.encode(content, CHARSET);
			String _param = null;

			URL url = null;
			HttpURLConnection urlConn = null;
			_param = "action=send&account=" + username
					+ "&password=" + pwd + "&mobile=" + mobileNumber
					+ "&content=" + content;
			url = new URL(serviceURL);

			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setRequestMethod("POST");
			// Post 请求不能使用缓存
			urlConn.setUseCaches(false);
			// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
			urlConn.setInstanceFollowRedirects(false);
			OutputStream out = urlConn.getOutputStream();
			out.write(_param.getBytes(CHARSET));
			out.flush();
			out.close();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream(), "UTF-8"));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			rd.close();

			String xml = sb.toString();
			System.out.println("================result xml ="+xml);
			log.debug("================result xml ="+xml);
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			String result = root.selectSingleNode("returnstatus").getText();
			log.info("---end---畅卓发送短信结果content|"+content+",mobile|"+mobileNumber+",result|"+result);
			if ("Success".equals(result)) {
				returnMap.put("state", "0");
				returnMap.put("resultMess", "发送成功");
				returnMap.put("extOrderId", Long.toString(System.currentTimeMillis()));
			}else {
				returnMap.put("state", "3");
			}
			return returnMap;
		} catch (Exception e) {
			returnMap.put("state", "3");
			returnMap.put("resultMess", "发送失败");
			log.error("发送信息失败" + e.getMessage());
		}
		log.info("畅卓发送短信结束了 "+content);
		return returnMap;
	}
	
	public static void main(String[] args) {
	SmsSendChangZhuoClient changzhuo  = SmsSendChangZhuoClient.getInstance();
	System.out.println(changzhuo.sendMessage("15201368252",
			"恭喜您，您可凭借此验证码:564557"));
    }
}
