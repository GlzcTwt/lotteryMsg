package com.lottery.print.send.sms;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class SmsSendClient {
	private static final Log log = LogFactory.getLog(SmsSendClient.class);
	private static final SmsSendClient sms = new SmsSendClient();
	private String SMS_SIGN = "【易通贷】";

	/**
	 * 地址
	 * 
	 * @author z @time 2013-9-5 上午11:04:01
	 */
	// private String serviceURL = "http://sdk2.zucp.net:8060/webservice.asmx";
	private String serviceURL = PropertiesLoader.getProperty("sms.url");
	/**
	 * 序列号
	 * 
	 * @author z @time 2013-9-5 上午11:04:03
	 */
	// private String sn = "SDK-BBX-010-17278";
	private String sn = PropertiesLoader.getProperty("sms.sn");
	/**
	 * 密码
	 * 
	 * @author z @time 2013-9-5 上午11:04:04
	 */
	// private String password = "604-76cd";
	private String password = PropertiesLoader.getProperty("sms.pwd");

	/**
	 * md5后的密码
	 * 
	 * @author z @time 2013-9-5 上午11:03:52
	 */
	private String pwd;

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
	private SmsSendClient() {
		map = new HashMap<String, String>();
		map.put("1", "没有需要取得的数据");
		map.put("-2", "帐号/密码不正确");
		map.put("-4", "余额不足");
		map.put("-5", "数据格式错误");
		map.put("-6", "没有需要取得的数据");
		map.put("-7", "没有需要取得的数据");
		map.put("-8", "没有需要取得的数据");
		map.put("-9", "没有需要取得的数据");
		map.put("-10", "没有需要取得的数据");
		map.put("-11", "没有需要取得的数据");
		map.put("-12", "没有需要取得的数据");
		map.put("-13", "没有需要取得的数据");
		map.put("-14", "没有需要取得的数据");
		map.put("-15", "没有需要取得的数据");
		map.put("-16", "没有需要取得的数据");
		map.put("-17", "没有需要取得的数据");
		map.put("-18", "没有需要取得的数据");

		try {
			this.pwd = this.getMD5(sn + password);
		} catch (UnsupportedEncodingException e) {
			log.error("md5加密异常", e);
		}
	}

	/**
	 * 获取短信客户端实例
	 * 
	 * @return
	 * @author z
	 * @time 2013-9-5 上午11:05:52
	 */
	public static SmsSendClient getInstance() {
		return sms;
	}

	/**
	 * getMD5 功 能：字符串MD5加密 参 数：待转换字符串 返 回 值：加密之后字符串
	 * 
	 * @param sourceStr
	 *            明文
	 * @return 密文
	 * @throws UnsupportedEncodingException
	 * @author z
	 * @time 2013-9-5 上午11:06:08
	 */
	public String getMD5(String sourceStr) throws UnsupportedEncodingException {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 注册，对应参数 省份，城市，行业，企业名称，联系人，电话，手机，电子邮箱，传真，地址，邮编
	 * 
	 * @param province
	 * @param city
	 * @param trade
	 * @param entname
	 * @param linkman
	 * @param phone
	 * @param mobile
	 * @param email
	 * @param fax
	 * @param address
	 * @param postcode
	 * @return 注册结果
	 * @author z
	 * @time 2013-9-5 上午11:07:11
	 */
	public String register(String province, String city, String trade,
			String entname, String linkman, String phone, String mobile,
			String email, String fax, String address, String postcode) {
		String result = "";
		String soapAction = "http://tempuri.org/Register";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">";
		xml += "<soap12:Body>";
		xml += "<Register xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + password + "</pwd>";
		xml += "<province>" + province + "</province>";
		xml += "<city>" + city + "</city>";
		xml += "<trade>" + trade + "</trade>";
		xml += "<entname>" + entname + "</entname>";
		xml += "<linkman>" + linkman + "</linkman>";
		xml += "<phone>" + phone + "</phone>";
		xml += "<mobile>" + mobile + "</mobile>";
		xml += "<email>" + email + "</email>";
		xml += "<fax>" + fax + "</fax>";
		xml += "<address>" + address + "</address>";
		xml += "<postcode>" + postcode + "</postcode>";
		xml += "</Register>";
		xml += "</soap12:Body>";
		xml += "</soap12:Envelope>";

		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes("GBK"));
			// bout.write(xml.getBytes("GBK"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length",
					String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(
					httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern
						.compile("<RegisterResult>(.*)</RegisterResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			in.close();
			return new String(result.getBytes(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 充值
	 * 
	 * @param cardno
	 *            充值卡号
	 * @param cardpwd
	 *            充值密码
	 * @return
	 * @author z
	 * @time 2013-9-5 上午11:07:56
	 */
	public String chargeFee(String cardno, String cardpwd) {
		String result = "";
		String soapAction = "http://tempuri.org/ChargUp";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">";
		xml += "<soap12:Body>";
		xml += "<ChargUp xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + password + "</pwd>";
		xml += "<cardno>" + cardno + "</cardno>";
		xml += "<cardpwd>" + cardpwd + "</cardpwd>";
		xml += "</ChargUp>";
		xml += "</soap12:Body>";
		xml += "</soap12:Envelope>";

		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes("GBK"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length",
					String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(
					httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern
						.compile("<ChargUpResult>(.*)</ChargUpResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			in.close();
			// return result;
			return new String(result.getBytes(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取余额
	 * 
	 * @return 余额
	 * @author z
	 * @time 2013-9-5 上午11:08:27
	 */
	public String getBalance() {
		String result = "";
		String soapAction = "http://tempuri.org/balance";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<balance xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwd + "</pwd>";
		xml += "</balance>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes("GBK"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length",
					String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(
					httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern
						.compile("<balanceResult>(.*)</balanceResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			in.close();
			return new String(result.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 添加签名，比如：【易通宝】
	 * 
	 * @param contents
	 *            短信内容，如果是多条以英文逗号分隔，例如：a,b,c
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
	 * @param ext
	 *            扩展码
	 * @param stime
	 *            定时时间
	 * @param rrid
	 *            唯一标识
	 * @return 唯一标识
	 * @author z
	 * @time 2013-9-5 上午11:08:48
	 */
	@SuppressWarnings("unchecked")
	public Map mt(String mobile, String content, String ext, String stime,
			String rrid) {
		Map retMap = new HashMap();
		content = addSign(content);
		String resultState = "-10";
		String result = "";
		String resultMess = "发送失败";
		String soapAction = "http://tempuri.org/mt";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mt xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwd + "</pwd>";
		xml += "<mobile>" + mobile + "</mobile>";
		xml += "<content>" + content + "</content>";
		xml += "<ext>" + ext + "</ext>";
		xml += "<stime>" + stime + "</stime>";
		xml += "<rrid>" + rrid + "</rrid>";
		xml += "</mt>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes("GBK"));
			// 如果您的系统是utf-8,这里请改成bout.write(xml.getBytes("GBK"));

			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length",
					String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(
					httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<mtResult>(.*)</mtResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			log.info("返回消息内容|" + result);
			if (result.startsWith("-") || "1".equals(result)) {
				if (map.get(result) != null)
					resultState = "-10";
					resultMess = map.get(result);
			} else {
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

	/**
	 * 接收短信
	 * 
	 * @return 接收到的信息
	 * @author z
	 * @time 2013-9-5 上午11:09:16
	 */
	public String mo() {
		String result = "";
		String soapAction = "http://tempuri.org/mo";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mo xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwd + "</pwd>";
		xml += "</mo>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes("GBK"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length",
					String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStream isr = httpconn.getInputStream();
			StringBuffer buff = new StringBuffer();
			byte[] byte_receive = new byte[10240];
			for (int i = 0; (i = isr.read(byte_receive)) != -1;) {
				buff.append(new String(byte_receive, 0, i));
			}
			isr.close();
			String result_before = buff.toString();
			int start = result_before.indexOf("<moResult>");
			int end = result_before.indexOf("</moResult>");
			result = result_before.substring(start + 10, end);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 发送个性短信 ，即给不同的手机号发送不同的内容，手机号和内容用英文的逗号对应好 如果不填写rrid将返回系统生成的
	 * 注意：群发时手机号码和内容用","隔开
	 * 
	 * @param mobile
	 *            手机号
	 * @param content
	 *            内容
	 * @param ext
	 *            扩展码
	 * @param stime
	 *            定时时间
	 * @param rrid
	 *            唯一标识
	 * @return 唯一标识
	 * @author z
	 * @time 2013-9-5 上午11:09:38
	 */
	public String gxmt(String mobile, String content, String ext, String stime,
			String rrid) {
		String info = null;
		if (null != content) {
			content = addSign(content.split(","));
			String result = "";
			String soapAction = "http://tempuri.org/gxmt";
			String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
			xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
			xml += "<soap:Body>";
			xml += "<gxmt xmlns=\"http://tempuri.org/\">";
			xml += "<sn>" + sn + "</sn>";
			xml += "<pwd>" + pwd + "</pwd>";
			xml += "<mobile>" + mobile + "</mobile>";
			xml += "<content>" + content + "</content>";
			xml += "<ext>" + ext + "</ext>";
			xml += "<stime>" + stime + "</stime>";
			xml += "<rrid>" + rrid + "</rrid>";
			xml += "</gxmt>";
			xml += "</soap:Body>";
			xml += "</soap:Envelope>";

			URL url;
			try {
				url = new URL(serviceURL);

				URLConnection connection = url.openConnection();
				HttpURLConnection httpconn = (HttpURLConnection) connection;
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				bout.write(xml.getBytes("GBK"));
				byte[] b = bout.toByteArray();
				httpconn.setRequestProperty("Content-Length",
						String.valueOf(b.length));
				httpconn.setRequestProperty("Content-Type",
						"text/xml; charset=gb2312");
				httpconn.setRequestProperty("SOAPAction", soapAction);
				httpconn.setRequestMethod("POST");
				httpconn.setDoInput(true);
				httpconn.setDoOutput(true);

				OutputStream out = httpconn.getOutputStream();
				out.write(b);
				out.close();

				InputStreamReader isr = new InputStreamReader(
						httpconn.getInputStream());
				BufferedReader in = new BufferedReader(isr);
				String inputLine;
				while (null != (inputLine = in.readLine())) {
					Pattern pattern = Pattern
							.compile("<gxmtResult>(.*)</gxmtResult>");
					Matcher matcher = pattern.matcher(inputLine);
					while (matcher.find()) {
						result = matcher.group(1);
					}
				}
				if (result.startsWith("-") || "1".equals(result)) {
					if (map.get(result) != null)
						result = map.get(result);
				} else {
					result = "成功";
				}

				return result;
			} catch (Exception e) {
				info = "发送短信异常";
				log.error(info, e);
				return info;
			}
		} else {
			info = "短信内容为空";
			log.error(info);
		}
		return info;
	}

	/**
	 * 注销
	 * 
	 * @return
	 * @author z
	 * @time 2013-9-5 上午11:10:19
	 */
	public String UnRegister() {
		String result = "";
		String soapAction = "http://tempuri.org/UnRegister";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">";
		xml += "<soap12:Body>";
		xml += "<UnRegister xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + password + "</pwd>";
		xml += "<cardno>" + sn + "</cardno>";
		xml += "<cardpwd>" + pwd + "</cardpwd>";
		xml += "</UnRegister>";
		xml += "</soap12:Body>";
		xml += "</soap12:Envelope>";
		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes("GBK"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length",
					String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=utf-8");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(
					httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern
						.compile("<UnRegisterResult>String</UnRegisterResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			in.close();
			return new String(result.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param newPwd
	 *            新密码
	 * @return
	 * @author z
	 * @time 2013-9-5 上午11:10:01
	 */
	public String UDPPwd(String newPwd) {
		String result = "";
		String soapAction = "http://tempuri.org/UDPPwd";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">";
		xml += "<soap12:Body>";
		xml += "<UDPPwd  xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + password + "</pwd>";
		xml += "<newpwd>" + newPwd + "</newpwd>";
		xml += "</UDPPwd>";
		xml += "</soap12:Body>";
		xml += "</soap12:Envelope>";

		URL url;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes("GBK"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length",
					String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(
					httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern
						.compile("<UDPPwdResult>(.*)</UDPPwdResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			in.close();
			return new String(result.getBytes(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getSMS_SIGN() {
		return SMS_SIGN;
	}

	public void setSMS_SIGN(String sMS_SIGN) {
		SMS_SIGN = sMS_SIGN;
	}

}
