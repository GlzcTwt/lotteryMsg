package com.lottery.print.send.sms;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lottery.print.util.DateTool;
import com.lottery.print.util.PropertiesLoader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.lottery.print.util.MD5Encode;

public class SmsVoiceBaiWuClient {
	    private static final SmsVoiceBaiWuClient sms = new SmsVoiceBaiWuClient();
	    
	    private static final Log log = LogFactory.getLog(SmsVoiceBaiWuClient.class);
		/**
		 * 地址
		 */
		private String serviceURL = PropertiesLoader.getProperty("sms.baiw.voice.url");
		
		/**
		 * 账户序列号
		 */
		private String accountSid = PropertiesLoader.getProperty("sms.baiw.voice.sid");
		/**
		 * 序列号密码
		 */
		private String password = PropertiesLoader.getProperty("sms.baiw.voice.pwn");
		/**
		 * 业务代码
		 */
		private String serviceCode = PropertiesLoader.getProperty("sms.baiw.voice.serviceCode");
		/**
		 * 主叫号码
		 */
		private String callNumber = PropertiesLoader.getProperty("sms.baiw.voice.callNumber");
		
		/**
		 * 实例
		 */
		public static SmsVoiceBaiWuClient getInstance() {
			return sms;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Map sendMessage(String mobileNumber,String content){
			String result = "";
			Map<String,String> retMap = new HashMap<String,String>();
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod(serviceURL);  
			log.info("---start---百悟发送语音内容content|"+content+",mobile|"+mobileNumber);
			post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");//在头文件中设置转码
			NameValuePair[] data ={
				new NameValuePair("accountSid", "ytd01"),
				new NameValuePair("signature", MD5Encode.md5(accountSid + MD5Encode.md5(password) + DateTool.parseDates2(new Date()))),
				new NameValuePair("operaType","voiceCode"),
				new NameValuePair("serviceCode",serviceCode),
				new NameValuePair("callNumber",callNumber),
				new NameValuePair("voiceCode", content),
				new NameValuePair("destNumber", mobileNumber),
				new NameValuePair("fetchDate",DateTool.parseDates2(new Date())),
			};
			post.setRequestBody(data);
			int clientRes=0;
			String resultMess="";
			Map<String,String> mapMess = new HashMap<String,String>();
			try {
				clientRes = client.executeMethod(post);
				//返回类型格式{"respCode":"000000","callId":"f599e3b7-a0f8-4409-90af-5fb72c55ca94","createDate":"20151123110956","result":""}
				resultMess = new String(post.getResponseBodyAsString().getBytes("utf-8"));
				ObjectMapper mapper = new ObjectMapper();
				mapMess = mapper.readValue(resultMess, Map.class);
			}  catch (Exception e) {
				e.printStackTrace();
			} 
			post.releaseConnection();
			if(200==clientRes){
				result = Long.toString(System.currentTimeMillis());
			}
	    	log.info("---end---百悟发送语音结果content|"+content+",mobile|"+mobileNumber+",result|"+resultMess);
	    	if("000000".equals(mapMess.get("respCode"))){
	    		retMap.put("state", "0");
	    		retMap.put("resultMess", "发送成功");
	    	}else{
	    		retMap.put("state", "3");
	    	}
	    	if(200==post.getStatusCode()){
	    		retMap.put("extOrderId", result);
	    	}
	    	System.out.println(mapMess.get("respCode"));
	    	return retMap;
		} 
		
		public static void main(String[] args) {
			SmsVoiceBaiWuClient bt = new SmsVoiceBaiWuClient();
			bt.sendMessage("13520159812", "5678");
		}
}