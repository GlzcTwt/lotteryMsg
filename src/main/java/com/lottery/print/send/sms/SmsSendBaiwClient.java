package com.lottery.print.send.sms;

import java.util.HashMap;
import java.util.Map;

import com.lottery.print.util.PropertiesLoader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SmsSendBaiwClient {

	private static final Log log = LogFactory.getLog(SmsSendBaiwClient.class);
	private static final SmsSendBaiwClient sms = new SmsSendBaiwClient();
	private String SMS_SIGN = "【易通贷】";
	
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
	private String serviceURL = PropertiesLoader.getProperty("sms.baiw.url");
	
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	private String id = PropertiesLoader.getProperty("sms.baiw.id");
	
	/**
	 * 账户密码
	 */
	private String pwd = PropertiesLoader.getProperty("sms.baiw.pwn");
	/**
	 * 业务代码
	 */
	private String serviceCode = PropertiesLoader.getProperty("sms.baiw.serviceCode");
	
	/**
	 * 实例
	 */
	public static SmsSendBaiwClient getInstance() {
		return sms;
	}
	
	/**
	 * 返回值
	 */
	private Map<String, String> map;
	
	private SmsSendBaiwClient(){
		map = new HashMap<String, String>();
		map.put("0#", "0#开头表示发送成功");
		map.put("100", "余额不足");
		map.put("101", "账号关闭");
		map.put("102", "短信内容超过500字或为空或内容编码格式不正确");
		map.put("103", "手机号码超过200个或合法手机号码为空或者与通道类型不匹配");
		map.put("106", "用户名不存在");
		map.put("107", "密码错误");
		map.put("108", "指定访问ip错误");
		map.put("109", "业务不存在或者通道关闭");
		map.put("110", "小号不合法");
	}

	
	/**
	 * 发送短信
	 * TODO
	 * @param mobileNumber	手机号码
	 * @param content	发送短信内容
	 * @return 
	 * <br>----------------------------------------------------变更记录--------------------------------------------------
	 * <br> <br> 序号      |            时间                         |   作者       |         描述       
	 * <br>  0     |  2015-1-14 上午11:34:54  |  付梦怡    |    创建
	 */
	public Map<String, String> sendMessage(String mobileNumber,String content){
		Map<String, String> retMap = new HashMap<String, String>();
		String result = "";
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(serviceURL);  
		log.info("---start---百悟发送短信内容content|"+content+",mobile|"+mobileNumber);
		post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
		NameValuePair[] data ={
			new NameValuePair("corp_id", id),
			new NameValuePair("corp_pwd", pwd),
			new NameValuePair("corp_service",serviceCode),
			new NameValuePair("mobile",mobileNumber),
			new NameValuePair("msg_content",content)
		};
		post.setRequestBody(data);
		int clientRes=0;
		String resultMess="";
		try {
			clientRes = client.executeMethod(post);
			resultMess = new String(post.getResponseBodyAsString().getBytes("gbk"));
		}  catch (Exception e) {
			e.printStackTrace();
		} 
    	post.releaseConnection();
    	
    	if(200==clientRes){
			result = Long.toString(System.currentTimeMillis());
		}

    	log.info("---end---百悟发送短信结果content|"+content+",mobile|"+mobileNumber+",result|"+resultMess);
    	
    	if(resultMess.startsWith("0#")){
    		retMap.put("state", "0");
    		retMap.put("resultMess", "发送成功");
    	}else
    		retMap.put("state", "3");
    	if(200==post.getStatusCode())
    	retMap.put("extOrderId", result);
		return retMap;
	}
	
	public static void main(String[] args) {
	SmsSendBaiwClient baiw = SmsSendBaiwClient.getInstance();
	System.out.println(baiw.sendMessage("13520159812",
			"恭喜您，您可凭借此验证码:bkyxefz 参加网贷天眼嘉年华活动哦！网页链接：http://licai.p2peye.com/topic/carnival"));
    }
}
