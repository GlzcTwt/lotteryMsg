/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.lottery.print.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户表
 */
@SuppressWarnings("serial")
public class TbMerchantInfo implements Serializable {

	// columns START
	/**
	 * 商户ID(100001)
	 */
	private Long merchId;
	/**
	 * 商户名称
	 */
	private java.lang.String merchName;
	private Date createTime;

	private int state;
	private String merchKey;
	private String merchCallUrl;
	private String merchDec;

	public Long getMerchId() {
		return merchId;
	}

	public void setMerchId(Long merchId) {
		this.merchId = merchId;
	}

	public java.lang.String getMerchName() {
		return merchName;
	}

	public void setMerchName(java.lang.String merchName) {
		this.merchName = merchName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMerchKey() {
		return merchKey;
	}

	public void setMerchKey(String merchKey) {
		this.merchKey = merchKey;
	}

	public String getMerchCallUrl() {
		return merchCallUrl;
	}

	public void setMerchCallUrl(String merchCallUrl) {
		this.merchCallUrl = merchCallUrl;
	}

	public String getMerchDec() {
		return merchDec;
	}

	public void setMerchDec(String merchDec) {
		this.merchDec = merchDec;
	}

}