package com.lottery.print.service;

import java.util.Map;

/**
 * 
 * @ClassName: ISendSmsService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fanghua
 * @date 2014年3月17日 下午5:13:18
 * 
 */
public interface ISendSmsService {
	public Map SendSms(String toName, String title, String content,
			Integer messType);
	public Map SendVoice(String toName, String title, String content,
			String merchName);
}
