package com.lottery.print.send.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lottery.print.util.PropertiesLoader;

/**
 * 短信客户端，这是一个单例的实现，参数信息也是硬编码的，属于临时解决文案，后期需要改造成一个独立的服务 我们所有的Demo，都是在GBK环境下测试的。
 * 调用注册方法可能不成功。 java.io.IOException: Server returned HTTP response code: 400 for
 * URL: http://sdk105.entinfo.cn/webservice.asmx。 如果出现上述400错误，请参考第102行。
 * 如果您的系统是utf-8，收到的短信可能是乱码，请参考第290行
 * 
 * @Type SmsClient
 * @author z
 * @date 2013-9-5
 * @Version V1.0
 */
public class SmsSend56Client {
	private static final Log log = LogFactory.getLog(SmsSend56Client.class);
	private static final SmsSend56Client sms = new SmsSend56Client();
	private String SMS_SIGN = "【易通贷】";

	/**
	 * 地址
	 */
	private String serviceURL = PropertiesLoader.getProperty("sms.56.url");
	/**
	 * 用户名
	 */
	private String sn = PropertiesLoader.getProperty("sms.56.sn");
	/**
	 * 密码
	 */
	private String password = PropertiesLoader.getProperty("sms.56.pwd");
	/**
	 * 企业ID
	 */
	private String comid = PropertiesLoader.getProperty("sms.56.comid");
	/**
	 * 所用平台
	 */
	private String smsnumber = PropertiesLoader.getProperty("sms.56.smsnumber");

	/**
	 * 返回值定义
	 * 
	 * @author z @time 2013-9-5 上午11:03:33
	 */
	private Map<String, String> map;

	/**
	 * 构造函数
	 * 
	 * @author z
	 * @time 2013-9-5 上午11:05:41
	 */
	private SmsSend56Client() {
		map = new HashMap<String, String>();
		map.put("1", "发送成功");
		map.put("-1", "手机号码不正确");
		map.put("-2", "除时间外，所有参数不能为空");
		map.put("-3", "用户名密码不正确");
		map.put("-4", "平台不存在");
		map.put("-5", "客户短信数量为0");
		map.put("-6", "客户账户余额小于要发送的条数");
		map.put("-7", "不能超过70个字");
		map.put("-8", "非法短信内容");
		map.put("-9", "未知系统故障");
		map.put("-10", "网络性错误");

	}

	/**
	 * 获取短信客户端实例
	 * 
	 * @return
	 * @author z
	 * @time 2013-9-5 上午11:05:52
	 */
	public static SmsSend56Client getInstance() {
		return sms;
	}

	/**
	 * 添加签名，比如：【易通宝】
	 * 
	 * @param contents
	 *             短信内容，如果是多条以英文逗号分隔，例如：a,b,c
	 * @return a【易通宝】,b【易通宝】,c【易通宝】
	 * @author z
	 * @time 2013-9-5 上午11:34:04
	 */
	private String addSign(String... contents) {
		StringBuffer sb = new StringBuffer();
		if (null != contents && contents.length > 0) {
			boolean first = true;
			for (String c : contents) {
				if (null != c && c.trim().length() > 0) {
					if (!first) {
						sb.append(",");
					}
					sb.append(c.trim()).append(SMS_SIGN);
					first = false;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 发送（内容统一，单个或者多个手机号码的）短信 ,传多个手机号就是群发，一个手机号就是单条提交 如果不填写rrid将返回系统生成的 注
	 * 意：群发时手机号码用","隔开
	 * 
	 * @param mobile
	 *            手机号
	 * @param content
	 *            内容
	 * @param stime
	 *            定时时间
	 * @param rrid
	 *            唯一标识
	 * @return 唯一标识
	 * @author z
	 * @time 2013-9-5 上午11:08:48
	 */
	public Map<String, String> mt(String mobile, String content, String ext,
			String stime, String rrid) {
		Map<String, String> retMap = new HashMap<String, String>();
		String resultState = "-10";
		String result = "";
		String resultMess = "发送失败";
		String str = "";
		URL url;
		try {
			content = java.net.URLEncoder.encode(addSign(content), "gbk");
			StringBuffer strBuff = new StringBuffer();
			strBuff.append("comid=").append(comid).append("&");
			strBuff.append("username=").append(sn).append("&");
			strBuff.append("userpwd=").append(password).append("&");
			strBuff.append("handtel=").append(mobile).append("&");
			strBuff.append("sendcontent=").append(content).append("&");
			strBuff.append("sendtime=").append(stime).append("&");
			strBuff.append("smsnumber=").append(smsnumber);

			String strUrl = serviceURL + "?" + strBuff.toString();
			log.info("56短信发送内容mobile|" + mobile + ",strUrl|" + strUrl);

			url = new URL(strUrl);
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);

			((HttpURLConnection) connection).setRequestMethod("GET");
			HttpURLConnection httpConnection = (HttpURLConnection) connection;

			int resultBoolean = httpConnection.getResponseCode();
			log.info("56短信发送返回状态mobile|" + mobile + ",resultBoolean|"
					+ resultBoolean);
			if (resultBoolean == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), "GBK"));
				char[] cbuf = new char[31 * 1024];
				int len = 0;
				while ((len = in.read(cbuf, 0, 1024 * 31)) != -1) {
					str += new String(cbuf, 0, len);
				}
				in.close();
				if (str != null)
					str = str.trim();// 去空格
				result = str;
			}

			log.info("返回消息内容mobile|" + mobile + ",result|" + result);
			if (result.startsWith("-")) {
				if (map.get(result) != null)
					resultState = "-10";
				resultMess = map.get(result);
			} else if ("1".equals(result)) {
				resultState = "0";
				resultMess = "发送成功";
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		retMap.put("state", resultState);
		retMap.put("extOrderId", result);
		retMap.put("resultMess", resultMess);
		return retMap;
	}

	public String getSMS_SIGN() {
		return SMS_SIGN;
	}

	public void setSMS_SIGN(String sMS_SIGN) {
		SMS_SIGN = sMS_SIGN;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getComid() {
		return comid;
	}

	public void setComid(String comid) {
		this.comid = comid;
	}

	public String getSmsnumber() {
		return smsnumber;
	}

	public void setSmsnumber(String smsnumber) {
		this.smsnumber = smsnumber;
	}

}
