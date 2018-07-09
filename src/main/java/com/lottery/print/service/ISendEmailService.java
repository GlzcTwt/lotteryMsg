package com.lottery.print.service;

import java.util.Map;

/**
 * 
 * @ClassName: ISendEmailService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fanghua
 * @date 2014年3月17日 下午5:21:49
 * 
 */
public interface ISendEmailService {

	public Map SendEmail(String toName, String title, String content);

}
