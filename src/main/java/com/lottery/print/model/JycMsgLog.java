/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.lottery.print.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信记录表
 */
public class JycMsgLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// columns START
	/**
	 * 交易号
	 */
	private Long id;
	/**
	 * 商户ID
	 */
	private Long merchId;
	/**
	 * 类型
	 */
	private int messType;
	/**
	 * 通知号码
	 */
	private java.lang.String messNumber;
	/**
	 * 标题
	 */
	private String messTitle;
	/**
	 * 内容
	 */
	private String messContent;
	/**
	 * 状态（0：发送成功，1：接收消息，2：发送消息，3：发送失败）
	 */
	private int tradeState;
	/**
	 * 记录时间
	 */
	private Date timeInsert;
	/**
	 * 发送时间
	 */
	private Date timeSend;
	/**
	 * 外部订单号
	 */
	private String extOrderId;
	/**
	 * 回调地址
	 */
	private java.lang.String callUrl;
	/**
	 * 回调时间
	 */
	private Date callTime;
	/**
	 * 回调次数
	 */
	private Integer callCount;

	private Date timeFinish;
	private Integer callState;

	private String merchOrderId;
	private String merchKey;
	private String merchName;

	// columns END

	public JycMsgLog() {
	}

	public JycMsgLog(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMerchId(Long merchId) {
		this.merchId = merchId;
	}

	public Long getMerchId() {
		return this.merchId;
	}

	public void setMessType(int messType) {
		this.messType = messType;
	}

	public int getMessType() {
		return this.messType;
	}

	public void setMessNumber(java.lang.String messNumber) {
		this.messNumber = messNumber;
	}

	public java.lang.String getMessNumber() {
		return this.messNumber;
	}

	public void setMessContent(String messContent) {
		this.messContent = messContent;
	}

	public String getMessContent() {
		return this.messContent;
	}

	public void setTimeInsert(Date timeInsert) {
		this.timeInsert = timeInsert;
	}

	public Date getTimeInsert() {
		return this.timeInsert;
	}

	public void setCallUrl(java.lang.String callUrl) {
		this.callUrl = callUrl;
	}

	public java.lang.String getCallUrl() {
		return this.callUrl;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public Date getCallTime() {
		return this.callTime;
	}

	public void setCallCount(Integer callCount) {
		this.callCount = callCount;
	}

	public Integer getCallCount() {
		return this.callCount;
	}

	public String getMessTitle() {
		return messTitle;
	}

	public void setMessTitle(String messTitle) {
		this.messTitle = messTitle;
	}

	public void setTradeState(int tradeState) {
		this.tradeState = tradeState;
	}

	public String getExtOrderId() {
		return extOrderId;
	}

	public void setExtOrderId(String extOrderId) {
		this.extOrderId = extOrderId;
	}

	public String getMerchOrderId() {
		return merchOrderId;
	}

	public void setMerchOrderId(String merchOrderId) {
		this.merchOrderId = merchOrderId;
	}

	public int getTradeState() {
		return tradeState;
	}

	public String getMerchKey() {
		return merchKey;
	}

	public void setMerchKey(String merchKey) {
		this.merchKey = merchKey;
	}

	public String getMerchName() {
		return merchName;
	}

	public void setMerchName(String merchName) {
		this.merchName = merchName;
	}

	public Date getTimeSend() {
		return timeSend;
	}

	public void setTimeSend(Date timeSend) {
		this.timeSend = timeSend;
	}

	public Date getTimeFinish() {
		return timeFinish;
	}

	public void setTimeFinish(Date timeFinish) {
		this.timeFinish = timeFinish;
	}

	public Integer getCallState() {
		return callState;
	}

	public void setCallState(Integer callState) {
		this.callState = callState;
	}

}
