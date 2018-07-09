package com.lottery.print.model;

/**
 * 
 * @创建时间 2014年3月5日 上午11:40:59
 * @author fanghua
 * 
 */
public class RequestEntity {

	private String merchId;
	private int messType;
	private String messNum;
	private String messOrderId;
	private String messContent;
	private String times;
	private String sign;
	private String messTitle;

	public String getMerchId() {
		return merchId;
	}

	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}

	public int getMessType() {
		return messType;
	}

	public void setMessType(int messType) {
		this.messType = messType;
	}

	public String getMessNum() {
		return messNum;
	}

	public void setMessNum(String messNum) {
		this.messNum = messNum;
	}

	public String getMessOrderId() {
		return messOrderId;
	}

	public void setMessOrderId(String messOrderId) {
		this.messOrderId = messOrderId;
	}

	public String getMessContent() {
		return messContent;
	}

	public void setMessContent(String messContent) {
		this.messContent = messContent;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMessTitle() {
		return messTitle;
	}

	public void setMessTitle(String messTitle) {
		this.messTitle = messTitle;
	}
	
}
