package com.lottery.print.send.sms;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lottery.print.send.sms.emayWebserviceClient.SDKClient;
import com.lottery.print.send.sms.emayWebserviceClient.SDKClientProxy;
import com.lottery.print.util.PropertiesLoader;

/**
 * 亿美短信发送客户端
 * 
 * @author Administrator
 * 
 */
public class SmsSendEmayClient {
	private static final Log log = LogFactory.getLog(SmsSendBaiwClient.class);
	private static final SmsSendEmayClient emay = new SmsSendEmayClient();

	/**
	 * 亿美序列号
	 */
	private String serialNo = PropertiesLoader.getProperty("sms.emay.serialNo");

	/**
	 * 亿美key值
	 */
	private String key = PropertiesLoader.getProperty("sms.emay.key");

	/**
	 * 实例
	 */
	public static SmsSendEmayClient getInstance() {
		return emay;
	}

	public Map<String, String> sendMessage(String mobileNumber, String content) {
		Map<String, String> retMap = new HashMap<String, String>();
		String resultMess = "";
		SDKClient emayClient = new SDKClientProxy();
		log.info("---start---亿美发送短信内容content|" + content + ",mobile|"
				+ mobileNumber);
		try {
			resultMess = String.valueOf(emayClient.sendSMS(serialNo, key, "",
					new String[] { mobileNumber }, content, "", "", 5, 0L));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		log.info("---end---亿美发送短信结果content|" + content + ",mobile|"
				+ mobileNumber + ",result|" + resultMess);
		if (resultMess.startsWith("0")) {
			retMap.put("state", "0");
			retMap.put("resultMess", "发送成功");
			retMap.put("extOrderId",  Long.toString(System.currentTimeMillis()));
		} else {
			retMap.put("state", "3");
		}
		return retMap;
	}

	public static void main(String[] args) {
		// SDKClient emayClient = new SDKClientProxy();
		// try {
		// int s = emayClient.sendSMS("8SDK-EMY-6699-RDTRT", "etd20151116", "",
		// new String[] { "13520159812" }, "您好，您的验证码为5632", "", "", 5, 0L);
		// System.out.println(s);
		// } catch (RemoteException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(SmsSendEmayClient.getInstance().sendMessage("13520159812",
		// "您好，您的验证码为7892"));
		SmsSendEmayClient client = getInstance();
		Map<String, String>  map = client.sendMessage("13520159812", "您好，您的验证码为5632");
        System.out.println(map.get("resultMess"));
	}
}
